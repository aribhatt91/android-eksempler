package eks.lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.Toast;
import dk.nordfalk.android.elementer.R;

public class BenytGridView extends Activity implements OnItemClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal", "Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal",};

		GridView gridView = new GridView(this);
		gridView.setOnItemClickListener(this);
		gridView.setNumColumns(GridView.AUTO_FIT);

		gridView.setAdapter(new ArrayAdapter(this, R.layout.listeelement, R.id.listeelem_overskrift, lande));

		setContentView(gridView);
	}

	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
	}
}
