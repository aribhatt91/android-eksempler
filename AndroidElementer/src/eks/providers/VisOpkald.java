/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.providers;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Jacob Nordfalk
 */
public class VisOpkald extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView=new TextView(this);
    textView.append("Herunder kommmer opkald\n");
    ScrollView scrollView=new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);

    ContentResolver cr=getContentResolver();
		String[] kolonner = {Calls.DATE, Calls.TYPE, Calls.NUMBER, Calls.CACHED_NAME, Calls.DURATION};
		String where = Calls.DATE + " >= " + (System.currentTimeMillis()-1000*60*60*24*7); // sidste 7 dage

    textView.append("\nkolonner = "+Arrays.asList(kolonner));
    textView.append("\nwhere = "+where);
    textView.append("\nURI = "+Calls.CONTENT_URI);
    textView.append("\n\n");
    Cursor c=cr.query(Calls.CONTENT_URI, kolonner, where, null, Calls.DATE);
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    while (c.moveToNext()) {
      textView.append(df.format(new Date(c.getLong(0)))+"  "+c.getInt(1)+" "+c.getString(2)+"  "+c.getString(3)+"  "+c.getInt(4)+"  "+"\n");
    }
    c.close();

  }
}
