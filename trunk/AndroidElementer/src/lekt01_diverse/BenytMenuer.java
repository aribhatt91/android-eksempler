package lekt01_diverse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import lekt02_aktiviteter.Indstillinger_akt;
import lekt03_net.ByvejrAktivitet;

public class BenytMenuer extends Activity {
  private TextView logTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    logTextView = new TextView(this);
    logTextView.setText("Dette eksempel viser hvordan menuer virker i Android 2, og hvordan ActionBar fungerer i Android 4\n");
    logTextView.append("Tryk på menu-knappen (F2 i emulatoren)\n");
    setContentView(logTextView);
  }

  /**
   * Kaldes én gang for generelt at forberede menuen
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu); // tilføj systemets standardmenuer
    logTextView.append("\nonCreateOptionsMenu " + menu);
    menu.add(Menu.NONE, 101, Menu.NONE, "javabog.dk");
    menu.add(Menu.NONE, 102, Menu.NONE, "Vejrudsigt").setIcon(android.R.drawable.ic_menu_compass);
    menu.add(Menu.NONE, 103, Menu.NONE, "Indstillinger").setIcon(android.R.drawable.ic_menu_preferences);
    menu.add(Menu.NONE, 104, Menu.NONE, "Afslut").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    // ofte vil man lægge menupunkterne ud i en XML-fil og pakke den ud således
    // getMenuInflater().inflate(R.menu.benytmenuer, menu);
    return true;
  }

  /**
   * Kaldes hver gang menuen skal vises
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    logTextView.append("\nonPrepareOptionsMenu " + menu);
    super.onPrepareOptionsMenu(menu); // forbered systemets standardmenuer
    return true;
  }

  /**
   * Kaldes hvis brugeren vælger noget i menuen
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    logTextView.append("\nonOptionsItemSelected(" + item.getTitle());
    if (item.getItemId() == 101) {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://javabog.dk"));
      startActivity(intent);
    } else if (item.getItemId() == 102) {
      Intent i = new Intent(this, ByvejrAktivitet.class);
      startActivity(i);
    } else if (item.getItemId() == 103) {
      Intent i = new Intent(this, Indstillinger_akt.class);
      startActivity(i);
    } else if (item.getItemId() == 104) {
      finish();
    } else {
      // Ikke håndteret - send kaldet videre til standardhåntering
      return super.onOptionsItemSelected(item);
    }
    return true;
  }

  /**
   * Kaldes når der trykkes på en fysisk knap (incl MENU)
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    logTextView.append("\nonKeyDown " + keyCode);
    return super.onKeyDown(keyCode, event);
  }
}
