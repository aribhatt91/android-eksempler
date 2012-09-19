package eks.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 *
 * @author Jacob Nordfalk
 */
public class HoldAppLevende extends Activity implements OnClickListener {

	Button knap1, knap2, knap3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TableLayout tl = new TableLayout(this);
		TextView tv = new TextView(this);
		tv.setText("Når servicen er startet vil systemet prøve at holde programmet "
				+ "i hukommelsen, også når den er ikke er i forgrunden (dvs ikke slå "
				+ "processen i hjel).\n"
				+ "Hvis det alligevel ryger ud vil systemet forsøge at genstarte processen "
				+ "og reinstantiere servicen igen hurtigst muligt.\n"
				+ "Nederste knap kalder System.exit() for at simulere at processen afsluttes, "
				+ "hvorefter systemet straks vil genstarte processen og reinstantiere servicen.");
		tl.addView(tv);

		knap1 = new Button(this);
		knap1.setText("Start service");
		tl.addView(knap1);

		knap2 = new Button(this);
		knap2.setText("Stop service");
		tl.addView(knap2);

		knap3 = new Button(this);
		knap3.setText("Stop proces");
		tl.addView(knap3);

		setContentView(tl);

		knap1.setOnClickListener(this);
		knap2.setOnClickListener(this);
		knap3.setOnClickListener(this);
	}

	public void onClick(View hvadBlevDerKlikketPå) {

		if (hvadBlevDerKlikketPå == knap1) {
			Intent i = new Intent(this, HoldAppLevendeService.class);
			i.putExtra("nogle ekstra data", "med en værdi");
      startService(i);
		} else if (hvadBlevDerKlikketPå == knap2) {
      stopService(new Intent(this, HoldAppLevendeService.class));
		} else if (hvadBlevDerKlikketPå == knap3) {
			System.exit(0);
		}
	}
}
