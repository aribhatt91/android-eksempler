package eks.diverse;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class BenytWebView2 extends Activity {

	public class MinKlasse {

		public void visToast(String tekst) {
			Toast.makeText(BenytWebView2.this, tekst, Toast.LENGTH_LONG).show();
		}

		public void afslut() {
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);

		webView.getSettings().setJavaScriptEnabled(true);

		MinKlasse mitObjekt = new MinKlasse();
		mitObjekt.visToast("Dette er en toast fra Java");

		webView.addJavascriptInterface(mitObjekt, "mitObjekt");
		webView.addJavascriptInterface(this, "aktiviteten");

		//webView.loadUrl("http://javabog.dk");
		webView.loadUrl("file:///android_asset/benytwebview.html");
		//webView.loadUrl("javascript:alert('Hej')");
    
		setContentView(webView);
	}
}
