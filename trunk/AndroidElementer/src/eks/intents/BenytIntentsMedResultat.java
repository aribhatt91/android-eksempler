package eks.intents;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

/**
 * Demonstrerer hvordan man benytter intents til at vælg en
 * kontaktperson, et billede eller tage et billede med kameraet
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedResultat extends Activity implements OnClickListener {

  Button vælgKontakt, vælgFlereKontakter, vælgBillede, tagBillede, dokumentation;
  TextView resultatTextView;
  LinearLayout resultatHolder;
  private int VÆLG_KONTAKT = 1111;
  private int VÆLG_FLERE_KONTAKTER = 1112;
  private int VÆLG_BILLEDE = 2222;
  private int TAG_BILLEDE =  3333;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    vælgKontakt = new Button(this);
    vælgKontakt.setText("Vælg kontakt");
    tl.addView(vælgKontakt);

    vælgFlereKontakter = new Button(this);
    vælgFlereKontakter.setText("Vælg flere kontakter\nVirker KUN PÅ HTC");
    tl.addView(vælgFlereKontakter);

    vælgBillede = new Button(this);
    vælgBillede.setText("Vælg billede fra galleri");
    tl.addView(vælgBillede);

    tagBillede = new Button(this);
    tagBillede.setText("Tag billede med kameraet");
    tl.addView(tagBillede);

    dokumentation = new Button(this);
    dokumentation.setText("Dokumentation om intents");
    tl.addView(dokumentation);

    resultatTextView = new TextView(this);
    tl.addView(resultatTextView);

    resultatHolder = new LinearLayout(this);
    tl.addView(resultatHolder);

    vælgKontakt.setOnClickListener(this);
    vælgBillede.setOnClickListener(this);
    tagBillede.setOnClickListener(this);
    dokumentation.setOnClickListener(this);

    setContentView(tl);
  }

  public void onClick(View hvadBlevDerKlikketPå) {
    try {
      if (hvadBlevDerKlikketPå == vælgKontakt) {
        // Se også http://stackoverflow.com/questions/2507898/how-to-pick-a-image-from-gallery-sd-card-for-my-app-in-android
        //Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, VÆLG_KONTAKT);

      } else if (hvadBlevDerKlikketPå == vælgFlereKontakter) {

        // Dette intent virker kun på HTC-telefoner og bør derfor undgås
        // med mindre man laver noget til en specifik enhed
        // http://stackoverflow.com/questions/3146377/selecting-multiple-contacts-in-android/6450200#6450200
        Intent i = new Intent(Intent.ACTION_GET_CONTENT, Email.CONTENT_URI);
        startActivityForResult(Intent.createChooser(i, ""), VÆLG_FLERE_KONTAKTER);

      } else if (hvadBlevDerKlikketPå == vælgBillede) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, VÆLG_BILLEDE);

      } else if (hvadBlevDerKlikketPå == tagBillede) {
        // Bemærk at jeg måtte have android:configChanges="orientation" for at aktiviteten
        // ikke blev vendt og jeg mistede billedet. I et rigtigt ville jeg forsyne mine views med
        // ID'er så deres indhold overlevede at skærmen skiftede orientering
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Hvis vi vil have billedet gemt
        //File fil = new File(Environment.getExternalStorageDirectory(),"billede.jpg");
        //i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fil));
        startActivityForResult(i, TAG_BILLEDE);

      } else {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/content/Intent.html")));
      }
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, "Du mangler Googles udvidelser på denne telefon:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent resultat) {
    resultatTextView.setText(requestCode + " gav resultat " + resultCode + " og data:\n" + resultat);
    System.out.println(requestCode + " gav resultat " + resultCode + " med data=" + resultat);

    resultatHolder.removeAllViews();

    if (resultCode == Activity.RESULT_OK) {
      try {
        if (requestCode == VÆLG_KONTAKT) {
          resultatTextView.append("\n\nresultat.getData()="+resultat.getData());
          resultatTextView.append("\n\nresultat.getExtras()="+resultat.getExtras());
          Uri contactData = resultat.getData();
          Cursor c = managedQuery(contactData, null, null, null, null);
          if (c.moveToFirst()) {
            resultatTextView.append("\n\nNAVN:"+
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
          }
        } else if (requestCode == VÆLG_FLERE_KONTAKTER) {
          resultatTextView.append("\n\nresultat.getData()="+resultat.getData());
          resultatTextView.append("\n\nresultat.getExtras()="+resultat.getExtras());
          Uri contactData = resultat.getData();
          Cursor c = managedQuery(contactData, null, null, null, null);
          while (c.moveToFirst()) {
            resultatTextView.append("\n\nNAVN:"+
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
          }
        } else if (requestCode == VÆLG_BILLEDE) {
          AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(resultat.getData(), "r");
          Bitmap bmp = BitmapFactory.decodeStream(videoAsset.createInputStream());
          ImageView iv = new ImageView(this);
          iv.setImageBitmap(bmp);
          resultatHolder.addView(iv);
        } else if (requestCode == TAG_BILLEDE) {
          Bitmap bmp = (Bitmap) resultat.getExtras().get("data");
          ImageView iv = new ImageView(this);
          iv.setImageBitmap(bmp);
          resultatHolder.addView(iv);
          iv = new ImageView(this);
          iv.setImageResource(android.R.drawable.btn_star);
          resultatHolder.addView(iv);
        }

      } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
      }
    }
    ImageView iv = new ImageView(this);
    iv.setImageResource(android.R.drawable.ic_dialog_email);
    resultatHolder.addView(iv);

  }
}
