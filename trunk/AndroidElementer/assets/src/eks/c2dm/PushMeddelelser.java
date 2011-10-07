package eks.c2dm;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Se http://code.google.com/intl/eo/android/c2dm/  og
 * http://blog.boxedice.com/2010/10/07/android-push-notifications-tutorial/ for generel info
 *
 * @author Jacob Nordfalk
 */
public class PushMeddelelser extends Activity implements OnClickListener {

/*
Instrukser på Linux/Mac:
0) Tilmeldt en Google-konto på http://code.google.com/intl/eo/android/c2dm/signup.html

1) Log ind på din Google-konto og få Auth-token.
read kode; curl https://www.google.com/accounts/ClientLogin -d Email=jacob.nordfalk@gmail.com -d "Passwd=$kode" -d accountType=GOOGLE -d source=AndroidElementer -d service=ac2dm

2) Sæt Auth
Auth=DQAAAMQAAABvZp3SKaAp6tDka6srXE9BARK4Ny7FEXfbPGv6kA9lYn6cuS6e1MJi-Fj1Aeit_hfbmAMTs7ogk5T2hQgQwvmtmFz-rdZB2VbW_ZDspaUszThAnb6bUMLwHEVoS1IbRIora_ake3anbNgrjiP_9_m1iH4rt8dRlMCPRVhOnP-ZegLs7_RUiME80WysaSMe2z04mamVtbND-pVzXzgYfw48Vl7psXnPtV8D7H-0EMpPc356zsQGVA-5fGxLcZhC0l8_Jar2MT9pvPejsGW11t5j

3) Start app-en og tryk 'registrér'. Led i logcat efter registration_id ser skal sendes til server
registration_id=APA91bEiTL01cThaAtkqJ9xM8WZARuzX4cD7GJ2NDK7ofSarh5veCfPMTGLWhR3uchuOxlI0_eYi7f3MNLPA6a9OgXN4Z-Gpcl31-i9nojdVQj--jkS1MO8u9EgtZeHC474dZF8RzmTC5Tskkaf5H7eiV6eubEXcpEDo_QW7ehbTfsdlMPbpjc8

4) Fra server skal du nu sende Auth + registration_id + nøgle-værdipar
curl --header "Authorization: GoogleLogin auth=$Auth" "https://android.apis.google.com/c2dm/send" -d registration_id=$registration_id -d "data.hilsen=HEJ FRA SKYEN" -d collapse_key=en_hilsen


*/
  private Button registrér, afregistrér;

  static TextView logTv; // Farligt! Husk at nulstille i onDestroy() !!

  static void log(final String txt) {
    Log.i("PushMeddelelser", txt);
    if (logTv != null) {
      logTv.append("\n"+txt);
      Toast.makeText(logTv.getContext(), txt.trim(), Toast.LENGTH_LONG).show();
    }
  }


  static void enMeddelelseErKommet(Bundle data) {
    log("\nMeddelse: "+data.getString("hilsen"));
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl=new TableLayout(this);

    registrér=new Button(this);
    registrér.setText("registrér");
    tl.addView(registrér);

    afregistrér=new Button(this);
    afregistrér.setText("afregistrér");
    tl.addView(afregistrér);

    logTv=new TextView(this);
    logTv.setText("Et eksempel på Android Cloud to Device Messaging\nTryk først på registrér og følg instrukserne i loggen");
    tl.addView(logTv);

    registrér.setOnClickListener(this);
    afregistrér.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);

    // Parse evt meddelser der er kommet med intent fordi aktiviteten ikke var startet da meddelsen kom
    Bundle dataFraReciever = getIntent().getExtras();
    if (dataFraReciever != null) enMeddelelseErKommet(dataFraReciever);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå==registrér) {
      Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
      registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // boilerplate
      registrationIntent.putExtra("sender", "jacob.nordfalk@gmail.com");
      startService(registrationIntent);
      logTv.append("Registreret");
    }
    else /* if (hvadBlevDerKlikketPå==afregistrér) */ {
      Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
      unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
      startService(unregIntent);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    logTv = null;
  }
}
