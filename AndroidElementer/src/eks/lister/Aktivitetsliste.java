package eks.lister;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import dk.nordfalk.android.elementer.R;


public class Aktivitetsliste extends Activity implements OnItemClickListener {
  private ActivityInfo[] aktiviteter;
  private CheckBox startDirekte;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ListView listView= new ListView(this);

    try {
      aktiviteter =getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES).activities;
    } catch (NameNotFoundException ex) {
      ex.printStackTrace();
    }


    listView.setAdapter(new ArrayAdapter(this, R.layout.listeelement,  R.id.listeelem_overskrift, aktiviteter )
    { // Anonym nedarving af ArrayAdapter med omdefineret getView()
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView listeelem_overskrift = (TextView) view.findViewById(R.id.listeelem_overskrift);
        TextView listeelem_beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
        ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);
        
        String pakkeOgKlasse = aktiviteter[position].name;
        String pakkenavn =  pakkeOgKlasse.substring(0, pakkeOgKlasse.lastIndexOf("."));
        String klassenavn =  pakkeOgKlasse.substring(pakkenavn.length() +1);

        listeelem_overskrift.setText( klassenavn );
        listeelem_beskrivelse.setText( pakkenavn);

        // Lad billedet på en eller anden måde afspejle pakkenavnet
        listeelem_billede.setImageResource(17301855  +Math.abs(pakkenavn.hashCode()%10));
        //listeelem_billede.setImageResource(android.R.drawable.ic_media_ff + pakkenavn.hashCode()%30);
        //listeelem_billede.setBackgroundColor( pakkenavn.hashCode() & 0x007f7f7f | 0xff000000 );
        //listeelem_billede.setBackgroundColor( pakkenavn.hashCode() | 0xff000000 );
        //listeelem_billede.setBackgroundColor( Color.HSVToColor(new float[] {pakkenavn.hashCode()%360, 1, 0.8f}));
        //listeelem_billede.setColorFilter(pakkenavn.hashCode() | 0x3f000000, Mode.SRC_ATOP);

        return view;
      }
    });
    SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
    int position = prefs.getInt("position", 0);
    listView.setSelectionFromTop( position, 30);
    listView.setOnItemClickListener(this);

    startDirekte = new CheckBox(this);
    startDirekte.setText("Start aktivitet direkte næste gang");
    startDirekte.setChecked( PreferenceManager.getDefaultSharedPreferences(this).getBoolean("startDirekte", false) );
    if (startDirekte.isChecked()) onItemClick(listView, null, position, 0); // hack - 'klik' på listen!

    TableLayout linearLayout = new TableLayout(this);
    linearLayout.addView(startDirekte);
    linearLayout.addView(listView);

    setContentView(linearLayout);
  }


  public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
    ActivityInfo aktivitetsinfo=aktiviteter[position];
    Toast.makeText(this, "Starter "+ aktivitetsinfo.name, Toast.LENGTH_SHORT).show();

    Intent intent = new Intent();
    intent.setClassName(aktivitetsinfo.applicationInfo.packageName, aktivitetsinfo.name);
    startActivity(intent);

    // Gem position og 'start aktivitet direkte' til næste gang
    PreferenceManager.getDefaultSharedPreferences(this).edit()
        .putInt("position", position).putBoolean("startDirekte", startDirekte.isChecked()).commit();
  }
}
