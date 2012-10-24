package eks.lister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class BenytSpinner extends Activity implements OnItemSelectedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Nepal"};
		Spinner liste = new Spinner(this);
		liste.setOnItemSelectedListener(this);
    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, android.R.id.text1, lande);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    liste.setAdapter(adapter);
    liste.setPrompt("Vælg et land");

    TableLayout tl = new TableLayout(this);
    tl.addView(liste);
		setContentView(tl);
	}

	public void onItemSelected(AdapterView<?> liste, View v, int position, long id) {
		Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
	}

  public void onNothingSelected(AdapterView<?> liste) {
		Toast.makeText(this, "Intet valgt", Toast.LENGTH_SHORT).show();
  }
}
