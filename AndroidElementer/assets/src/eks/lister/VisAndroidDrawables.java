package eks.lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;


public class VisAndroidDrawables extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ListView listView= new ListView(this);
    listView.setAdapter(new AndroidDrawablesAdapter());
    setContentView(listView);
  }


  public class AndroidDrawablesAdapter extends BaseAdapter {
    public int getCount() { return 1500; } // der er omkring tusind drawables

    public Object getItem(int position) { return position; } // bruges ikke
    public long getItemId(int position) { return position; } // bruges ikke

    public View getView(int position, View view, ViewGroup parent) {
      if (view==null) view = getLayoutInflater().inflate(R.layout.listeelement, null);
      TextView listeelem_overskrift = (TextView) view.findViewById(R.id.listeelem_overskrift);
      TextView listeelem_beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
      ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);

      int resurseId = android.R.drawable.alert_dark_frame + position; // første resurse

      listeelem_overskrift.setText( Integer.toString(resurseId) );
      listeelem_beskrivelse.setText( "Hex: " + Integer.toHexString(resurseId) );
      listeelem_billede.setImageResource(resurseId);

      return view;
    }

  }
}
