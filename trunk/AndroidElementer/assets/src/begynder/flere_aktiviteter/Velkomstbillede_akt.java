package begynder.flere_aktiviteter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import dk.nordfalk.android.elementer.R;

public class Velkomstbillede_akt extends Activity implements Runnable {
  Handler handler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ImageView iv = new ImageView(this);
    iv.setImageResource(R.drawable.logo);
    setContentView(iv);

    handler.postDelayed(this, 3000); // Kalder run() om 3 sekunder
  }

  public void run() {
    startActivity(new Intent(this, Hovedmenu_akt.class));
    finish(); // Luk velkomsaktiviteten
  }
}
