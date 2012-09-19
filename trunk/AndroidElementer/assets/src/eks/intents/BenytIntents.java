package eks.intents;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.InputType;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 *
 * @author Jacob Nordfalk
 */
public class BenytIntents extends Activity implements OnClickListener {
	EditText tekstfelt, nummerfelt;
	Button åbnOpkald, ringOpDirekte, sendSms, delVia, sendEpost, websøgning;


	private void åbnOpkald(String nummer) {
		Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + nummer));
		startActivity(dialIntent);
    // eller blot:
		//     startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+nummer)));
	}


	/**
	 * Bemærk: Kræver <uses-permission android:name="android.permission.CALL_PHONE" /> i manifestet
	 */
	private void ringOpDirekte(String nummer) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nummer));
			startActivity(callIntent);
		} catch (Exception e) {
			Toast.makeText(this, "Du mangler <uses-permission android:name=\"android.permission.CALL_PHONE\" /> i manifestet\n" + e, Toast.LENGTH_LONG).show();
		}
	}

	/** Åbner et SMS-vindue og lader brugeren sende SMS'en */
	public void åbnSendSms(String nummer, String besked) {
		// Kilde: http://andmobidev.blogspot.com/2010/01/launching-smsmessages-activity-using.html
		//Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+number));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setType("vnd.android-dir/mms-sms");
		intent.putExtra("sms_body", besked);
		intent.putExtra("address", nummer);
		startActivity(intent);
	}


	public void websøgning(String søgestreng) {
		Intent søgeIntent = new Intent(Intent.ACTION_WEB_SEARCH);
		søgeIntent.putExtra(SearchManager.QUERY, søgestreng);
		startActivity(søgeIntent);
	}


	public void åbnBrowser(String adresse) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adresse));
		startActivity(intent);
	}



	void åbnDelVia() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_SUBJECT, "Prøv AndroidElementer");
		i.putExtra(Intent.EXTRA_TEXT,
			"Hej!\n\n"+
			"Hvis du programmerer til Android så prøv denne her eksempelsamling\n"+
			"AndroidElementer\n"+
			"https://market.android.com/details?id=dk.nordfalk.android.elementer"
		);
		i.setType("text/plain");
		startActivity(Intent.createChooser(i, "Del via"));
	}


	void åbnSendEpost(String modtager, String emne, String txt) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_SUBJECT, emne);
		i.putExtra(Intent.EXTRA_TEXT, txt);
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{modtager});
		i.putExtra(Intent.EXTRA_CC, new String[]{"jacob.nordfalk@gmail.com"});
		i.setType("text/plain");
		startActivity(Intent.createChooser(i, "Send e-post..."));
	}



	/** Ofte har man som udvikler brug for info om den telefon brugeren har.
	    Denne metode giver telefonmodel, Androidversion og programversion etc. */
	public String lavTelefoninfo() {
		String version = "(ukendt)";
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
			version = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "\nProgram: " + getPackageName() + " version " + version
				+ "\nTelefonmodel: " + Build.MODEL
				+ "\n" + Build.PRODUCT
				+ "\nAndroid v" + Build.VERSION.RELEASE
				+ "\nsdk: r" + Build.VERSION.SDK // SDK_INT kommer først i Androd 1.6
				+ "\nAndroid ID: " + Secure.getString(getContentResolver(), Secure.ANDROID_ID);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TableLayout tl = new TableLayout(this);

		tekstfelt = new EditText(this);
		tekstfelt.setText("Skriv tekst her");
		tl.addView(tekstfelt);

		nummerfelt = new EditText(this);
		nummerfelt.setHint("evt telefonnummer her");
		nummerfelt.setInputType(InputType.TYPE_CLASS_PHONE);
		tl.addView(nummerfelt);

		åbnOpkald = new Button(this);
		åbnOpkald.setText("Ring op");
		tl.addView(åbnOpkald);

		ringOpDirekte = new Button(this);
		ringOpDirekte.setText("Ring op - direkte");
		tl.addView(ringOpDirekte);

		sendSms = new Button(this);
		sendSms.setText("Åbn send SMS");
		tl.addView(sendSms);

		sendEpost = new Button(this);
		sendEpost.setText("Åbn og send epostbesked");
		tl.addView(sendEpost);

		delVia = new Button(this);
		delVia.setText("Del app...");
		tl.addView(delVia);

		websøgning = new Button(this);
		websøgning.setText("Websøgning");
		tl.addView(websøgning);

		TextView tv = new TextView(this);
		tv.setText("Man kan også bruge klassen Linkify til at putte intents ind i tekst.\n"
				+ "Mit telefonnummer er 26206512, min e-post er jacob.nordfalk@gmail.com og "
				+ "jeg har en hjemmeside på http://javabog.dk.");
		Linkify.addLinks(tv, Linkify.ALL);
		tl.addView(tv);

		åbnOpkald.setOnClickListener(this);
		ringOpDirekte.setOnClickListener(this);
		sendSms.setOnClickListener(this);
		delVia.setOnClickListener(this);
		sendEpost.setOnClickListener(this);
		websøgning.setOnClickListener(this);

		ScrollView sv = new ScrollView(this);
		sv.addView(tl);
		setContentView(sv);
	}


	public void onClick(View v) {
		String nummer = nummerfelt.getText().toString();
		String tekst = tekstfelt.getText().toString();
		if (v == åbnOpkald) {
			åbnOpkald(nummer);
		} else if (v == ringOpDirekte) {
			ringOpDirekte(nummer);
		} else if (v == sendSms) {
			åbnSendSms(nummer, tekst + lavTelefoninfo());
		} else if (v == sendEpost) {
			åbnSendEpost(tekst, nummer, lavTelefoninfo());
		} else if (v == delVia) {
			åbnDelVia();
		} else if (v == websøgning) {
			websøgning(tekst);
		}
	}
}
