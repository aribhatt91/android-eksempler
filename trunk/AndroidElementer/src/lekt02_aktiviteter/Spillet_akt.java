package lekt02_aktiviteter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 *
 * @author Jacob Nordfalk
 */
public class Spillet_akt extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView tv = new TextView(this);
		tv.setText("Velkommen til mit fantastiske spil.\nDet er bare ikke helt færdigt endnu.");
		setContentView(tv);
	}
}
