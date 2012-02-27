package begynder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import eks.vejret.ByvejrAktivitet;
import eks.vejret.Indstillinger;

/**
 *
 * @author Jacob Nordfalk
 */
public class BenytMenuer1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("Tryk på menu-knappen\n");
		setContentView(tv);
	}

	/** Kaldes én gang for at forberede menuen */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 101, Menu.NONE, "Start vejrudsigt");
		menu.add(Menu.NONE, 102, Menu.NONE, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}

	/** Kaldes hvis brugeren vælger noget i menuen */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 101) {
			Intent i = new Intent(this, ByvejrAktivitet.class);
			startActivity(i);
			return true;
		} else if (item.getItemId() == 102) {
			Intent i = new Intent(this, Indstillinger.class);
			startActivity(i);
			return true;
		} else {
			// Ikke håndteret - send kaldet videre til standardhåntering
			return super.onOptionsItemSelected(item);
		}
	}
}
