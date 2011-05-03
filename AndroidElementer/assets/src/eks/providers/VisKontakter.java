/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.providers;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 *
 * @author Jacob Nordfalk
 */
public class VisKontakter extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView=new TextView(this);
    textView.append("Herunder kommmer kontakters navn og info fra telefonbogen\n");
    ScrollView scrollView=new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);

    ContentResolver cr=getContentResolver();

    Uri uri=ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    String[] kolonnner = {
      ContactsContract.Contacts._ID,
      ContactsContract.Contacts.DISPLAY_NAME,
      ContactsContract.CommonDataKinds.Email.DATA,
    };
    String where = Contacts.IN_VISIBLE_GROUP + " = '1'";
    String orderBy=ContactsContract.Contacts.DISPLAY_NAME+" COLLATE LOCALIZED ASC";
    Cursor cursor=cr.query(uri, kolonnner, where, null, orderBy);

    while (cursor.moveToNext()) {
      String id=cursor.getString(0);
      String navn=cursor.getString(1);
      String epost=cursor.getString(2);
      String tlf=cursor.getString(2);
      textView.append(navn+"  "+epost+" "+tlf+"\n");
    }
    cursor.close();

  }
}
