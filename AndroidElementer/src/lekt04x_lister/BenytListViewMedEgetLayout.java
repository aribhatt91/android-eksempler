package lekt04x_lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class BenytListViewMedEgetLayout extends Activity implements OnItemClickListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal"};
    ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listeelement, R.id.listeelem_overskrift, lande);

    ListView liste = new ListView(this);
    liste.setOnItemClickListener(this);
    liste.setAdapter(adapter);

    setContentView(liste);
  }

  public void onItemClick(AdapterView<?> liste, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
    // v vil pege på det LinearLayout der er roden i R.layout.listeelement
    //Toast.makeText(this, " v = " + v, Toast.LENGTH_SHORT).show();
  }
}
