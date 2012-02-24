package eks.touchinput;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.TextView;

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
		tv.setText("Spred eller knib sammen på skærmen");
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
			log("onScale()\n" + sgd.getScaleFactor());
			return false;
		}

		public boolean onScaleBegin(ScaleGestureDetector sgd) {
			log("onScaleBegin()\n" + sgd.getScaleFactor());
			return true;
		}

		public void onScaleEnd(ScaleGestureDetector sgd) {
			log("onScaleEnd()\n" + sgd.getScaleFactor());
		}
	}
}
