package eks.simpla;

import org.simpla.SimplaView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import org.simpla.R;
import org.simpla.SimplaActivity;

/**
 * 
 * @author Jacob Nordfalk
 */

class GrafikView extends SimplaView {

  Drawable enBil;
  String navnet = "Jacob";

  @Override
  protected void onDraw(Canvas c) {

     enBil.setBounds(0, 0, 50, 50);
     enBil.draw(c);

     Paint tekstStregtype = new Paint();
     tekstStregtype.setColor(Color.GREEN);
     tekstStregtype.setTextSize(24);

     c.drawText(navnet, 10, 20, tekstStregtype);

  }
}

public class Eks2_grafik extends SimplaActivity {
	public void simplaMain() {
    // Opret grafik-view
    GrafikView minGrafik = new GrafikView();

    // Indlæs res/drawable/car.png
    minGrafik.enBil = getResources().getDrawable(R.drawable.bil);

    minGrafik.setBackgroundColor(Color.DKGRAY);
		simpla.setContentView(minGrafik);

    simpla.sleep(5000); // vent 5 sekunder

    minGrafik.navnet = simpla.showEditTextDialog("Hvad hedder du?", "(skriv dit navn her)");

    minGrafik.postInvalidate(); // Får Android til at kalde onDraw() på viewet

    simpla.sleep(5000); // vent 5 sekunder

    minGrafik.navnet = "SLUT!";
    minGrafik.postInvalidate(); // Lad Android gentegne viewet med den nye tekst
    simpla.sleep(1000);
	}
}
