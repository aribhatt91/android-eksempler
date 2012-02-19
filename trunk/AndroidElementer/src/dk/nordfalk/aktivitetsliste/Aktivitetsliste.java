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
import android.widget.Gallery;
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
import java.util.HashSet;
import java.util.LinkedHashMap;

public class Aktivitetsliste extends Activity implements OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {

	/** Programdata - static da de ikke fylder det store og vi dermed slipper for reinitialisering */
	static ArrayList<String> alleAktiviteter = new ArrayList<String>();
	static ArrayList<String> pakkenavne;
	static ArrayList<String> pakkekategorier;
	static ArrayList<ArrayList<String>> klasselister = new ArrayList<ArrayList<String>>();

	ArrayList<String> klasserDerVisesNu = new ArrayList<String>();
	ArrayAdapter<String> klasserDerVisesNuAdapter;
	int onStartTæller;
	ToggleButton seKildekode;
	Gallery kategorivalg;
	//EditText søgEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final long tid = System.currentTimeMillis();

		if (alleAktiviteter.isEmpty()) { // Førstegangsinitialisering af programdata
			try {
				for (ActivityInfo a : getPackageManager().
						getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities) {
					alleAktiviteter.add(a.name);
				}
			} catch (NameNotFoundException ex) {
				ex.printStackTrace();
			}
			alleAktiviteter.add("AndroidManifest.xml");

			LinkedHashMap<String, Integer> pakkeTilPosition = new LinkedHashMap<String, Integer>();
			//kategorier.add("(søg)");
			pakkeTilPosition.put(" = vis alle = ", 0);
			klasselister.add(alleAktiviteter);
			for (String navn : alleAktiviteter) {
				int n = navn.lastIndexOf(".");
				String pakkenavn = navn.substring(0, n); // Fjern klassenavnet
				//String klassenavn = navn.substring(n+1); // Klassenavnet

				Integer position = pakkeTilPosition.get(pakkenavn);
				ArrayList klasser;
				if (position == null) {
					position = pakkeTilPosition.size();
					pakkeTilPosition.put(pakkenavn, position);
					klasser = new ArrayList<String>();
					klasselister.add(klasser);
				} else {
					klasser = klasselister.get(position);
				}
				klasser.add(navn);
			}

			pakkeTilPosition.put("eks.levendebaggrund", pakkeTilPosition.size());
			klasselister.add(new ArrayList<String>());
			pakkeTilPosition.put("eks.levendeikon", pakkeTilPosition.size());
			klasselister.add(new ArrayList<String>());
			pakkenavne = new ArrayList(pakkeTilPosition.keySet());
			pakkekategorier = new ArrayList(pakkenavne); // tag kopi og ændr den
			for (int i=1; i<pakkekategorier.size(); i++) {
				String pakkenavn = pakkekategorier.get(i);
				if (pakkenavn.startsWith("eks")) {
					pakkenavn = pakkenavn.substring(4); // tag 'diverse' fra 'eks.diverse'
					pakkekategorier.set(i, pakkenavn);
				}
			}


			// Start asynkron indlæsning af klasselister
			new Thread() {
				public void run() {
					for (int i = 1; i < pakkekategorier.size(); i++) {
						try { // Vent lidt for at lade systemet starte op
							Thread.sleep(500);
						} catch (Exception ex) { }
						tjekForAndreFilerIPakken(i);
						Log.d("Aktivitetsliste", "T "+i+" tid: "+(System.currentTimeMillis()-tid));
					}
				}
			}.start();

			Toast.makeText(this, "Lav langt tryk for at se kildekoden\n", Toast.LENGTH_LONG).show();
			Log.d("Aktivitetsliste", "1 tid: "+(System.currentTimeMillis()-tid));
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
		kategorivalg.setAdapter(new ArrayAdapter(this, android.R.layout.simple_gallery_item, android.R.id.text1, pakkekategorier));
		kategorivalg.setSpacing(10);
		kategorivalg.setVerticalScrollBarEnabled(true);
		kategorivalg.setOnItemSelectedListener(this);
		kategorivalg.setUnselectedAlpha(0.4f);
		kategorivalg.setBackgroundColor(Color.DKGRAY);
		if (savedInstanceState == null) // Frisk start - vis animation
		{
			kategorivalg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.egen_anim2));
		}


		klasserDerVisesNu.addAll(alleAktiviteter);
		// Anonym nedarving af ArrayAdapter med omdefineret getView()
		klasserDerVisesNuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, klasserDerVisesNu) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView listeelem_overskrift = (TextView) view.findViewById(android.R.id.text1);
				TextView listeelem_beskrivelse = (TextView) view.findViewById(android.R.id.text2);

				String pakkeOgKlasse = klasserDerVisesNu.get(position);
				if (pakkeOgKlasse.endsWith(".java")) {
					String pakkenavn = pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf('/'));
					String klassenavn = pakkeOgKlasse.substring(pakkenavn.length() + 1);
					listeelem_overskrift.setText(klassenavn);
					listeelem_beskrivelse.setText(pakkeOgKlasse);
				} else if (pakkeOgKlasse.endsWith(".xml")) {
					listeelem_overskrift.setText(pakkeOgKlasse);
					listeelem_beskrivelse.setText("");
				} else {
					String pakkenavn = pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf('.'));
					String klassenavn = pakkeOgKlasse.substring(pakkenavn.length() + 1);
					listeelem_overskrift.setText(klassenavn);
					listeelem_beskrivelse.setText(pakkenavn);
				}

				return view;
			}
		};
		ListView visKlasserListView = new ListView(this);
		Log.d("Aktivitetsliste", "2 tid: "+(System.currentTimeMillis()-tid));
		visKlasserListView.setAdapter(klasserDerVisesNuAdapter);
		Log.d("Aktivitetsliste", "3 tid: "+(System.currentTimeMillis()-tid));

		visKlasserListView.setOnItemClickListener(this);
		visKlasserListView.setOnItemLongClickListener(this);

		seKildekode = new ToggleButton(this);
		seKildekode.setTextOff("Se kilde");
		seKildekode.setTextOn("Se kilde");
		seKildekode.setChecked(false);


		// Layout
		TableLayout tl = new TableLayout(this);
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


		// Sæt ID'er så vi understøtter vending
		visKlasserListView.setId(117);
		kategorivalg.setId(118);
		seKildekode.setId(119);

		// Genskab valg fra sidst der blev startet en aktivitet
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int position = prefs.getInt("position", 0);
		visKlasserListView.setSelectionFromTop(position, 30);
		kategorivalg.setSelection(prefs.getInt("kategoriPos", 1));

		Log.d("Aktivitetsliste", "4 tid: "+(System.currentTimeMillis()-tid));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// nulstil først kategorivalg, afslut derefter
			if (kategorivalg.getSelectedItemPosition() > 0) {
				//kategorivalg.setSelection(0, true);
				kategorivalg.onFling(null, null, 3000, 0);
			} else {
				finish();
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			// søgning her?
		}
		//tv.append("\nonKeyDown "+keyCode);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (onStartTæller++ == 2) {
			Toast.makeText(this, "Vink: Tryk længe på et punkt for at se kildekoden", Toast.LENGTH_LONG).show();
		}
	}

	public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
		String akt = klasserDerVisesNu.get(position);

		if (seKildekode.isChecked() || akt.endsWith(".java") || akt.endsWith(".xml")) {
			visKildekode(akt);
			return;
		}

		try {
			// Tjek at klassen faktisk kan indlæses (så prg ikke crasher hvis den ikke kan!)
			Class klasse = Class.forName(akt);
			startActivity(new Intent(this, klasse));
			overridePendingTransition(0, 0); // hurtigt skift
			Toast.makeText(this, "Starter " + akt, Toast.LENGTH_SHORT).show();
		} catch (Throwable e) {
			e.printStackTrace();
			//while (e.getCause() != null) e = e.getCause(); // Hop hen til grunden
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Kunne ikke starte");
			TextView tv = new TextView(this);
			tv.setText(akt + " gav fejlen:\n" + Log.getStackTraceString(e));
			dialog.setView(tv);
			//dialog.setMessage(aktivitetsinfo.name+" gav fejlen:\n"+Log.getStackTraceString(e));
			dialog.show();
		}

		// Find position i fuld liste
		position = alleAktiviteter.indexOf(akt);
		// Gem position og 'start aktivitet direkte' til næste gang
		PreferenceManager.getDefaultSharedPreferences(this).edit().
				putInt("position", position).
				putInt("kategoriPos", kategorivalg.getSelectedItemPosition()).
				commit();
	}

	public boolean onItemLongClick(AdapterView<?> listView, View v, int position, long id) {
		visKildekode(klasserDerVisesNu.get(position));
		return true;
	}

	/**
	 *
	 * @param position
	 */
	private void visKildekode(String klasse) {
		String filnavn = klasse;
		if (filnavn.equals("AndroidManifest.xml")) {
			// ingen ekstra sti eller andet
		} else {
			filnavn = "src/" + filnavn;
			if (!filnavn.endsWith(".java")) {
				filnavn = filnavn.replace('.', '/') + ".java";
			}
		}

		Toast.makeText(this, "Viser " + filnavn, Toast.LENGTH_LONG).show();

		Intent i = new Intent(this, VisKildekode.class);
		i.putExtra(VisKildekode.KILDEKODE_FILNAVN, filnavn);
		startActivity(i);
	}

	static HashSet<Integer> tjekketForAndreFiler = new HashSet<Integer>();
	private synchronized void tjekForAndreFilerIPakken(int position) {
		if (tjekketForAndreFiler.contains(position)) return;
		tjekketForAndreFiler.add(position);
		String pnavn = pakkenavne.get(position);
		ArrayList<String> klasser = klasselister.get(position);
		Log.d("Aktivitetsliste", "pakkeTilKlasseliste.get "+position+" = "+klasser+" "+pnavn);

		// if (a.toLowerCase().contains(kategori)) klasserDerVisesNu.add(a); // kun nødvendig til søgning
		try { // Skan efter filer der ikke er aktiviteter og vis også dem
			//System.out.println(klasserDerVisesNu);
			String mappe = pnavn.replace(".", "/");
			ydre:
			for (String fil : getAssets().list("src/" + mappe)) {
				String klassenavn = fil.substring(0, fil.lastIndexOf(".")); // stryg filendelse
				//System.out.println(klassenavn);
				for (String n : klasser) {
					if (n.endsWith(klassenavn)) {
						continue ydre; // allerede listet
					}
				}
				klasser.add(mappe + "/" + fil);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	//
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		//tv.setText("onItemSelected "+position+" "+kategorivalg.getSelectedItemPosition());
		this.klasserDerVisesNu.clear();
		tjekForAndreFilerIPakken(position);
		this.klasserDerVisesNu.addAll(klasselister.get(position));
		klasserDerVisesNuAdapter.notifyDataSetChanged();
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}
}
