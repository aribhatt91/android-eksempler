package eks.diverse;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.InputType;
import android.text.util.Linkify;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Jacob Nordfalk
 */
public class BenytIntents extends Activity implements OnClickListener {

  EditText meddelelse, nummer;
  Button ringOp, ringOpDirekte, sendSms, sendEpost, websøgning;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl=new TableLayout(this);

    meddelelse=new EditText(this);
    meddelelse.setText("Skriv tekst her");
    tl.addView(meddelelse);

    nummer=new EditText(this);
    nummer.setHint("evt telefonnummer");
    nummer.setInputType(InputType.TYPE_CLASS_PHONE);
    tl.addView(nummer);

    ringOp=new Button(this);
    ringOp.setText("Ring op");
    tl.addView(ringOp);

    ringOpDirekte=new Button(this);
    ringOpDirekte.setText("Ring op - direkte");
    tl.addView(ringOpDirekte);

    sendSms=new Button(this);
    sendSms.setText("Åbn send SMS");
    tl.addView(sendSms);

    sendEpost=new Button(this);
    sendEpost.setText("Åbn og send epostbesked");
    tl.addView(sendEpost);

    websøgning=new Button(this);
    websøgning.setText("Websøgning");
    tl.addView(websøgning);

    TextView tv=new TextView(this);
    tv.setText("Man kan også bruge klassen Linkify til at putte lænker ind i tekst.\n"
        +"Mit telefonnummer er 26206512, min e-post er jacob.nordfalk@gmail.com og jeg har en hjemmeside på http://javabog.dk.");
    Linkify.addLinks(tv, Linkify.ALL);
    tl.addView(tv);

    ringOp.setOnClickListener(this);
    ringOpDirekte.setOnClickListener(this);
    sendSms.setOnClickListener(this);
    sendEpost.setOnClickListener(this);
    websøgning.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    if (hvadBlevDerKlikketPå==ringOp) {
      ringOp(nummer.getText().toString());
    } else if (hvadBlevDerKlikketPå==ringOpDirekte) {
      ringOpDirekte(nummer.getText().toString());
    } else if (hvadBlevDerKlikketPå==sendSms) {
      åbnSendSms(nummer.getText().toString(), meddelelse.getText().toString()+lavModelinfo());
    } else if (hvadBlevDerKlikketPå==sendEpost) {
      åbnSendEmail(nummer.getText().toString(), meddelelse.getText().toString(), lavModelinfo());
    } else if (hvadBlevDerKlikketPå==websøgning) {
      websøgning(meddelelse.getText().toString());
    }
  }

  private void ringOp(String nummer) {
    Uri number=Uri.parse("tel:"+nummer);
    Intent dial=new Intent(Intent.ACTION_DIAL, number);
    startActivity(dial);
  }

  /**
   * Kræver  <uses-permission android:name="android.permission.CALL_PHONE" />
   * @param nummer 
   */
  private void ringOpDirekte(String nummer) {
    try {
      Uri number=Uri.parse("tel:"+nummer);
      Intent dial=new Intent(Intent.ACTION_CALL, number);
      startActivity(dial);
    } catch (Exception e) {
      Toast.makeText(this, "Du mangler <uses-permission android:name=\"android.permission.CALL_PHONE\" /> i manifestet\n"+e, Toast.LENGTH_LONG).show();
    }
  }

  /** Åbner et SMS-vindue og lader brugeren sende SMS'en */
  public void åbnSendSms(String nummer, String besked) {
    // Kilde: http://andmobidev.blogspot.com/2010/01/launching-smsmessages-activity-using.html
    Intent intent=new Intent(Intent.ACTION_VIEW);
    //Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+number));
    intent.setType("vnd.android-dir/mms-sms");
    intent.putExtra("sms_body", besked);
    intent.putExtra("address", nummer);
    startActivity(intent);
  }

  public void websøgning(String søgestreng) {
    Intent searchGivenText=new Intent(Intent.ACTION_WEB_SEARCH);
    searchGivenText.putExtra(SearchManager.QUERY, søgestreng);
    startActivity(searchGivenText);
  }

  public void åbnUrl(String url) {
    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(intent);
  }

  void åbnSendEmail(String modtager, String emne, String txt) {
    Intent emailIntent=new Intent(android.content.Intent.ACTION_SEND);
    emailIntent.setType("plain/text");
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {modtager});
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, emne);
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, txt);
    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
  }

  public String lavModelinfo() {
    String version=null;
    try {
      PackageInfo pi=getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
      version=pi.versionName;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "\nProgram: "+getPackageName()+" version "+version
        +"\nTelefonmodel: "+Build.MODEL
        +"\n"+Build.PRODUCT
        +"\nAndroid v"+Build.VERSION.RELEASE
        +"\nsdk: r"+Build.VERSION.SDK // SDK_INT kommer først i Androd 1.6
        +"\nAndroid ID: "+Secure.getString(getContentResolver(), Secure.ANDROID_ID);
  }
}
