package eks.ufaerdigt;

import android.view.GestureDetector.OnGestureListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * @author Jacob Nordfalk
 */
public class BenytScaleGestureDetector extends Activity {

  ScaleGestureDetector detector;
  TextView tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    detector = new ScaleGestureDetector(this, new GestusLytter());
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


  class GestusLytter implements OnScaleGestureListener {

    public boolean onScale(ScaleGestureDetector sgd) {
      log("onScale()\n"+sgd);
      return false;
    }

    public boolean onScaleBegin(ScaleGestureDetector sgd) {
      log("onScaleBegin()\n"+sgd);
      return false;
    }

    public void onScaleEnd(ScaleGestureDetector sgd) {
      log("onScaleEnd()\n"+sgd);
    }

  }
}
