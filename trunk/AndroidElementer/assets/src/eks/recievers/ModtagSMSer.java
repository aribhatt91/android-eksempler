package eks.recievers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;



class SMSReciever extends BroadcastReceiver {

  @Override
  public void onReceive(Context ctx, Intent intent) {
    Toast.makeText(ctx, intent.toURI(), Toast.LENGTH_LONG).show();
    System.out.println("intent.toURI() = "+intent.toURI());
    Bundle data=intent.getExtras();
    if (data!=null) {
      Object pdus[]=((Object[]) data.get("pdus"));
      for (Object pdu : pdus) {
        SmsMessage part=SmsMessage.createFromPdu((byte[]) pdu);
        Toast.makeText(ctx, "SMS fra "+part.getDisplayOriginatingAddress(), Toast.LENGTH_SHORT).show();
        Toast.makeText(ctx, part.getDisplayMessageBody(), Toast.LENGTH_LONG).show();
      }
    }
  }
}

/**
 * 
 * @author Jacob Nordfalk
 */
public class ModtagSMSer extends Activity implements OnClickListener {

  Button registrer, afregistrer;
  static SMSReciever reciever=new SMSReciever();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl=new TableLayout(this);
    TextView tv=new TextView(this);
    tv.setText("Broadcastreciever der opdager når der modages SMSer");
    tl.addView(tv);

    registrer=new Button(this);
    registrer.setText("Registrer reciever");
    tl.addView(registrer);

    afregistrer=new Button(this);
    afregistrer.setText("Afregistrer reciever");
    tl.addView(afregistrer);

    setContentView(tl);

    registrer.setOnClickListener(this);
    afregistrer.setOnClickListener(this);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå==registrer) {
      IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
      registerReceiver(reciever, filter);
      Toast.makeText(this, "Send nu en SMS til telefonen", Toast.LENGTH_LONG).show();
    } else if (hvadBlevDerKlikketPå==afregistrer) {
      unregisterReceiver(reciever);
      Toast.makeText(this, "Afregistreret", Toast.LENGTH_LONG).show();
    }
  }
}
