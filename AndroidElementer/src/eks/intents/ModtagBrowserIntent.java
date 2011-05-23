package eks.intents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class ModtagBrowserIntent extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent i = getIntent();
    String url = i.getDataString();

    if (url == null) {
      url = "http://javabog.dk";
      Toast.makeText(this, "URL manglede", Toast.LENGTH_LONG).show();
    }

    Toast.makeText(this, "AndroidElementer viser\n"+url, Toast.LENGTH_LONG).show();
    Toast.makeText(this, "Intent var\n"+i, Toast.LENGTH_LONG).show();

    WebView webView=new WebView(this);
    webView.loadUrl(url);

    setContentView(webView);
  }
}
