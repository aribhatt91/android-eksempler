package begynder.flere_aktiviteter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import dk.nordfalk.android.elementer.R;

public class Velkomstbillede_akt extends Activity implements Runnable {
  Handler handler = new Handler();
  static Velkomstbillede_akt aktivitetDerVisesNu = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ImageView iv = new ImageView(this);
    iv.setImageResource(R.drawable.logo);
    setContentView(iv);

    // Hvis savedInstanceState ikke er null er det en aktivitet der er ved at blive genstartet
    if (savedInstanceState==null) {
      handler.postDelayed(this, 3000); // <1> Kør run() om 3 sekunder
    }
    aktivitetDerVisesNu = this;
  }

  public void run() {
    startActivity(new Intent(this, Hovedmenu_akt.class));
    aktivitetDerVisesNu.finish();  // <2> Luk velkomsaktiviteten
    aktivitetDerVisesNu = null;
  }
}