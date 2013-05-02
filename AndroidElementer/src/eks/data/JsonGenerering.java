package eks.data;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;
import java.io.InputStream;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jacob Nordfalk
 */
public class JsonGenerering extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);

		try {

			JSONObject json = new JSONObject(); // { }
      json.put("bank", "Merkur");  // {  "bank": "Merkur" }
      JSONArray kunder = new JSONArray();
      json.put("kunder", kunder);  // {  "bank": "Merkur", "kunder": [] }
      JSONObject k = new JSONObject();
      k.put("navn", "Jacob");
      k.put("kredit", 1000);
      kunder.put(k); // {  "bank": "Merkur", "kunder": [  { "navn": "Jacob", "kredit": 1000 }] }
      kunder.put(new JSONObject("{ \"navn\": \"Søren\", \"kredit\": 1007, \"alder\": 29 }"));

			tv.append("\n\nHele JSONobjektet på en linje " + kunder.toString());
			tv.append("\n\nHele JSONobjektet på flere linjer " + kunder.toString(2));

		} catch (Exception ex) {
			ex.printStackTrace();
			tv.append("FEJL:" + ex.toString());
		}

		setContentView(tv);
	}
}
