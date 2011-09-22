package eks.ufaerdigt;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;
import eks.livscyklus.Programdata;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class VisKontakterMedBillede extends Activity
{
	private ListView listView;
	private ArrayList<String> numre;
/*

  public static void log(Object txt) {
    Log.d("Log", ""+txt);
  }



  public static void tjekKontakter() {

    if (kontakter==null || kontakter_skal_opdateres) {
      kontakter = new LinkedHashMap<String, Kontakt>();
      kontakter_skal_opdateres = false;

      Uri uri=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

      String[] kolonner={
          ContactsContract.Contacts._ID,
          ContactsContract.Contacts.DISPLAY_NAME,
          ContactsContract.CommonDataKinds.Phone.NUMBER,
//          Contacts.PHOTO_ID
      };
      String where = Contacts.IN_VISIBLE_GROUP + " = '1'";
      String orderBy = ContactsContract.Contacts.DISPLAY_NAME+" COLLATE LOCALIZED ASC";
      Cursor cursor = cr.query(uri, kolonner, where, null, orderBy);

      while (cursor.moveToNext()) {
        Kontakt k = new Kontakt();
        k.id=cursor.getLong(0);
        k.navn=cursor.getString(1);
        k.ikkeNormaliseretTelefonNr=cursor.getString(2);
        k.telefonNr=normaliserTelefonnr(k.ikkeNormaliseretTelefonNr);

//        Kontakt.log(cursor.getString(3));


        kontakter.put(k.telefonNr, k);
      }
      cursor.close();
      Set<String> ks = kontakter.keySet();
      if (!ks.equals(alleKontakter)) {
        alleKontakter = new ArrayList<String>(kontakter.keySet());
        handler.postDelayed(registerKontakterIgen, 100);
        forsinkelse = 5000;
      }
    }


  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);



    Uri uri=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    ContentObserver minContentObserver = new ContentObserver(new Handler()) {
      @Override
      public void onChange (boolean selfChange) {
        log("KONTAKTBOG ÆNDRET");
        kontakter_skal_opdateres = true;
      }
    };
    cr.registerContentObserver(uri, true, minContentObserver);


    ArrayAdapter adapter = new ArrayAdapter<String>(this,
      R.layout.listeelement, R.id.listeelem_overskrift, numre) {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view=super.getView(position, convertView, parent);
        TextView listeelem_overskrift=(TextView) view.findViewById(R.id.listeelem_overskrift);
        TextView listeelem_beskrivelse=(TextView) view.findViewById(R.id.listeelem_beskrivelse);
        ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);

        Kontakt k = Programdata.kontakter.get(numre.get(position));

        listeelem_overskrift.setText(k.navn);
        listeelem_beskrivelse.setText(k.telefonNr);
        Bitmap billede = hentBillede(cr);
        if (billede != null) listeelem_billede.setImageBitmap(billede);
        else listeelem_billede.setImageResource(android.R.drawable.ic_menu_gallery);
        return view;
      }
    };

  }

  Bitmap hentBillede(ContentResolver cr) {
    if (!billedeForsøgtHentet) {
      billedeForsøgtHentet = true;
      billedeUri = null;
      billedeRef = null;

      Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(telefonNr));
      Cursor contact = cr.query(phoneUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

      if (contact.moveToFirst()) {
        long userId = contact.getLong(contact.getColumnIndex(ContactsContract.Contacts._ID));
        billedeUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);
      }
    }

    Bitmap b = billedeRef==null?null:billedeRef.get();
    if (b==null && billedeUri == null && billedeForsøgtHentet) {
      b = hentUkendtVenIkon();
    } else if (b == null) {  // Billedet er blevet garbage colleted eller aldrig indlæst
      InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, billedeUri);
      RFAktivitet.log("Billede "+telefonNr+" ("+billedeUri+") er: "+input);
      if (input == null) {
        billedeUri = null;
        return null; // intet billede, så billedeRef=null for fremtiden
      }
      try {
        Bitmap b2 = BitmapFactory.decodeStream(input);

        b = b2;

        input.close();
      } catch (IOException ex) {
        RFAktivitet.logFejl(ex);
      }
      if (b == null) {
        billedeUri = null;
        return null; // intet billede, så billedeRef=null for fremtiden
      }
      billedeRef = new SoftReference<Bitmap>(b);
    }
    return b;
  }
*/
}
