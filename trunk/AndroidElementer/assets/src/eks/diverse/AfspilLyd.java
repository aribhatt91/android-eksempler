package eks.diverse;

import android.app.Activity;
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
public class AfspilLyd extends Activity implements OnClickListener {

	Button enKnap;
	MediaPlayer enLyd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enKnap = new Button(this);
		enKnap.setText("Spil en lyd");
		enKnap.setOnClickListener(this);
		setContentView(enKnap);

		// Volumen op/ned skal styre lydstyrken af medieafspilleren, uanset som noget spilles lige nu eller ej
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		enLyd = MediaPlayer.create(this, R.raw.jeg_bremser_haardt);
		enLyd.setVolume(1, 1);
	}

	public void onClick(View arg0) {
		enLyd.start();
	}
}
