package eks.lister;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import dk.nordfalk.android.elementer.R;


public class BenytListViewMedEgetLayout2 extends Activity implements OnItemClickListener {
  private ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = { "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal",  };

    listView= new ListView(this);
    listView.setOnItemClickListener(this);

    listView.setAdapter(new ArrayAdapter(this, R.layout.listeelement,  R.id.listeelem_overskrift, lande )
    {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView listeelem_beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
        listeelem_beskrivelse.setText("Land nummer "+position);
        ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);
        if (position % 3 == 2) listeelem_billede.setImageResource(android.R.drawable.sym_action_call);
        else  listeelem_billede.setImageResource(android.R.drawable.sym_action_email);
        return view;
    }
    });

    //listView.setDivider(getResources().getDrawable(android.R.drawable.divider_horizontal_dark));
    // Rød kasse omkring det valgte element
    listView.setSelector(android.R.drawable.ic_notification_overlay);

    setContentView(listView);
  }


  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på "+ position, Toast.LENGTH_SHORT).show();
  }
}
