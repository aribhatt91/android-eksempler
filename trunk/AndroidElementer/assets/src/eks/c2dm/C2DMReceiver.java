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
		Bundle data = intent.getExtras();
		if (data != null) {
			data.isEmpty(); // Forårsager extras bliver pakket ud så toString() virker
		}
		MeddelelserFraServer.log(this.getClass().getSimpleName() + " onReceive(" + intent + "\n" + data);

		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			String registration = intent.getStringExtra("registration_id");
			if (intent.getStringExtra("error") != null) {
				MeddelelserFraServer.log("Registrering fejlede, prøv igen senere (er Android Marked installeret?)");
			} else if (intent.getStringExtra("unregistered") != null) {
				MeddelelserFraServer.log("Enheden er afregistreret. Yderligere serverbeskedder bliver afvist");
			} else if (registration != null) {
				// Send the registration ID to the 3rd party site that is sending the messages.
				// This should be done in a separate thread.
				// When done, remember that all registration is done.
				MeddelelserFraServer.log("Enheden er registreret:\nregistration_id=" + registration);
				MeddelelserFraServer.log("Denne ID skal sendes til din server så serveren kan kontakte enheden");
			}
		} else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			if (MeddelelserFraServer.logTv != null) {
				MeddelelserFraServer.enMeddelelseErKommet(data);
			} else {
				// Aktiviteten kører ikke. Start den og send data meddelelsen med i extra
				Intent i = new Intent(ctx, MeddelelserFraServer.class);
				i.putExtras(data);

				// Kode til at starte aktiviteten direkte - ikke så brugervenligt!
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//ctx.startActivity(i);

				// Opret en notifikation i stedet
				PendingIntent pi = PendingIntent.getActivity(ctx, 0, i, 0);
				Notification n = new Notification(R.drawable.bil, "Meddelelse fra skyen", System.currentTimeMillis());
				n.setLatestEventInfo(ctx, "Meddelelse fra skyen", data.getString("hilsen"), pi);

				NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				nm.notify(42, n);
			}
		}

	}
}
