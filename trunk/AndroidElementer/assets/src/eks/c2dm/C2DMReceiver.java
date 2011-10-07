package eks.c2dm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import dk.nordfalk.android.elementer.R;



public class C2DMReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context ctx, Intent intent) {
    PushMeddelelser.log(this+" onReceive("+ intent);

    if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
      String registration = intent.getStringExtra("registration_id");
      if (intent.getStringExtra("error") != null) {
          // Registration failed, should try again later.
      } else if (intent.getStringExtra("unregistered") != null) {
          // unregistration done, new messages from the authorized sender will be rejected
      } else if (registration != null) {
         // Send the registration ID to the 3rd party site that is sending the messages.
         // This should be done in a separate thread.
         // When done, remember that all registration is done.
        PushMeddelelser.log("Enheden er registreret:\nregistration_id="+registration);
      }
    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
      //   handleMessage(ctx, intent);
        Bundle data = intent.getExtras();
        if (PushMeddelelser.logTv==null) {
          // Start aktiviteten og send data meddelelsen med i extra!
          Intent i = new Intent(ctx, PushMeddelelser.class);
          i.putExtras(data);

          // Kode til at starte aktiviteten direkte - ikke så brugervenligt!
          //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //ctx.startActivity(i);
          PendingIntent pi = PendingIntent.getActivity(ctx, 0, i, 0);
          Notification n = new Notification(R.drawable.bil, "Meddelelse fra skyen", System.currentTimeMillis());
          n.setLatestEventInfo(ctx, "Meddelelse fra skyen", data.getString("hilsen"), pi);

          NotificationManager nm=(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
          nm.notify(42, n);

        } else {
          PushMeddelelser.enMeddelelseErKommet(data);
        }
     }

    }
  }

