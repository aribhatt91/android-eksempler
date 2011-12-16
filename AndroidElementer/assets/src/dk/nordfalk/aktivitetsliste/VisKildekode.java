package dk.nordfalk.aktivitetsliste;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisKildekode extends Activity {

	String filnavn;
	private static int onCreateTæller = 0;
	public final static String KILDEKODE_FILNAVN = "filen der skal vises";
	final static String LOKAL_PRÆFIX = "file:///android_asset/";
	static String HS_PRÆFIX = "http://code.google.com/p/android-eksempler/source/browse/trunk/AndroidElementer/";

	static void findWebUrl(Context ctx) {
		try {
			Bundle metaData = ctx.getPackageManager().
					getActivityInfo(new ComponentName(ctx, VisKildekode.class), PackageManager.GET_META_DATA).metaData;
			HS_PRÆFIX = metaData.getString("web_url");
		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(ctx, "Kunne ikke læse web_url fra manifestet. Eksterne henvisninger er muligvis forkerte", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findWebUrl(this);

		TextView tv = new TextView(this);
		ScrollView sv = new ScrollView(this);
		sv.addView(tv);
		setContentView(sv);

		tv.setId(117);
		sv.setId(118);

		Intent kaldtMedIntent = getIntent();
		if (kaldtMedIntent.getExtras() != null) {
			filnavn = kaldtMedIntent.getExtras().getString(KILDEKODE_FILNAVN);
		}

		if (filnavn == null) {
			tv.setText("Manglede ekstradata med filen der skal vises.\n"
					+ "Du kan lave et 'langt tryk' på aktivitetslisten for at vise kildekoden til en aktivitet");
		} else {
			try {
				InputStream is = getAssets().open(filnavn);
				byte b[] = new byte[is.available()]; // kun små filer
				is.read(b);
				is.close();
				String str = new String(b, "UTF-8");

				tv.setText(str);


				TransformFilter filter = new TransformFilter() {

					public final String transformUrl(final Matcher match, String url) {
						String klassenavn = match.group(1);
						return "http://developer.android.com/reference/" + klassenavn.replace('.', '/') + ".html";
					}
				};
				Linkify.addLinks(tv, Pattern.compile("import (android.*?);"), null, null, filter);
				Linkify.addLinks(tv, Pattern.compile("import (java.*?);"), null, null, filter);


				filter = new TransformFilter() {

					public final String transformUrl(final Matcher match, String url) {
						// http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/res/res/layout/simple_list_item_1.xml"
						// http://android.git.kernel.org/?p=platform/frameworks/base.git;a=blob;f=core/res/res/" + match.group(1).replace('.', '/') + ".xml";
						// https://github.com/android/platform_frameworks_base/blob/master/core/res/res/

						return "https://github.com/android/platform_frameworks_base/blob/master/core/res/res/" + match.group(1).replace('.', '/') + ".xml";
					}
				};
				Linkify.addLinks(tv, Pattern.compile("android.R.(layout.[a-z0-9_]+)"), null, null, filter);
				Linkify.addLinks(tv, Pattern.compile("android.R.(xml.[a-z0-9_]+)"), null, null, filter);
				Linkify.addLinks(tv, Pattern.compile("android.R.(raw.[a-z0-9_]+)"), null, null, filter);
				Linkify.addLinks(tv, Pattern.compile("android.R.(drawable.[a-z0-9_]+)"), null, null, filter);
				Linkify.addLinks(tv, Pattern.compile("android.R.(anim.[a-z0-9_]+)"), null, null, filter);


				filter = new TransformFilter() {

					public final String transformUrl(final Matcher match, String url) {
						// LOKAL_PRÆFIX dur ikke her da vi starter et webbrowserintent
						return HS_PRÆFIX + "res/" + match.group(1).replace('.', '/') + ".xml";
					}
				};
				Linkify.addLinks(tv, Pattern.compile("R.(layout.[a-z0-9_]+)"), null, null, filter);
				Linkify.addLinks(tv, Pattern.compile("R.(xml.[a-z0-9_]+)"), null, null, filter);
				Linkify.addLinks(tv, Pattern.compile("R.(anim.[a-z0-9_]+)"), null, null, filter);

				//
				filter = new TransformFilter() {

					public final String transformUrl(final Matcher match, String url) {
						// LOKAL_PRÆFIX dur ikke her da vi starter et webbrowserintent
						return HS_PRÆFIX + "src/" + match.group(1).replace('.', '/') + ".java";
					}
				};
				Linkify.addLinks(tv, Pattern.compile("import ([a-zA-Z0-9_\\.]+)"), null, null, filter);
				//



				// Almindelig HTTP
				filter = new TransformFilter() {

					public final String transformUrl(final Matcher match, String url) {
						return match.group(1);
					}
				};
				Linkify.addLinks(tv, Pattern.compile("(http[a-zA-Z0-9_/:\\.\\?\\&\\=\\-]+)"), null, null, filter);

			} catch (FileNotFoundException ex) {
				Toast.makeText(this, "Filen mangler i assets/src.\nViser " + filnavn + " fra nettet i stedet.", Toast.LENGTH_LONG).show();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(HS_PRÆFIX + filnavn)));
				finish(); // Afslut denne aktivitet
			} catch (IOException ex) {
				Logger.getLogger(VisKildekode.class.getName()).log(Level.SEVERE, null, ex);
				tv.setText("Kunne ikke åbne " + filnavn + ":\n" + ex);
			}
		}


		if (onCreateTæller++ == 2) {
			Toast.makeText(this, "Tryk MENU for andre visninger", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 100, 0, "Vis i WebView");
		menu.add(0, 102, 0, "Vælg fil");
		menu.add(0, 103, 0, "Ekstern browser");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 100) {
			startActivity(new Intent(this, VisKildekodeIWebView.class).putExtra(KILDEKODE_FILNAVN, filnavn));
		} else if (item.getItemId() == 102) {
			vælgFil();
		} else if (item.getItemId() == 103) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(HS_PRÆFIX + filnavn)));
		}
		return true;
	}

	private void vælgFil() {
		try {
			final String sti = filnavn.substring(0, filnavn.lastIndexOf("/"));;
			final String[] filer = getAssets().list(sti);
			new AlertDialog.Builder(this).setTitle("Filer i " + sti).setItems(filer, new Dialog.OnClickListener() {

				public void onClick(DialogInterface arg0, int hvilken) {
					filnavn = sti + "/" + filer[hvilken];
					Toast.makeText(VisKildekode.this, "Viser " + filnavn, Toast.LENGTH_SHORT).show();
					startActivity(new Intent(VisKildekode.this, VisKildekode.class).putExtra(KILDEKODE_FILNAVN, filnavn));
				}
			}).show();
		} catch (IOException ex) {
			Logger.getLogger(VisKildekode.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
