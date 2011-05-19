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
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

/**
 *
 * @author Jacob Nordfalk
 */
public class Tegneprogram extends Activity {

  EditText tegneflade;
  ArrayList<Point> berøringspunkter = new ArrayList<Point>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
     final Paint tekstStregtype = new Paint();
     tekstStregtype.setColor(Color.GREEN);
     tekstStregtype.setTextSize(24);


     tegneflade = new EditText(this) { // anonym nedarving af View
      @Override
      protected void onDraw(Canvas c) {
         super.onDraw(c);
         c.drawText("Tryk og træk over skærmen", 0, 20, tekstStregtype);

         for (Point p : berøringspunkter) c.drawCircle(p.x, p.y, 3, tekstStregtype);
      }
    };
     tegneflade.setText("hej");

    tegneflade.setOnTouchListener(new OnTouchListener() { // anonym implementering af OnTouchListener
      public boolean onTouch(View view, MotionEvent berøring) {
        System.out.println(berøring);
        Point punktet = new Point((int)berøring.getX(), (int)berøring.getY());
        berøringspunkter.add(punktet);
        //System.out.println(berøringspunkter);
        tegneflade.invalidate();
        return true;
      }
    });

     setContentView(tegneflade);
  }
}
