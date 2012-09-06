package begynder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

public class BenytMenuer1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);
		tv.setText("Tryk på menu-knappen (eller F2 i emulatoren)\n");
		setContentView(tv);
	}

	/** Kaldes én gang for at forberede menuen */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 101, Menu.NONE, "javabog.dk");
		menu.add(Menu.NONE, 102, Menu.NONE, "Vejrudsigt").setIcon(android.R.drawable.ic_menu_compass);
		menu.add(Menu.NONE, 103, Menu.NONE, "Afslut").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/** Kaldes hvis brugeren vælger noget i menuen */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 101) {
			WebView webView = new WebView(this);
			webView.loadUrl("http://javabog.dk");
			setContentView(webView);
		} else if (item.getItemId() == 102) {
			WebView webView = new WebView(this);
			webView.loadUrl("http://www.dmi.dk/dmi/index/danmark.htm");
			setContentView(webView);
		} else if (item.getItemId() == 103) {
			finish(); // Lukker aktiviteten
		} else {
			System.out.println("Ikke håndteret");
		}
		return true;
	}
}
