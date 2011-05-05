package eks.diverse;

import android.app.Activity;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import dk.nordfalk.android.elementer.R;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Eks_lyd extends Activity implements OnClickListener {

  Button spilKnap;
  MediaPlayer enLyd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    spilKnap = new Button(this);
    spilKnap.setText("Spil en lyd");
    spilKnap.setOnClickListener(this);
    setContentView(spilKnap);

    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    enLyd = MediaPlayer.create(this, R.raw.jeg_bremser_haardt);
    enLyd.setVolume(1, 1);
  }

  public void onClick(View arg0) {
    enLyd.start();
  }

}
