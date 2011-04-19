package eks.diverse;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.util.ArrayList;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Tegneprogram extends Activity {

  View tegneflade;
  ArrayList<Point> berøringspunkter = new ArrayList<Point>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
     final Paint tekstStregtype = new Paint();
     tekstStregtype.setColor(Color.GREEN);
     tekstStregtype.setTextSize(24);

    tegneflade = new View(this) { // anonym nedarving af View
      @Override
      protected void onDraw(Canvas c) {
         super.onDraw(c);
         c.drawText("Tryk og træk over skærmen", 0, 20, tekstStregtype);

         for (Point p : berøringspunkter) c.drawCircle(p.x, p.y, 3, tekstStregtype);
      }
    };

    tegneflade.setOnTouchListener(new OnTouchListener() { // anonym implementering af OnTouchListener
      public boolean onTouch(View view, MotionEvent hændelse) {
        System.out.println(hændelse);
        //System.out.println(hændelse.getX() +"," +  hændelse.getY());
        berøringspunkter.add(new Point((int)hændelse.getX(), (int)hændelse.getY()));
        //System.out.println(berøringspunkter);
        tegneflade.invalidate();
        return true;
      }
    });

    setContentView(tegneflade);
  }
}
