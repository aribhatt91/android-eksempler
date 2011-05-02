package eks.diverse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisKildekode extends Activity {

  public final static String KILDEKODE_FILNAVN="filen der skal vises";
  private final static String LOKAL_PRÆFIX="file:///android_asset/src/";
  private final static String HS_PRÆFIX="http://code.google.com/p/android-eksempler/source/browse/trunk/AndroidElementer/src/";
  private static int onCreateTæller = 0;

  WebView webView;
  String filnavn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    webView=new WebView(this);

    Intent kaldtMedIntent=getIntent();

    filnavn=kaldtMedIntent.getExtras().getString(KILDEKODE_FILNAVN);

    if (filnavn!=null) {
      webView.loadUrl(LOKAL_PRÆFIX+filnavn);
      webView.getSettings().setDefaultTextEncodingName("UTF-8");
      webView.getSettings().setBuiltInZoomControls(true);
      webView.setInitialScale(75);
    } else {
      webView.loadData("Manglede ekstradata med filen der skal vises.\n"
          +"Du kan lave et 'langt tryk' på aktivitetslisten for at vise kildekoden til en aktivitet", "text/plain", "UTF-8");
      webView.setInitialScale(100);
    }

    setContentView(webView);
    
    if (onCreateTæller++ == 0) Toast.makeText(this, "Tryk MENU for andre visninger", Toast.LENGTH_LONG).show();
    
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)  {
    menu.add(0,100,0,"Vis på nettet");
    menu.add(0,101,0,"Vis lokalt");
    menu.add(0,102,0,"Vælg fil");
    menu.add(0,103,0,"Ekstern browser");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId()==100) webView.loadUrl(HS_PRÆFIX+filnavn);
    else if (item.getItemId()==101) webView.loadUrl(LOKAL_PRÆFIX+filnavn);
    else if (item.getItemId()==102) vælgFil();
    else if (item.getItemId()==103) startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(HS_PRÆFIX+filnavn)));
    return true;
  }

  private void vælgFil() {
    try {
      final String sti = filnavn.substring(0, filnavn.lastIndexOf("/"));;
      final String[] filer = getAssets().list("src/"+ sti);
          new AlertDialog.Builder(this)
          .setTitle("Filer i "+sti)
          .setItems(filer, new Dialog.OnClickListener() {
            public void onClick(DialogInterface arg0, int hvilken) {
              filnavn = sti + "/" + filer[hvilken];
              Toast.makeText(VisKildekode.this, "Viser "+filnavn, Toast.LENGTH_SHORT).show();
              webView.loadUrl(LOKAL_PRÆFIX+filnavn);
            }
          })
          .show();
    } catch (IOException ex) {
      Logger.getLogger(VisKildekode.class.getName()).log(Level.SEVERE, null, ex);
    }
    
  }
}
