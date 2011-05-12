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
public class Eks2_grafik_animeret extends SimplaActivity {

  Drawable enBil;
  String navnet = "Jacob";
  int x = 0;
  int y = 0;
  
  public class MinGrafik extends SimplaView {

    @Override
    protected void onDraw(Canvas canvas) {

       enBil.setBounds(x, y, x+50, y+50+(int) (10*Math.sin(x*Math.PI/5)));
       enBil.draw(canvas);

       Paint tekstStregtype = new Paint();
       tekstStregtype.setColor(Color.GREEN);
       tekstStregtype.setTextSize(24);

       canvas.rotate(x*.15f);
       canvas.drawText(navnet, x, 20+y, tekstStregtype);

    }
  }

	public void simplaMain() {

    // Indlæs res/drawable/car.png
    enBil = getResources().getDrawable(R.drawable.bil);

    // Opret grafik-view
    MinGrafik minGrafik = new MinGrafik();
    minGrafik.setBackgroundColor(Color.DKGRAY);
		simpla.setContentView(minGrafik);

    simpla.sleep(500); // vent 0.5 sekund

    navnet = simpla.showEditTextDialog("Hvad hedder du?", "(skriv dit navn her)");

    int n=0;
    while (n<100) {
      x = n*2;
      y = n*3;
      minGrafik.postInvalidate(); // Får Android til at kalde onDraw() på viewet
      simpla.sleep(100); // vent 0.1 sekund
      n = n + 1;
    }

    navnet = "SLUT!";
    minGrafik.postInvalidate(); // Lad Android gentegne viewet med den nye tekst
    simpla.sleep(1000);
    
	}
}
