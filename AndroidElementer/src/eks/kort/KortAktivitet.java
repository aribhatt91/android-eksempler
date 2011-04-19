package eks.kort;

import android.content.Intent;
import android.graphics.Point;
import java.io.InputStream;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import dk.nordfalk.android.elementer.R;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Jacob Nordfalk
 */
public class KortAktivitet extends MapActivity {
  static float MIKRO=1.0f/1000000; // Google maps' koordinater er i mikrograder

  MapView mapView;

  // Overlejrede data
  MyLocationOverlay myLocationOverlay;
  WMSOverlay wMSOverlay;
  MitItemizedOverlay itemizedoverlay;
  
  // Geopunkter
  GeoPoint valby = new GeoPoint(55654074, 12493775);
  GeoPoint valgtPunkt = valby;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Debug-nøgle: 0osb1BfVdrk1u8XJFAcAD0tA5hvcMFVbzInEgNQ
    mapView=new MapView(this, "0osb1BfVdrk1u8XJFAcAD0tA5hvcMFVbzInEgNQ");
    // Officiel nøgle: 0osb1BfVdrk3etct3WjSX-gUUayztcGvB51EMwg
    //mapView=new MapView(this, "0osb1BfVdrk3etct3WjSX-gUUayztcGvB51EMwg");

    mapView.getController().setZoom(7);
    mapView.setClickable(true);
    mapView.setEnabled(true);
    mapView.setBuiltInZoomControls(true);
    // Centrér omkring Valby
    mapView.getController().setCenter(valby);


    // Overlejr kortet med brugerens placering
    myLocationOverlay=new MyLocationOverlay(this, mapView);
    myLocationOverlay.runOnFirstFix(new Runnable() {
      public void run() { // Flyt kort til aktuelt sted når første stedbestemmelse er foretaget
        mapView.getController().animateTo(myLocationOverlay.getMyLocation());
      }
    });

    // Vis liste af overlejrede ikoner
    Drawable ikon=getResources().getDrawable(android.R.drawable.star_big_on);
    itemizedoverlay=new MitItemizedOverlay(ikon);
    itemizedoverlay.tilføj(new OverlayItem(new GeoPoint(57607065, 10249187), "Tversted Plantage", "Osterklit"), ikon);
    itemizedoverlay.tilføj(new OverlayItem(new GeoPoint(55756630,  9133909), "Legoland", "Billund"), getResources().getDrawable(android.R.drawable.ic_dialog_email));
    itemizedoverlay.tilføj(new OverlayItem(valby, "Her bor Jacob", "Valby"), getResources().getDrawable(R.drawable.logo));
    itemizedoverlay.tilføj(new OverlayItem(new GeoPoint(54714330, 11664910), "Døllefjælde-Musse", "Naturpark"), null);
    itemizedoverlay.tilføjFærdig();

    
    wMSOverlay = new WMSOverlay();
    
    mapView.getOverlays().add(myLocationOverlay);    
    mapView.getOverlays().add(itemizedoverlay);
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

  protected boolean isRouteDisplayed() { return false; } // skal defineres

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, 50, 0, "Satellit til/fra");
    menu.add(0, 51, 0, "Trafik til/fra");
    menu.add(0, 52, 0, "Hvor er der\ngadevisn. til/fra");
    menu.add(0, 53, 0, "Rutevejledning");
    menu.add(0, 54, 0, "Start std kortvisning");
    menu.add(0, 55, 0, "Start gadevisning");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    GeoPoint her = myLocationOverlay.getMyLocation();
    if (her==null) her=valby;
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
        startActivity(new Intent(android.content.Intent.ACTION_VIEW, 
            Uri.parse("http://maps.google.com/maps?saddr="+her.getLatitudeE6()*MIKRO+","+her.getLongitudeE6()*MIKRO+
            "&daddr="+valgtPunkt.getLatitudeE6()*MIKRO+","+valgtPunkt.getLongitudeE6()*MIKRO))); 
        break;
      case 54:
        startActivity(new Intent(android.content.Intent.ACTION_VIEW, 
            Uri.parse("geo:"+valgtPunkt.getLatitudeE6()*MIKRO+","+valgtPunkt.getLongitudeE6()*MIKRO+"?z="+mapView.getZoomLevel()))); 
        break;
      case 55:
        startActivity(new Intent(android.content.Intent.ACTION_VIEW, 
            Uri.parse("google.streetview:cbll="+valgtPunkt.getLatitudeE6()*MIKRO+","+valgtPunkt.getLongitudeE6()*MIKRO+"&cbp=1"))); 
        break;
      default:
        Toast.makeText(this, "Ikke implementeret", Toast.LENGTH_SHORT).show();
    }

    return super.onOptionsItemSelected(item);
  }

    
//  http://geusjuptest.geus.dk/OneGeologyEurope/
// http://geusjuptest.geus.dk/oneGEconnector/?service=WMS&version=1.1.1&styles=age&request=GetMap&layers=OGE_1M_surface_GeologicUnit&width=500&height=500&bbox=8,54,15.5,58&format=image/png&srs=epsg:4326
  public class WMSOverlay extends Overlay {
    GeoPoint øverstVenstre=new GeoPoint(0, 0);
    GeoPoint nederstHøjre=new GeoPoint(0, 0);
    Bitmap kort;
    Paint paint=new Paint();
    {
      paint.setAlpha(128);
      paint.setStyle(Paint.Style.STROKE);
    }

    public Bitmap hentNytKort(int width, int height) {
      try {
        String url=String.format("http://iceds.ge.ucl.ac.uk/cgi-bin/icedswms?"
            +"LAYERS=lights&TRANSPARENT=true&FORMAT=image/png&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&STYLES=&EXCEPTIONS=application/vnd.ogc.se_inimage&SRS=EPSG:4326"
            +"&BBOX=%f,%f,%f,%f&WIDTH=%d&HEIGHT=%d",
            MIKRO*øverstVenstre.getLongitudeE6(), MIKRO*nederstHøjre.getLatitudeE6(),
            MIKRO*nederstHøjre.getLongitudeE6(), MIKRO*øverstVenstre.getLatitudeE6(), width, height);
        Log.d("WMSOverlay", "henter "+url);
        InputStream input=new URL(url).openStream();
        Bitmap bm=BitmapFactory.decodeStream(input);
        input.close();
        return bm;
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    public void draw(Canvas c, MapView mapView, boolean shadow) {
      super.draw(c, mapView, shadow);

      if (!shadow) {
        Rect canvasRekt=new Rect(0, 0, c.getWidth(), c.getHeight());
        Rect kortRekt=findKortRekt();

        if (!kortRekt.contains(canvasRekt.centerX(), canvasRekt.centerY())) {
          øverstVenstre=mapView.getProjection().fromPixels(0, 0);
          nederstHøjre=mapView.getProjection().fromPixels(c.getWidth(), c.getHeight());
          kortRekt=findKortRekt();
          kort=hentNytKort(c.getWidth(), c.getHeight());
          //kortRekt = canvasRekt;
        }
        //Log.d("WMSOverlay", "tegner "+øvVenPix+kortRekt);
        c.drawBitmap(kort, canvasRekt, kortRekt, paint);
        //c.drawBitmap(kort, 0, 0, paint);
        c.drawRect(kortRekt, paint);
      }
    }

    private Rect findKortRekt() {
      Point p1=mapView.getProjection().toPixels(øverstVenstre, null);
      Point p2=mapView.getProjection().toPixels(nederstHøjre, null);
      Rect kortRekt=new Rect(p1.x, p1.y, p2.x, p2.y);
      return kortRekt;
    }
    
    @Override
    public boolean onTap(GeoPoint pkt, MapView mv) {
      Toast.makeText(KortAktivitet.this, "Trykkede på "+ pkt, Toast.LENGTH_SHORT).show();      
      valgtPunkt = pkt;
      return false;
    }       
  }

  
  
  public class MitItemizedOverlay extends ItemizedOverlay<OverlayItem>
  {
    public ArrayList<OverlayItem> elementer=new ArrayList<OverlayItem>();

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
      OverlayItem e=elementer.get(i);
      Toast.makeText(KortAktivitet.this, "Klikkede nr "+ i + " "+e.getTitle(), Toast.LENGTH_SHORT).show();
      Toast.makeText(KortAktivitet.this, e.getSnippet()+"\n"+e.routableAddress(), Toast.LENGTH_SHORT).show();
      return true;
    }

    void tilføj(OverlayItem overlayItem, Drawable ikon) {
      if (ikon!=null) {
        overlayItem.setMarker(boundCenterBottom(ikon));
      }
      elementer.add(overlayItem);
    }
  }
}
