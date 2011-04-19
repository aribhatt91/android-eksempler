package eks.recievers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

class InstallationsReciever extends BroadcastReceiver {

  @Override
  public void onReceive(Context ctx, Intent i) {
    System.out.println("onReceive"+ctx+":\n"+i);
    Toast.makeText(ctx, "onReceive"+ctx+":\n"+i, Toast.LENGTH_LONG).show();
  }
}

/**
 * 
 * @author Jacob Nordfalk
 */
public class BenytReciever extends Activity implements OnClickListener {

  Button registrer, afregistrer;
  static InstallationsReciever reciever=new InstallationsReciever();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl=new TableLayout(this);
    TextView tv=new TextView(this);
    tv.setText("Broadcastreciever der opdager når der bliver installeret/afinstalleret apps");
    tl.addView(tv);

    registrer=new Button(this);
    registrer.setText("Registrer reciever");
    tl.addView(registrer);

    afregistrer=new Button(this);
    afregistrer.setText("Afregistrer reciever");
    tl.addView(afregistrer);

    registrer.setOnClickListener(this);
    afregistrer.setOnClickListener(this);

    setContentView(tl);
  }

  /**
   * @return filteret der opdager at pakker er blevet fjernet/tilføjet/opgraderet
   */
  public IntentFilter lavIntentFilter() {
    IntentFilter i=new IntentFilter();
    i.addCategory("android.intent.category.DEFAULT");
    i.addAction("android.intent.action.PACKAGE_CHANGED");
    i.addAction("android.intent.action.PACKAGE_REMOVED");
    i.addAction("android.intent.action.PACKAGE_ADDED");
    i.addAction("android.intent.action.PACKAGE_INSTALL");
    i.addAction("android.intent.action.PACKAGE_REPLACED");
    i.addDataScheme("package");
    return i;
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå==registrer) {
      registerReceiver(reciever, lavIntentFilter());
      Toast.makeText(this, "Installer eller fjern nu en app", Toast.LENGTH_LONG).show();
    } else if (hvadBlevDerKlikketPå==afregistrer) {
      unregisterReceiver(reciever);
      Toast.makeText(this, "Afregistreret", Toast.LENGTH_LONG).show();
    }
  }
}
