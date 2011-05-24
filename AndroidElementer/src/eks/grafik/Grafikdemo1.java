package eks.grafik;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import dk.nordfalk.android.elementer.R;

/**
 *
 * @author Jacob Nordfalk
 */
public class Grafikdemo1 extends Activity {

  Drawable enBil;
  String navnet = "Jacob";
  int x = 0;
  int y = 0;
  View minGrafik;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Indlæs res/drawable/bil.png
    enBil = getResources().getDrawable(R.drawable.bil);

    minGrafik = new View(this) { // anonym nedarving af View

      @Override
      protected void onDraw(Canvas canvas) {

        Paint tekstStregtype = new Paint();
        tekstStregtype.setColor(Color.GREEN);
        tekstStregtype.setTextSize(24);
        tekstStregtype.setStyle(Paint.Style.FILL_AND_STROKE);

        Path cirkel = new Path();
        cirkel.addCircle(150, 150, 100, Direction.CW);

        Paint cirkelStregtype = new Paint(Paint.ANTI_ALIAS_FLAG);
        cirkelStregtype.setStyle(Paint.Style.STROKE);
        cirkelStregtype.setColor(Color.LTGRAY);
        cirkelStregtype.setStrokeWidth(3);

        canvas.drawPath(cirkel, cirkelStregtype);
        canvas.drawTextOnPath(x + "Hvornår er en Tuborg bedst?", cirkel, 0 + x, 20 + x, tekstStregtype);

        canvas.rotate(x * .15f);
        enBil.setBounds(x, y, x + 50, y + 50 + (int) (10 * Math.sin(x * Math.PI / 5)));
        enBil.draw(canvas);
        canvas.drawText(navnet, x, 20 + y, tekstStregtype);
      }
    };

    minGrafik.setBackgroundResource(R.drawable.logo);
    setContentView(minGrafik);
    new Thread(runnable).start();
  }

  Runnable runnable = new Runnable() {

    public void run() {
      try {
        Thread.sleep(500); // vent 0.5 sekund
        int n = 0;
        while (n < 100) {
          x = n * 2;
          y = n * 3;
          minGrafik.postInvalidate(); // Får Android til at kalde onDraw() på viewet
          Thread.sleep(100); // vent 0.1 sekund
          n = n + 1;
        }
      } catch (InterruptedException ex) {
      }

      navnet = "SLUT!";
      minGrafik.postInvalidate(); // Lad Android gentegne viewet med den nye tekst
    }
  };
}
