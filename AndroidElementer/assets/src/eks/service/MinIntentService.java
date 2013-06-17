/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import eks.livscyklus.MinApplicationSingleton;

/**
 *
 * @author j
 */
public class MinIntentService extends IntentService {
  
  static BenytIntentService aktivitetDerSkalOpdateres;
  static boolean annulleret;

  public MinIntentService() {
    super("MinIntentService");
  }

  /** Håndtag til forgrundstråden */
  public Handler forgrundstråd = new Handler();


  @Override
  protected void onHandleIntent(Intent intent) {
    annulleret = false;
    Log.d(getClass().getSimpleName(), "onHandleIntent( "+intent);

		for (int i = 0; i < 100; i++) {
      if (annulleret) return;
      SystemClock.sleep(100);
      final int progress = i;
      forgrundstråd.post(new Runnable() {
        public void run() {
          if (aktivitetDerSkalOpdateres == null) return;  // Nødvendigt tjek
          aktivitetDerSkalOpdateres.progressBar.setProgress(progress);
          aktivitetDerSkalOpdateres.knap.setText("progress = "+progress);
        }
      });
		}

    forgrundstråd.post(new Runnable() {
      public void run() {
        if (aktivitetDerSkalOpdateres == null) return;  // Nødvendigt tjek
        aktivitetDerSkalOpdateres.knap.setText("færdig");
      }
    });
    Log.d(getClass().getSimpleName(), "onHandleIntent() færdig ");
  }
}
