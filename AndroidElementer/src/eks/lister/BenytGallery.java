package eks.lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import dk.nordfalk.android.elementer.R;


public class BenytGallery extends Activity implements OnItemClickListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String[] lande = { "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal",  };

    Gallery listView= new Gallery(this);
    listView.setOnItemClickListener(this);
    listView.setSpacing(25); // 25 punkter

    listView.setAdapter(new ArrayAdapter(this, R.layout.listeelement,  R.id.listeelem_overskrift, lande ));
    setContentView(listView);

  }


  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på "+ position, Toast.LENGTH_SHORT).show();
  }
}
