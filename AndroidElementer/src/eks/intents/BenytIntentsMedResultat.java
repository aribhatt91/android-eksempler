package eks.intents;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedResultat extends Activity implements OnClickListener {

  Button vælgKontakt, vælgBillede, tagBillede, dokumentation;
  private int SELECT_IMAGE = 10007;
  private int RECQUEST_CAMERA_IMAGE = 10009;
  private int PICK_CONTACT = 10005;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    vælgKontakt = new Button(this);
    vælgKontakt.setText("Vælg kontakt");
    tl.addView(vælgKontakt);

    vælgBillede = new Button(this);
    vælgBillede.setText("Vælg billede fra galleri");
    tl.addView(vælgBillede);

    tagBillede = new Button(this);
    tagBillede.setText("Tag billede med kameraet");
    tl.addView(tagBillede);

    dokumentation = new Button(this);
    dokumentation.setText("Dokumentation om intents");
    tl.addView(dokumentation);

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
        Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intentContact, PICK_CONTACT);

      } else if (hvadBlevDerKlikketPå == vælgBillede) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_IMAGE);

      } else if (hvadBlevDerKlikketPå == tagBillede) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, RECQUEST_CAMERA_IMAGE);

      } else {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/content/Intent.html")));
      }
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, "Du mangler Googles udvidelser på denne telefon:\n"+e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    System.out.println(requestCode+" gav resultat "+resultCode+ " med data="+data);
    if (resultCode == Activity.RESULT_OK) try {

  		if (requestCode == RECQUEST_CAMERA_IMAGE) {

					AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
					FileInputStream in = videoAsset.createInputStream();
          Bitmap yourSelectedImage = BitmapFactory.decodeStream(in);
          // xxx kode mangler
			}

    } catch (IOException e) {
      e.printStackTrace();
    }

	}
}
