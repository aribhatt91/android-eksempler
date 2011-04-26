package eks.lister;

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
  private ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = { "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal" };

    listView= new ListView(this);
    listView.setOnItemClickListener(this);
    listView.setAdapter(new ArrayAdapter(this, R.layout.listeelement,  R.id.listeelem_overskrift, lande ));

    setContentView(listView);
  }


  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på "+ position, Toast.LENGTH_SHORT).show();
  }
}
