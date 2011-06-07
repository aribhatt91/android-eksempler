package eks.diverse;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import java.util.Locale;

/**
 *
 * @author Jacob Nordfalk
 */
public class TekstTilTale extends Activity implements OnClickListener, TextToSpeech.OnInitListener {

  EditText udtaleTekst;
  Button udtalKnap;
  TextToSpeech tts;
/*

Saluton, mi estas via amiko.
Kion vi volas fari hodiaŭ?

Tio estas pomo. Kion vi volas fari per la pomo?

   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    udtaleTekst = new EditText(this);
    udtaleTekst.setText("Min danske oot tale - med Locale US - eer maiet dorli.");
    udtalKnap = new Button(this);
    udtalKnap.setOnClickListener(this);
    udtalKnap.setText("Vent, indlæser TTS-modul...");
    udtalKnap.setEnabled(false);

    TableLayout ll = new TableLayout(this);
    ll.addView(udtaleTekst);
    ll.addView(udtalKnap);
    setContentView(ll);
    tts = new TextToSpeech(this, this);
    //tts.setLanguage(Locale.US);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    tts.stop();
    tts.shutdown();
  }

  public void onInit(int arg0) {
    if (arg0 == TextToSpeech.SUCCESS) {
      tts.speak("Text til tale er klar.", TextToSpeech.QUEUE_ADD, null);
      udtalKnap.setText("TTS Klar");
      udtalKnap.setEnabled(true);
    } else {
      udtalKnap.setText("Kunne ikke indlæse TTS");
    }
  }

  public void onClick(View arg0) {
    String tekst = udtaleTekst.getText().toString();
    tts.speak(tekst, TextToSpeech.QUEUE_ADD, null);
  }
}
