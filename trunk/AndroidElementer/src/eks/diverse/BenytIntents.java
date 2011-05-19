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

  EditText tekstfelt, nummerfelt;
  Button ringOp, ringOpDirekte, sendSms, sendEpost, websøgning;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl=new TableLayout(this);

    tekstfelt=new EditText(this);
    tekstfelt.setText("Skriv tekst her");
    tl.addView(tekstfelt);

    nummerfelt=new EditText(this);
    nummerfelt.setHint("evt telefonnummer her");
    nummerfelt.setInputType(InputType.TYPE_CLASS_PHONE);
    tl.addView(nummerfelt);

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
    tv.setText("Man kan også bruge klassen Linkify til at putte intents ind i tekst.\n"
        +"Mit telefonnummer er 26206512 (jeg fik det i år 2002), min e-post er jacob.nordfalk@gmail.com og "
            +"jeg har en hjemmeside på http://javabog.dk.");
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
    String nummer = nummerfelt.getText().toString();
    String tekst = tekstfelt.getText().toString();
    if (hvadBlevDerKlikketPå==ringOp) ringOp(nummer);
    else if (hvadBlevDerKlikketPå==ringOpDirekte) ringOpDirekte(nummer);
    else if (hvadBlevDerKlikketPå==sendSms) åbnSendSms(nummer, tekst +lavTelefoninfo());
    else if (hvadBlevDerKlikketPå==sendEpost) åbnSendEpost(tekst, nummer, lavTelefoninfo());
    else if (hvadBlevDerKlikketPå==websøgning) websøgning(tekst);
  }

  private void ringOp(String nummer) {
    Uri nummerUri = Uri.parse("tel:"+nummer);
    Intent dialIntent = new Intent(Intent.ACTION_DIAL, nummerUri);
    startActivity(dialIntent);

    // eller blot:
    //     startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+nummer)));

  }

  /**
   * Kræver  <uses-permission android:name="android.permission.CALL_PHONE" />
   * @param nummerfelt
   */
  private void ringOpDirekte(String nummer) {
    try {
      Uri nummerUri = Uri.parse("tel:"+nummer);
      Intent callIntent = new Intent(Intent.ACTION_CALL, nummerUri);
      startActivity(callIntent);
    } catch (Exception e) {
      Toast.makeText(this, "Du mangler <uses-permission android:name=\"android.permission.CALL_PHONE\" /> i manifestet\n"+e, Toast.LENGTH_LONG).show();
    }
  }

  /** Åbner et SMS-vindue og lader brugeren sende SMS'en */
  public void åbnSendSms(String nummer, String besked) {
    // Kilde: http://andmobidev.blogspot.com/2010/01/launching-smsmessages-activity-using.html
    //Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+number));
    Intent intent=new Intent(Intent.ACTION_VIEW);
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

  void åbnSendEpost(String modtager, String emne, String txt) {
    Intent postIntent=new Intent(android.content.Intent.ACTION_SEND);
    postIntent.setType("plain/text");
    postIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {modtager});
    postIntent.putExtra(Intent.EXTRA_CC, new String[] {"jacob.nordfalk@gmail.com"});
    postIntent.putExtra(Intent.EXTRA_SUBJECT, emne);
    postIntent.putExtra(Intent.EXTRA_TEXT, txt);
    startActivity(Intent.createChooser(postIntent, "Send mail..."));
  }

  public String lavTelefoninfo() {
    String version="(ukendt)";
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
