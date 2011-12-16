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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Indlæs res/drawable/bil.png
		final Drawable enBil = getResources().getDrawable(R.drawable.bil);

		final Paint tekststreg = new Paint(); // bør egtl. gøres udenfor onDraw
		tekststreg.setColor(Color.GREEN);
		tekststreg.setTextSize(24);
		tekststreg.setStyle(Paint.Style.FILL);

		final Path cirkel = new Path();
		cirkel.addCircle(150, 150, 100, Direction.CW);

		final Paint cirkelstreg = new Paint();
		cirkelstreg.setStyle(Paint.Style.STROKE);
		cirkelstreg.setColor(Color.LTGRAY);
		cirkelstreg.setStrokeWidth(5);

		final long t0 = System.currentTimeMillis();

		View minGrafik = new View(this) { // anonym nedarving af View

			@Override
			protected void onDraw(Canvas c) {
				int t = (int) (System.currentTimeMillis() - t0) / 10; // millisekunder sekunder siden start
				int x = t * 20 / 1000; // går fra 0 til 200
				int y = t * 40 / 1000; // går fra 0 til 400
				System.out.println(t + " x=" + x + " y=" + y);

				c.drawPath(cirkel, cirkelstreg);
				c.drawTextOnPath("Hvornår er en Tuborg bedst?", cirkel, x, y - 100, tekststreg);

				c.rotate(t * 0.05f, x, y);  // rotér om (x,y)
				enBil.setBounds(x, y, x + 50, y + 50 + (int) (10 * Math.sin(t * Math.PI / 1000)));
				enBil.draw(c);
				c.drawText("t=" + t, x, y - 20, tekststreg);
				if (t < 10000) {
					this.postInvalidateDelayed(10); // tegn igen om 1/100 sekund
				} else {
					finish(); // afslut aktiviteten hvis der er gået 10 sekunder
				}
			}
		};
		setContentView(minGrafik);
	}
}
