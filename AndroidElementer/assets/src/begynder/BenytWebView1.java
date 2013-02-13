package begynder;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class BenytWebView1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);
		webView.loadUrl("http://javabog.dk");
		setContentView(webView);
	}
}
