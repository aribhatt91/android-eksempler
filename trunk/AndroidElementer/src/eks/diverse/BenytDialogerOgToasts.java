package eks.diverse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;
import dk.nordfalk.android.elementer.R;
import eks.recievers.BenytReciever;

/**
 * 
 * @author Jacob Nordfalk
 */
public class BenytDialogerOgToasts extends Activity implements OnClickListener {

  Button standardToast, toastMedBillede, visAlertDialog, visAlertDialog1, visAlertDialog2, visNoitification;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl=new TableLayout(this);

    standardToast=new Button(this);
    standardToast.setText("standardToast");
    tl.addView(standardToast);

    toastMedBillede=new Button(this);
    toastMedBillede.setText("toastMedBillede");
    tl.addView(toastMedBillede);

    visAlertDialog=new Button(this);
    visAlertDialog.setText("visAlertDialog");
    tl.addView(visAlertDialog);

    visAlertDialog1=new Button(this);
    visAlertDialog1.setText("visAlertDialog med 1 knap");
    tl.addView(visAlertDialog1);

    visAlertDialog2=new Button(this);
    visAlertDialog2.setText("visAlertDialog med 2 knapper");
    tl.addView(visAlertDialog2);

    visNoitification=new Button(this);
    visNoitification.setText("visNoitification");
    tl.addView(visNoitification);

    standardToast.setOnClickListener(this);
    toastMedBillede.setOnClickListener(this);
    visAlertDialog.setOnClickListener(this);
    visAlertDialog1.setOnClickListener(this);
    visAlertDialog2.setOnClickListener(this);
    visNoitification.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå==standardToast) {
      Toast.makeText(this, "Standard-toast", Toast.LENGTH_LONG).show();
    } else if (hvadBlevDerKlikketPå==toastMedBillede) {
      Toast t=new Toast(this);
      ImageView im=new ImageView(this);
      im.setImageResource(R.drawable.logo);
      im.setAlpha(180);
      t.setView(im);
      t.setGravity(Gravity.CENTER, 0, 0);
      t.show();
    } else if (hvadBlevDerKlikketPå==visAlertDialog) {
      AlertDialog.Builder dialog=new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setMessage("Denne her har ingen knapper");
      dialog.show();
    } else if (hvadBlevDerKlikketPå==visAlertDialog1) {
      AlertDialog.Builder dialog=new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      dialog.setIcon(R.drawable.logo);
      dialog.setMessage("Denne her har én knap");
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(BenytDialogerOgToasts.this, "Standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      dialog.show();
    } else if (hvadBlevDerKlikketPå==visAlertDialog2) {
      AlertDialog.Builder dialog=new AlertDialog.Builder(this);
      dialog.setTitle("En AlertDialog");
      EditText et=new EditText(this);
      et.setText("Denne her viser et generelt view og har to knapper");
      dialog.setView(et);
      dialog.setPositiveButton("Vis endnu en toast", new AlertDialog.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
          Toast.makeText(BenytDialogerOgToasts.this, "Standard-toast", Toast.LENGTH_LONG).show();
        }
      });
      dialog.setNegativeButton("Nej tak", null);
      dialog.show();
    } else if (hvadBlevDerKlikketPå==visNoitification) {
      Context ctx=getApplicationContext(); // Undgå this for ikke at lække hukommelse
      Intent tegneIntent = new Intent(ctx, Tegneprogram.class);
      PendingIntent tegneAktivitet = PendingIntent.getActivity(ctx, 0, tegneIntent, 0);
      Notification notification = new Notification(R.drawable.logo, "Tegn!", System.currentTimeMillis());   
      notification.setLatestEventInfo(ctx, "Der skal tegnes!", "Du er nødt til at tegne lidt", tegneAktivitet);

      long[] vibrate = {0,100,300,400, 500, 510, 550, 560, 600, 610, 650, 610, -1};
      notification.vibrate = vibrate;

      NotificationManager notificationManager=(NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(42, notification);     
    }
  }

}
