package eks.kort;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import dk.nordfalk.android.elementer.R;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.InputStream;
/**
 *
 * @author Jacob Nordfalk
 */
public class KortAktivitet extends MapActivity {

	final static float MIKRO = 1.0f / 1000000; // Google maps' koordinater er i mikrograder

	MapView mapView;

	// Overlejrede data
	MyLocationOverlay myLocationOverlay;
	MitItemizedOverlay itemizedOverlay;
	WMSOverlay wMSOverlay;

	// Geopunkter
	GeoPoint valby = new GeoPoint(55654074, 12493775);
	GeoPoint valgtPunkt = valby;

	/** Finder korrekt API nøgle ud fra app'ens signatur. Se
	http://stackoverflow.com/questions/3029819/android-automatically-choose-debug-release-maps-api-key  */
	String findApiNøgle() {
		String md5 = null;
		try {
			for (Signature sig : getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures) {
				MessageDigest m = MessageDigest.getInstance("MD5");
				m.update(sig.toByteArray());
				md5 = new BigInteger(1, m.digest()).toString(16);

				// Foranstil med nuller hvis summen indeholder mindre end 32 tegn
				while (md5.length()<32) md5 = "0"+md5;

				Log.d("findApiNøgle", "md5fingerprint: " + md5);

				// Jacobs debug-nøgle
				if (md5.equals("55d643a6dd14dce9643ffde54491dd33")) {
					return "0osb1BfVdrk1FfefX3nEC0WXr4HeYVNPNojXkXQ";
				}
				// Jacobs officielle nøgle
				if (md5.equals("d9a7385fd19107698149b7576fcb8b29")) {
					return "0osb1BfVdrk3etct3WjSX-gUUayztcGvB51EMwg";
				}

				// indsæt din egen nøgle her:
				if (md5.equals("dit md5 fingerprint")) {
					return "nøglen du får fra Google Maps signup";
				}
			}

			// Ingen nøgle fundet. Vis hjælp til brugeren
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Mangler API-nøgle");

      String tekst = "Før kortet kan vises skal du registrere dig og få en API-nøgle .\n"
					+ "Dit MD5 er: \n\n" + md5
					+ "\n\nDet skal registreres på:\n"
          + "http://code.google.com/android/maps-api-signup.html"
					+ "\nDerefter skal begge dele skrives ind i kildekoden (" + this.getClass().getSimpleName() + ".java).\n"
					+ "Denne meddelelse er også kommet i loggen så du kan gøre det fra din PC.\n"
					+ "MD5-certifikatet er også glevet gemt i udklipsholderen på telefonen.";

			Log.e("findApiNøgle", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			Log.e("findApiNøgle", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			Log.e("findApiNøgle", tekst);  // Skriv teksten tydelig ud med RØDT i loggen
			Log.e("findApiNøgle", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			Log.e("findApiNøgle", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

			ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboardManager.setText(md5);
			EditText et = new EditText(this);
			et.setText(tekst);
			dialog.setView(et);
			dialog.setPositiveButton("Regstér nu)", new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://code.google.com/android/maps-api-signup.html")));
				}
			});
			dialog.setNegativeButton("Senere", null);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ukendt";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mapView = new MapView(this, findApiNøgle());
		mapView.getController().setZoom(7);
		mapView.setClickable(true);
		mapView.setEnabled(true);
		mapView.setBuiltInZoomControls(true); // vis zoomknapper (plus og minus)
		mapView.getController().setCenter(valby); // Centrér omkring Valby


		// Overlejr kortet med brugerens placering
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.runOnFirstFix(new Runnable() {

			public void run() { // Flyt overlejretKort til aktuelt sted når første stedbestemmelse er foretaget
				mapView.getController().animateTo(myLocationOverlay.getMyLocation());
			}
		});
		mapView.getOverlays().add(myLocationOverlay);


		// Vis liste af overlejrede ikoner
		Drawable ikon = getResources().getDrawable(android.R.drawable.star_big_on);
		itemizedOverlay = new MitItemizedOverlay(ikon);
		itemizedOverlay.tilføj(new OverlayItem(new GeoPoint(57607065, 10249187), "Tversted Plantage", "Osterklit"), ikon);
		itemizedOverlay.tilføj(new OverlayItem(new GeoPoint(55756630, 9133909), "Legoland", "Billund"), getResources().getDrawable(android.R.drawable.ic_dialog_email));
		itemizedOverlay.tilføj(new OverlayItem(valby, "Her bor Jacob", "Valby"), getResources().getDrawable(R.drawable.logo));
		itemizedOverlay.tilføj(new OverlayItem(new GeoPoint(54714330, 11664910), "Døllefjælde-Musse", "Naturpark"), null);
		itemizedOverlay.tilføjFærdig();
		mapView.getOverlays().add(itemizedOverlay);


		wMSOverlay = new WMSOverlay();
		mapView.getOverlays().add(wMSOverlay);

		setContentView(mapView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
	}

	@Override
	protected void onStop() {
		super.onStop();
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
	}

	protected boolean isRouteDisplayed() {
		return false;
	} // skal defineres

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 50, 0, "Satellit til/fra");
		menu.add(0, 51, 0, "Trafik til/fra");
		menu.add(0, 52, 0, "Hvor er der\ngadevisn. til/fra");
		menu.add(0, 53, 0, "Rutevejledning");
		menu.add(0, 54, 0, "Start std kortvisning");
		menu.add(0, 55, 0, "Start gadevisning");
		menu.add(0, 56, 0, "Nærmeste adresse");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 50:
				mapView.setSatellite(!mapView.isSatellite());
				break;
			case 51:
				mapView.setTraffic(!mapView.isTraffic());
				break;
			case 52:
				mapView.setStreetView(!mapView.isStreetView());
				break;
			case 53:
				GeoPoint her = myLocationOverlay.getMyLocation();
				if (her == null) {
					her = valby;
				}
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr=" + her.getLatitudeE6() * MIKRO + "," + her.getLongitudeE6() * MIKRO
						+ "&daddr=" + valgtPunkt.getLatitudeE6() * MIKRO + "," + valgtPunkt.getLongitudeE6() * MIKRO)));
				break;
			case 54:
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("geo:" + valgtPunkt.getLatitudeE6() * MIKRO + "," + valgtPunkt.getLongitudeE6() * MIKRO + "?z=" + mapView.getZoomLevel())));
				break;
			case 55: // google.streetview:cbll=55.65407,12.493775&cbp=1
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("google.streetview:cbll=" + valgtPunkt.getLatitudeE6() * MIKRO + "," + valgtPunkt.getLongitudeE6() * MIKRO + "&cbp=1")));
				break;
			case 56:
				Geocoder geocoder = new Geocoder(this);
				try { // forsøg at finde nærmeste adresse - burde ske asynkront
					List<Address> adresser = geocoder.getFromLocation(
							valgtPunkt.getLatitudeE6() * MIKRO, valgtPunkt.getLongitudeE6() * MIKRO, 1);
					if (adresser != null && adresser.size() > 0) {
						Toast.makeText(this, adresser.get(0).toString(), Toast.LENGTH_LONG).show();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
				}
				break;
			default:
				Toast.makeText(this, "Ikke implementeret", Toast.LENGTH_SHORT).show();
		}

		return super.onOptionsItemSelected(item);
	}




	/**
	 * Eksempel på at overlejre informationer (ikoner) på et kort
	 */
	public class MitItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		public ArrayList<OverlayItem> elementer = new ArrayList<OverlayItem>();

		public MitItemizedOverlay(Drawable stdIkon) {
			super(boundCenterBottom(stdIkon));
		}

		public void tilføjFærdig() {
			this.populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return elementer.get(i);
		}

		@Override
		public int size() {
			return elementer.size();
		}

		@Override
		protected boolean onTap(int i) {
			OverlayItem e = elementer.get(i);
			Toast.makeText(KortAktivitet.this, "Klikkede nr " + i + " " + e.getTitle(), Toast.LENGTH_SHORT).show();
			Toast.makeText(KortAktivitet.this, e.getSnippet() + "\n" + e.routableAddress(), Toast.LENGTH_SHORT).show();
			return true;
		}

		void tilføj(OverlayItem overlayItem, Drawable ikon) {
			if (ikon != null) {
				overlayItem.setMarker(boundCenterBottom(ikon));
			}
			elementer.add(overlayItem);
		}
	}



	/**
	 * Eksempel på hvordan man overlejrer et kort (kaldet WMS - Web Map Service)
	 */
	public class WMSOverlay extends Overlay {

		GeoPoint øverstVenstre = new GeoPoint(0, 0);
		GeoPoint nederstHøjre = new GeoPoint(0, 0);
		Paint paint = new Paint();
		Bitmap overlejretKort;
		boolean iGangMedAtHenteNytKort = false;

		public WMSOverlay() {
			paint.setAlpha(128);
			paint.setStyle(Paint.Style.STROKE);
		}

		private Rect geoTilSkærmRekt(GeoPoint øverstVenstre, GeoPoint nederstHøjre) {
			Point p1 = mapView.getProjection().toPixels(øverstVenstre, null);
			Point p2 = mapView.getProjection().toPixels(nederstHøjre, null);
			Rect kortRekt = new Rect(p1.x, p1.y, p2.x, p2.y);
			return kortRekt;
		}

		@Override
		public void draw(Canvas c, MapView mapView, boolean shadow) {
			//super.draw(c, mapView, shadow);

			if (shadow) {
				return; // tegner ikke skygger
			}
			Rect canvasRekt = new Rect(0, 0, c.getWidth(), c.getHeight());
			Rect kortRekt = geoTilSkærmRekt(øverstVenstre, nederstHøjre);

			if (!kortRekt.contains(canvasRekt.centerX(), canvasRekt.centerY())
					&& !iGangMedAtHenteNytKort) {
				iGangMedAtHenteNytKort = true;
				startHentNytKort(mapView.getProjection(), canvasRekt);
				Toast.makeText(KortAktivitet.this, "Henter nyt overlejret kort...", Toast.LENGTH_SHORT).show();
			}
			//Log.d("WMSOverlay", "tegner "+øvVenPix+kortRekt);
			if (overlejretKort != null) {
				c.drawBitmap(overlejretKort, canvasRekt, kortRekt, paint);
			}
			c.drawRect(kortRekt, paint);
		}

		@Override
		public boolean onTap(GeoPoint pkt, MapView mv) {
			Toast.makeText(KortAktivitet.this, "Trykkede på " + pkt, Toast.LENGTH_SHORT).show();
			valgtPunkt = pkt;
			return false;
		}

		private void startHentNytKort(final Projection projektion, final Rect canvasRekt) {
			new AsyncTask() {

				GeoPoint nytØV = projektion.fromPixels(0, 0);
				GeoPoint nytNH = projektion.fromPixels(canvasRekt.width(), canvasRekt.height());
				Rect nytKortRekt = geoTilSkærmRekt(nytØV, nytNH);

				@Override
				protected Object doInBackground(Object... arg0) {
					try {
						// Kilde: http://androidgps.blogspot.com/2008/09/simple-wms-client-for-android.html
						String url = String.format(Locale.US, "http://iceds.ge.ucl.ac.uk/cgi-bin/icedswms?"
								+ "LAYERS=lights&TRANSPARENT=true&FORMAT=image/png&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&EXCEPTIONS=application/vnd.ogc.se_inimage&SRS=EPSG:4326"
								+ "&BBOX=%f,%f,%f,%f&WIDTH=%d&HEIGHT=%d",
								MIKRO * nytØV.getLongitudeE6(), MIKRO * nytNH.getLatitudeE6(),
								MIKRO * nytNH.getLongitudeE6(), MIKRO * nytØV.getLatitudeE6(),
								canvasRekt.width(), canvasRekt.height());

						Log.d("WMSOverlay", "henter nyt kort fra: " + url);
						InputStream input = new URL(url).openStream();
						overlejretKort = BitmapFactory.decodeStream(input);
						Log.d("WMSOverlay", "færdig med at hente nyt kort");
						øverstVenstre = nytØV;
						nederstHøjre = nytNH;
						mapView.postInvalidate(); // bed om en gentegning af kortet (fra en anden tråd)
						input.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					iGangMedAtHenteNytKort = false;
					return null;
				}
			}.execute(); // start udførelsen asynkront
		}
	}

}
