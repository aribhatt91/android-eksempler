package eks.diverse;

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
public class BenytMenuer2 extends Activity {
	private TextView logTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logTextView = new TextView(this);
		logTextView.setText("Tryk på menu-knappen\n");
		setContentView(logTextView);
	}

	/** Kaldes når der trykkes på en fysisk knap (incl MENU) */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		logTextView.append("\nonKeyDown " + keyCode);
		return super.onKeyDown(keyCode, event);
	}

	/** Kaldes én gang for generelt at forberede menuen */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); // tilføj systemets standardmenuer
		logTextView.append("\nonCreateOptionsMenu " + menu);
		menu.add(Menu.NONE, 101, Menu.NONE, "Start vejrudsigt");
		menu.add(Menu.NONE, 102, Menu.NONE, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}

	/** Kaldes hver gang menuen skal vises */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu); // forbered systemets standardmenuer
		return true;
	}


	/** Kaldes hvis brugeren vælger noget i menuen */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		logTextView.append("\nonOptionsItemSelected(" + item.getTitle());
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
