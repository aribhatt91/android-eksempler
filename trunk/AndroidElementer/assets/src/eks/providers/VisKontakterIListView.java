package eks.providers;

import android.widget.SimpleCursorAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.widget.ListView;
import dk.nordfalk.android.elementer.R;

/**
 *
 * @author Jacob Nordfalk
 */
public class VisKontakterIListView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] kolonnner = {Contacts._ID, Contacts.DISPLAY_NAME, Email.DATA};

		Cursor cursor = getContentResolver().query(Email.CONTENT_URI, kolonnner,
				Contacts.IN_VISIBLE_GROUP + " = '1'", null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		startManagingCursor(cursor); // Lad cursoren følge aktivitetens livscyklus

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listeelement, cursor,
				// Disse kolonner i cursoren...
				new String[]{Contacts.DISPLAY_NAME, Email.DATA},
				// ... skal afbildes over i disse views i res/layout/listeelement.xml
				new int[]{R.id.listeelem_overskrift, R.id.listeelem_beskrivelse});

		ListView listView = new ListView(this);
		listView.setAdapter(adapter);

		setContentView(listView);
	}
}
