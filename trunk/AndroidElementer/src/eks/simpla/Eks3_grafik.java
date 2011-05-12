package eks.simpla;

import org.simpla.SimplaView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import org.simpla.R;
import org.simpla.SimplaActivity;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Eks3_grafik extends SimplaActivity {

  int x;

  public class MinGrafik extends SimplaView {

    @Override
    protected void onDraw(Canvas canvas) {

      Path cirkel=new Path();
      cirkel.addCircle(150, 150, 100, Direction.CW);

      Paint cirkelStregtype=new Paint(Paint.ANTI_ALIAS_FLAG);
      cirkelStregtype.setStyle(Paint.Style.STROKE);
      cirkelStregtype.setColor(Color.LTGRAY);
      cirkelStregtype.setStrokeWidth(3);

      canvas.drawPath(cirkel, cirkelStregtype);

      Paint tekstStregtype=new Paint();
      tekstStregtype.setStyle(Paint.Style.FILL_AND_STROKE);
      tekstStregtype.setColor(Color.GREEN);
      tekstStregtype.setTextSize(24);

      canvas.drawTextOnPath(x+"Hvornår er en Tuborg bedst?", cirkel, 0+x, 20+x, tekstStregtype);
    }
  }

  public void simplaMain() {

    // Opret grafik-view
    MinGrafik minGrafik=new MinGrafik();

    // Brug bilen som baggrundsgrafik
    Drawable enBil=getResources().getDrawable(R.drawable.bil);
    minGrafik.setBackgroundDrawable(enBil);

    simpla.setContentView(minGrafik);

    for (int n=0; n<100; n++) {
      x=n*2;
      minGrafik.postInvalidate(); // Får Android til at kalde onDraw() på viewet
      simpla.sleep(100); // vent 0.1 sekund
    }
  }
}
