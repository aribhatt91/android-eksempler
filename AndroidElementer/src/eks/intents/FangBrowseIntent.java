package eks.intents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class FangBrowseIntent extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent i = getIntent();
    String url = i.getDataString();

    if (url == null) {
      TextView tv=new TextView(this);
      tv.setText("Dette eksempel viser hvordan man fanger et browserintent.\n"
          +"Gå ind på http://javabog.dk og vælg et kapitel fra grundbogen,"
          +"f.eks http://javabog.dk/OOP/kapitel3.jsp ");
      Linkify.addLinks(tv, Linkify.ALL);
      setContentView(tv);
      return;
    }

    Toast.makeText(this, "AndroidElementer viser\n"+url, Toast.LENGTH_LONG).show();
    Toast.makeText(this, "Intent var\n"+i, Toast.LENGTH_LONG).show();

    WebView webView=new WebView(this);
    webView.loadUrl(url);

    setContentView(webView);
  }
}
