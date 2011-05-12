package eks.simpla;

import org.simpla.SimplaView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.TextView;
import org.simpla.SimplaActivity;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Eks5_vejrudsigt_flytbar extends SimplaActivity {

  Bitmap vejrUdsigt;
  float x, y;
  float xFingerSidst, yFingerSidst;

  public class MinGrafik extends SimplaView {

    @Override
    protected void onDraw(Canvas canvas) {
      Paint stregtype=new Paint();
      canvas.drawBitmap(vejrUdsigt, x, y, stregtype);
      canvas.drawText("Dagens vejrudsigt:", 10, 5, stregtype);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
      x = x + event.getX();
      y = y + event.getY();
      postInvalidate();
      return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
      System.out.println(event);
      System.out.println(event.getAction());

      if (event.getAction() == MotionEvent.ACTION_MOVE) {
        // husk startpunkt
        float dx = event.getX() - xFingerSidst;
        float dy = event.getY() - yFingerSidst;
        x = x + dx;
        y = y + dy;
        postInvalidate();
      }
      xFingerSidst = event.getX();
      yFingerSidst = event.getY();
      return true;
    }

  }

  public void simplaMain() {

    TextView intro=simpla.createTextView("Vent, indlæser vejrudsigt...");
    intro.setTextSize(36);
    simpla.setContentView(intro);

    vejrUdsigt=simpla.createBitmapFromUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by=2500&mode=long");

    if (vejrUdsigt==null) {
      simpla.println(intro, "\n\nBeklager, den ku ikke hentes");
    } else {
      // Opret grafik-view og vis det
      MinGrafik minGrafik=new MinGrafik();
      simpla.setContentView(minGrafik);
    }

  }
}
