package eks.livscyklus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

/**
 *
 * @author Jacob Nordfalk
 */
public class Liv1_singleton extends LogAktivitet {

  /**
   * Gem programmets data i en klassevariabel - den er fælles for alle instanser
   */
  static Programdata data;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (data == null) {
      data = new Programdata();
      data.liste.add("første element");
    } else {
      data.liste.add("dataFraForrigeAkrivitet "+data.liste.size());
    }

    EditText et = new EditText(this);
    et.setText( data.toString() );
    setContentView(et);

    this.data.liste.add("hej");
  }
}
