/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class BenytMenuer extends Activity {

	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tv = new TextView(this);
		tv.setText("Tryk på menu-knappen\n");
		setContentView(tv);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		tv.append("\nonKeyDown " + keyCode);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		tv.append("\nonCreateOptionsMenu " + menu);
		menu.add(Menu.NONE, 101, Menu.NONE, "Start vejrudsigt");
		menu.add(Menu.NONE, 102, Menu.NONE, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		tv.append("\nonOptionsItemSelected(" + item.getTitle());
		if (item.getItemId() == 101) {
			Intent i = new Intent(this, ByvejrAktivitet.class);
			startActivity(i);
		} else if (item.getItemId() == 102) {
			Intent i = new Intent(this, Indstillinger.class);
			startActivity(i);
		}
		return true;
	}
}
