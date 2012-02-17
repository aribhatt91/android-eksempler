package begynder1.knapper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;

/**
 *
 * @author Jacob Nordfalk
 */
public class FlereKnapper extends Activity implements OnClickListener {
	// Vi erklærer variabler herude så de huskes fra metode til metode
	Button enKnap, enAndenKnap, enTredjeKnap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Programmatisk layout
		TableLayout tl = new TableLayout(this);

		enKnap = new Button(this);
		enKnap.setText("enKnap");
		tl.addView(enKnap);

		enAndenKnap = new Button(this);
		enAndenKnap.setText("enAndenKnap");
		tl.addView(enAndenKnap);

		enTredjeKnap = new Button(this);
		enTredjeKnap.setText("enTredjeKnap");
		tl.addView(enTredjeKnap);

		setContentView(tl);
		// Havde vi brugt deklarativt layout i XML havde vi i stedet her skrevet
		//setContentView(R.layout.mit_layout);
		//enKnap = (Button) findViewById(R.id.enKnap);
		//enAndenKnap = (Button) findViewById(R.id.enAndenKnap);
		//enTredjeKnap = (Button) findViewById(R.id.enTredjeKnap);

		enKnap.setOnClickListener(this);
		enAndenKnap.setOnClickListener(this);
		enTredjeKnap.setOnClickListener(this);
	}

	public void onClick(View v) {
		System.out.println("Der blev trykket på en knap");

		// Vis et tal der skifter så vi kan se hver gang der trykkes
		long etTal = System.currentTimeMillis();

		if (v == enKnap) {

			enKnap.setText("Du trykkede på mig. Tak! \n"+etTal);

		} else if (v == enAndenKnap) {

			enTredjeKnap.setText("Nej nej, tryk på mig i stedet!\n"+etTal);

		} else if (v == enTredjeKnap) {

			enAndenKnap.setText("Hey, hvis der skal trykkes, så er det på MIG!\n"+etTal);

		}

	}
}
