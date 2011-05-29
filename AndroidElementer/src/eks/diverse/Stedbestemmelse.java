/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.diverse;

import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jacob Nordfalk
 */
public class Stedbestemmelse extends Activity {

  TextView textView;
  ScrollView scrollView;
  LocationManager locationManager;
  Locationlytter locationlytter = new Locationlytter();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView = new TextView(this);
    scrollView = new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


    // Løb igennem alle udbyderne (typisk "gps", "network" og "passive")
    for (String udbyderNavn : locationManager.getAllProviders()) {
      LocationProvider udbyder = locationManager.getProvider(udbyderNavn);
      Location sidsteSted = locationManager.getLastKnownLocation(udbyderNavn);

      textView.append(udbyderNavn + " - tændt: " + locationManager.isProviderEnabled(udbyderNavn) + "\n"
              + "præcision=" + udbyder.getAccuracy() + " " + "strømforbrug=" + udbyder.getPowerRequirement() + "\n"
              + "requiresSatellite=" + udbyder.requiresSatellite() + " requiresNetwork=" + udbyder.requiresNetwork() + "\n"
              + "sidsteSted=" + sidsteSted + "\n\n");

    }

  }

  @Override
  protected void onResume() {
    super.onResume();

    Criteria kriterium = new Criteria();
    kriterium.setAccuracy(Criteria.ACCURACY_FINE);
    String bedsteUdbyder = locationManager.getBestProvider(kriterium, true);

    //  Bed om opdateringer hvert 60. sekunder eller 20. meter
    locationManager.requestLocationUpdates(bedsteUdbyder, 60000, 20, locationlytter);

    textView.append("========= Lytter til udbyder: " + bedsteUdbyder + "\n\n");


    Geocoder geocoder = new Geocoder(this);
    Location sidsteSted = locationManager.getLastKnownLocation(bedsteUdbyder);
    if (sidsteSted!=null) try { // forsøg at finde nærmeste adresse
      List<Address> adresser = geocoder.getFromLocation(sidsteSted.getLatitude(), sidsteSted.getLongitude(), 1);
      if (adresser!=null && adresser.size()>0) {
        textView.append("NÆRMESTE ADRESSE: "+adresser.get(0).toString()+ "\n\n");
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(locationlytter);
  }

  // indre klasse
  class Locationlytter implements LocationListener {

    public void onLocationChanged(Location sted) {
      textView.append(sted + "\n\n");
      scrollView.scrollTo(0, textView.getHeight()); // rul ned i bunden
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onProviderDisabled(String arg0) {
    }
  }; // Locationlytter slut
}
