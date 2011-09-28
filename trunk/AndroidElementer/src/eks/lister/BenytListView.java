package eks.lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class BenytListView extends Activity implements OnItemClickListener {
  private ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = { "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal" };
    listView= new ListView(this);
    listView.setOnItemClickListener(this);
    listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,  android.R.id.text1, lande ));

    setContentView(listView);
  }


  public void onItemClick(AdapterView<?> liste, View v, int position, long id) {
    Toast.makeText(this, "Klik på "+ position, Toast.LENGTH_SHORT).show();
  }
}
