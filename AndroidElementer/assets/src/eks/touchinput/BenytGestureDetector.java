package eks.touchinput;

import android.view.GestureDetector.OnGestureListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;


/**
 *
 * @author Jacob Nordfalk
 */
public class BenytGestureDetector extends Activity {

  GestureDetector detector;
  TextView tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    detector = new GestureDetector(this, new GestusLytter());
    tv = new TextView(this);
    //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    //tv.setTextSize(16);
    tv.setText("Lav nogle gestusser");
    setContentView(tv);
  }

  /**
   * Send hændelser videre til detektoren
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return detector.onTouchEvent(event);
  }

  public void log(String tekst) {
    tv.setText(tekst);
    Log.d("Gestus", tekst);
  }


  class GestusLytter implements OnGestureListener {

    public boolean onDown(MotionEvent pkt) {
      log("onDown()\n"+pkt);
      return false;
    }

    public void onShowPress(MotionEvent pkt) {
      log("onShowPress()\n"+pkt);
    }

    public boolean onSingleTapUp(MotionEvent pkt) {
      log("onSingleTapUp()\n"+pkt);
      return false;
    }

    /** Bemærk at dx og dy er ændringer i forhold til sidste punkt, ikke i forh til startPkt */
    public boolean onScroll(MotionEvent startPkt, MotionEvent nuvPkt, float dx, float dy) {
      log("onScroll()\n"+startPkt+" -> \n"+nuvPkt+" d="+dx+","+dy);
      return false;
    }

    public void onLongPress(MotionEvent pkt) {
      log("onLongPress()\n"+pkt);
    }

    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

    public boolean onFling(MotionEvent startPkt, MotionEvent slutPkt, float vx, float vy) {
      float dx = slutPkt.getX() - startPkt.getX();
      float dy = slutPkt.getY() - startPkt.getY();
      log("onFling()\n"+startPkt+" -> \n"+slutPkt+" v="+vx+","+vy+"\nd="+dx+","+dy);

      // Eksempel på detektering af swipe højre/venstre hen over skærmen
      // Krav: dy < dx/4 (ikke for skråt) og dx>skærmbredde/3  (ikke for kort)
      if (Math.abs(dy) < Math.abs(dx)/4 && Math.abs(dx)>display.getWidth()/3) {
        if (dx<0) log("swipe venstre");
        else log("swipe højre");
        return true; // hændelse håndteret
      }

      // Krav: dx < dy/4 (ikke for skråt) og dy>skærmhøjde/3  (ikke for kort)
      if (Math.abs(dx) < Math.abs(dy)/4 && Math.abs(dy)>display.getHeight()/3) {
        if (dy<0) log("swipe op");
        else log("swipe ned");
        return true; // hændelse håndteret
      }

      return false; // hændelse ikke håndteret
    }
  }
}
