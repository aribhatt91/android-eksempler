package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import dk.nordfalk.android.elementer.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Aktivitetsliste extends Activity implements OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {

  /** Programdata - static da de ikke fylder det store og vi dermed slipper for reinitialisering */
  static ArrayList<String> alleAktiviteter = new ArrayList<String>();
  static ArrayList<String> kategorier;
  static HashMap<Integer, ArrayList<String>> kategorivalgTilVisKlasserCache = new HashMap<Integer, ArrayList<String>>();


  ArrayList<String> visKlasser = new ArrayList<String>();

  TextView textView;
  ArrayAdapter<String> visKlasserAdapter;
  CheckBox autostart;
  int onStartTæller;
  ToggleButton seKildekode;
  Gallery kategorivalg;
  //EditText søgEditText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (alleAktiviteter.isEmpty()) { // Førstegangsinitialisering af programdata
      try {
        for (ActivityInfo a : getPackageManager().
                getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities) {
          alleAktiviteter.add(a.name);
        }
      } catch (NameNotFoundException ex) {
        ex.printStackTrace();
      }

      LinkedHashSet<String> kategorisæt = new LinkedHashSet<String>();
      //kategorier.add("(søg)");
      kategorisæt.add(" = vis alle = ");
      for (String pnavn : alleAktiviteter) {
        pnavn = pnavn.substring(0, pnavn.lastIndexOf(".")); // Fjern klassenavnet
        if (pnavn.startsWith("eks"))
          pnavn=pnavn.substring(4); // tag 'diverse' fra 'eks.diverse'
        kategorisæt.add(pnavn);
      }
      kategorisæt.add("levendebaggrund");
      kategorisæt.add("levendeikon");
      kategorier = new ArrayList(kategorisæt);

      // Start asynkron indlæsning af kategorivalgTilVisKlasserCache
      new Thread() {
        public void run() {
          for (int i=0; i<kategorier.size(); i++) findKlasserIKategori(i);
        }
      }.start();
    }


    //kategorier.add("(søg)");
    /*
    søgEditText = new EditText(this);
    søgEditText.setHint("Søg");
    søgEditText.setSingleLine();
    søgEditText.setEnabled(true);
    søgEditText.setOnKeyListener(this);
     */

    kategorivalg = new Gallery(this);
    kategorivalg.setAdapter(new ArrayAdapter(this, android.R.layout.simple_gallery_item, android.R.id.text1, kategorier));
    kategorivalg.setSpacing(10);
    kategorivalg.setVerticalScrollBarEnabled(true);
    kategorivalg.setOnItemSelectedListener(this);
    kategorivalg.setUnselectedAlpha(0.4f);
    kategorivalg.setBackgroundColor(Color.DKGRAY);
    if (savedInstanceState == null) // Frisk start - vis animation
      kategorivalg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.egen_anim2));


    visKlasser.addAll(alleAktiviteter);
    // Anonym nedarving af ArrayAdapter med omdefineret getView()
    visKlasserAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, visKlasser)
    {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view=super.getView(position, convertView, parent);
        TextView listeelem_overskrift=(TextView) view.findViewById(android.R.id.text1);
        TextView listeelem_beskrivelse=(TextView) view.findViewById(android.R.id.text2);

        String pakkeOgKlasse=visKlasser.get(position);
        if (pakkeOgKlasse.endsWith(".java")) {
          String pakkenavn=pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf('/'));
          String klassenavn=pakkeOgKlasse.substring(pakkenavn.length()+1);
          listeelem_overskrift.setText(klassenavn);
          listeelem_beskrivelse.setText(pakkeOgKlasse);
        } else {
          String pakkenavn=pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf('.'));
          String klassenavn=pakkeOgKlasse.substring(pakkenavn.length()+1);
          listeelem_overskrift.setText(klassenavn);
          listeelem_beskrivelse.setText(pakkenavn);
        }

        return view;
      }
    };
    ListView visKlasserListView=new ListView(this);
    visKlasserListView.setAdapter(visKlasserAdapter);

    visKlasserListView.setOnItemClickListener(this);
    visKlasserListView.setOnItemLongClickListener(this);

    boolean startetFraLauncher = Intent.ACTION_MAIN.equals(getIntent().getAction());

    autostart = new CheckBox(this);
    autostart.setText("Start automatisk aktivitet næste gang");

    textView = new TextView(this);
    textView.setText("Se kildekode med langt tryk");

    seKildekode = new ToggleButton(this);
    seKildekode.setTextOff("Se kilde");
    seKildekode.setTextOn("Se kilde");
    seKildekode.setChecked(false);


    // Layout
    TableLayout tl=new TableLayout(this);
    //tl.addView(textView);
    tl.addView(visKlasserListView, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
    //tl.addView(visKlasserListView);
    //((LinearLayout.LayoutParams) visKlasserListView.getLayoutParams()).weight = 1; // Stræk listen
    TableRow tr = new TableRow(this);
    tr.addView(seKildekode);
    //tr.addView(søgEditText);


    tr.addView(kategorivalg, new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1));
    tl.addView(tr);

    setContentView(tl);


    // Genskab valg fra sidst der blev startet en aktivitet
    SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
    int position=prefs.getInt("position", 0);
    visKlasserListView.setSelectionFromTop(position, 30);
    autostart.setChecked(prefs.getBoolean("autostart", false));
    if (autostart.isChecked() && startetFraLauncher) {
      onItemClick(visKlasserListView, null, position, 0); // hack - 'klik' på listen!
    }

    // Sæt ID'er så vi understøtter vending
    visKlasserListView.setId(117);
    kategorivalg.setId(118);
    seKildekode.setId(119);

  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode ==  KeyEvent.KEYCODE_BACK) {
      // nulstil først kategorivalg, afslut derefter
      if (kategorivalg.getSelectedItemPosition() > 0) {
        kategorivalg.setSelection(0, true);
      } else {
        finish();
      }
      return true;
    } else if (keyCode ==  KeyEvent.KEYCODE_SEARCH) {
      // søgning her?
    }
    //tv.append("\nonKeyDown "+keyCode);
    return super.onKeyDown(keyCode, event);
  }


  @Override
  public void onStart() {
    super.onStart();
    if (onStartTæller++ == 2) Toast.makeText(this, "Vink: Tryk længe på et punkt for at se kildekoden", Toast.LENGTH_LONG).show();
  }


  public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
    String akt=visKlasser.get(position);

    if (seKildekode.isChecked() || akt.endsWith(".java")) {
      visKildekode(akt);
      return;
    }

    try {
      // Tjek at klassen faktisk kan indlæses (så prg ikke crasher hvis den ikke kan!)
      Class klasse = Class.forName(akt);
      startActivity(new Intent(this, klasse));
      overridePendingTransition(0, 0); // hurtigt skift
      Toast.makeText(this, "Starter "+akt, Toast.LENGTH_SHORT).show();
    } catch (Throwable e) {
      e.printStackTrace();
      //while (e.getCause() != null) e = e.getCause(); // Hop hen til grunden
      AlertDialog.Builder dialog=new AlertDialog.Builder(this);
      dialog.setTitle("Kunne ikke starte");
      TextView tv = new TextView(this);
      tv.setText(akt+" gav fejlen:\n"+Log.getStackTraceString(e));
      dialog.setView(tv);
      //dialog.setMessage(aktivitetsinfo.name+" gav fejlen:\n"+Log.getStackTraceString(e));
      dialog.show();
    }

    // Find position i fuld liste
    position = alleAktiviteter.indexOf(akt);
    // Gem position og 'start aktivitet direkte' til næste gang
    PreferenceManager.getDefaultSharedPreferences(this).edit().
        putInt("position", position).putBoolean("autostart", autostart.isChecked()).commit();
  }

  public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
    visKildekode(visKlasser.get(position));
    return true;
  }


  /**
   *
   * @param position
   */
  private void visKildekode(String klasse) {
    String filnavn=klasse;
    if (!filnavn.endsWith(".java")) {
      filnavn = filnavn.replace('.', '/')+".java";
    }
    Toast.makeText(this, "Viser "+filnavn, Toast.LENGTH_LONG).show();

    Intent i = new Intent(this, VisKildekode.class);
    i.putExtra(VisKildekode.KILDEKODE_FILNAVN, "src/"+filnavn);
    startActivity(i);
  }



  private synchronized ArrayList<String> findKlasserIKategori(int position) {
    if (position==0) return alleAktiviteter; // Vis alle aktiviteter

    // Tjek cache
    ArrayList<String> klasser = kategorivalgTilVisKlasserCache.get(position);
    if (klasser != null) return klasser;

    //else if ("(søg)".equals(kategori)) visKlasser.addAll(alleAktiviteter);
    klasser = new ArrayList<String>();
    String pnavn = kategorier.get(position);
    if (!pnavn.contains(".")) pnavn = "eks."+pnavn; // put eks. foran i pakkkenavn
    for (String a : alleAktiviteter)
      if (a.contains(pnavn)) klasser.add(a);
    // if (a.toLowerCase().contains(kategori)) visKlasser.add(a); // kun nødvendig til søgning
    try { // Skan efter filer der ikke er aktiviteter og vis også dem
      //System.out.println(visKlasser);
      String mappe = pnavn.replace(".", "/");
      ydre:
      for (String fil : getAssets().list("src/"+mappe)) {
        String klassenavn = fil.substring(0, fil.lastIndexOf(".")); // stryg filendelse
        //System.out.println(klassenavn);
        for (String n : klasser) if (n.endsWith(klassenavn)) continue ydre; // allerede listet
        klasser.add(mappe+"/"+fil);
      }
    } catch (IOException ex) {
     ex.printStackTrace();
    }

    kategorivalgTilVisKlasserCache.put(position, klasser);
    return klasser;
  }


  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    //tv.setText("onItemSelected "+position+" "+kategorivalg.getSelectedItemPosition());
    this.visKlasser.clear();
    this.visKlasser.addAll(findKlasserIKategori(position));
    visKlasserAdapter.notifyDataSetChanged();
  }

  public void onNothingSelected(AdapterView<?> parent) { }

}
