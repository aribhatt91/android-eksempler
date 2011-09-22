/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.ufaerdigt;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;
import dk.nordfalk.android.elementer.R;

/**
 * Klasse sørger for animationer frem og tilbage og lignende rare fællesmetoder
 * @author j
 */
public class AnimeredeOvergange extends Activity {

  static Application applikation; // Du kan regne med at den altid er initialiseret
  static int n = 0;
  static boolean UDVIKLING = false;

  public static void animationFrem(Activity a) {
    n++;
    switch (n) {
      case 1:
        a.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        break;
      case 2:
        a.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        break;
      case 3:
        a.overridePendingTransition(R.anim.custom_anim, 0);
        break;
      case 4:
        a.overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
        break;
      case 5:
        a.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        break;
      case 6:
        a.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        break;
      case 7:
        a.overridePendingTransition(R.anim.custom_anim, R.anim.push_up_out);
        break;
      case 8:
        a.overridePendingTransition(R.anim.push_up_in, R.anim.push_left_out);
        break;
      default:
        //Toast.makeText(a, RFAktivitet.class.getSimpleName() + ": Starter forfra", Toast.LENGTH_SHORT).show();
        n = 0;
    }
  }

  //static int xxx = 0;
  private static OnTouchListener farvKnapNårDenErTrykketNed = new OnTouchListener() {
      public boolean onTouch(View view, MotionEvent me) {
        ImageButton ib = (ImageButton) view;
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
          //log("farve "+me);
          //log(++xxx);
          ib.setColorFilter(0xFFA0A0A0, PorterDuff.Mode.MULTIPLY);
        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
        } else {
          //log("ikke farve "+me);
          ib.setColorFilter(null);
        }
        return false;
      }
    };

  public static ImageButton farvTrykketNed(View view) {
    if (view==null) throw new IllegalStateException("view er null!"); // Se [1]
    ImageButton ib = (ImageButton) view;
    ib.setOnTouchListener(farvKnapNårDenErTrykketNed);
    return ib;
  }


  public static void animationTilbage(Activity a) {
    animationFrem(a);
    //a.overridePendingTransition(R.anim.custom_anim, R.anim.custom_anim);
  }

  @Override
  public void finish() {
    super.finish();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    AnimeredeOvergange.animationTilbage(this);
  }

  @Override
  public void startActivityForResult(Intent intent, int requestCode) {
    super.startActivityForResult(intent, requestCode);
    AnimeredeOvergange.animationFrem(this);
  }

  @Override
  public void startActivity(Intent intent) {
    super.startActivity(intent);
    AnimeredeOvergange.animationFrem(this);
  }

}

/*
[1]  XXX Hm! Tjek på Samsung

java.lang.NullPointerException
at dk.nordfalk.roskilde.RFAktivitet.farvTrykketNed(RFAktivitet.java:84)
at dk.nordfalk.roskilde.HovedmenuAkt.onCreate(HovedmenuAkt.java:47)
at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1047)
at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2459)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2512)

GT-I5500 	'2.1-update1 	GT-I5500 	Samsung 	GT-I5500 	ERE27 	Samsung/GT-I5500/GT-I5500/GT-I5500:2.1-update1/ERE27/XWJH4:user/release-keys 	SE-S609 	ERE27 	GT-I5500 	GT-I5500 	test-keys 	1281775608000 	user 	root
*/
