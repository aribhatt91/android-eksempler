package eks.livscyklus;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;

/**
 *
 * @author Jacob Nordfalk
 */
public class Liv4_bundle extends LogAktivitet {

  Programdata data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    data = new Programdata();
    if (savedInstanceState == null) {
      data.liste.add("første element");
    } else {
      data.etTal = savedInstanceState.getInt("etTal");
      data.etAndetTal = savedInstanceState.getInt("etAndetTal");
      data.liste = savedInstanceState.getStringArrayList("liste");
      //data = (Programdata) savedInstanceState.getSerializable("data");
      data.liste.add("dataFraForrigeAkrivitet "+data.liste.size());
    }

    EditText tv1 = new EditText(this);
    tv1.setText( data.toString() );

    EditText tv2 = new EditText(this);
    tv2.setText("Et view uden id");

    EditText tv3 = new EditText(this);
    tv3.setText("Et view med id");
    tv3.setId(1000042); // bare et eller andet

    TableLayout tl = new TableLayout(this);
    tl.addView(tv1);
    tl.addView(tv2);
    tl.addView(tv3);
    setContentView(tl);
  }


  @Override
  protected void onRestoreInstanceState (Bundle savedInstanceState) {
     // her genskabes indhold for alle views med id
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState); // gem indhold for alle views med id
    outState.putInt("etTal", data.etTal++);
    outState.putInt("etAndetTal", data.etAndetTal);
    outState.putStringArrayList("liste", data.liste);
    outState.putSerializable("data", data);
  }
}
