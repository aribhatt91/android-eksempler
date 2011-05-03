/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.diverse;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;

/**
 *
 * @author Jacob Nordfalk
 */
public class Eks_sensorer extends Activity {

  TextView textView;
  String[] senesteMålinger=new String[9];
  SensorManager sensorManager;
  Sensorlytter sensorlytter=new Sensorlytter();
  MediaPlayer enLyd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    textView=new TextView(this);
    ScrollView scrollView=new ScrollView(this);
    scrollView.addView(textView);
    setContentView(scrollView);
    enLyd=MediaPlayer.create(this, R.raw.jeg_bremser_haardt);
    sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
  }

  @Override
  protected void onResume() {
    super.onResume();
    /*
    Sensor orienteringsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    if (orienteringsSensor != null) {
    sensorManager.registerListener(sensorlytter, orienteringsSensor, SensorManager.SENSOR_DELAY_GAME);
    } else {
    textView.setText("Fejl. Din telefon har ikke en orienteringssensor");
    }
     */
    for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
      System.out.println("sensor="+sensor);
      sensorManager.registerListener(sensorlytter, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    System.out.println("nu blev onPause() kaldt");
    sensorManager.unregisterListener(sensorlytter);
    enLyd.release();
  }

  // indre klasse
  class Sensorlytter implements SensorEventListener {

    public void onSensorChanged(SensorEvent e) {
      int sensortype=e.sensor.getType();

      String måling="Type: "+sensortype+" navn: "+e.sensor.getName()+"\n"
          +"Udbyder "+e.sensor.getVendor()+"\n"
          +"Tid: "+e.timestamp+"  præcision: "+e.accuracy;

      if (sensortype==Sensor.TYPE_ORIENTATION) {
        måling=måling+"\n"+e.values[0]+" - vinkel til nord\n"+e.values[1]+" - hældning\n"+e.values[2]+" - krængning";
      } else {
        måling=måling+"\n"+e.values[0]+"\n"+e.values[1]+"\n"+e.values[2];
      }

      if (sensortype==Sensor.TYPE_ACCELEROMETER) {
        double g=9.80665; // normal-tyngdeaccelerationen - se http://da.wikipedia.org/wiki/Tyngdeacceleration
        double sum=Math.abs(e.values[0])+Math.abs(e.values[1])+Math.abs(e.values[2]);
        if (sum>3*g) {
          if (!enLyd.isPlaying()) enLyd.start(); // BANG!
          måling=måling+"\nBANG!!";
        }
      }

      System.out.println(måling);
      senesteMålinger[sensortype]=måling;

      String tekst=måling;

      // Tilføj alle de forskellige sensorers seneste målinger til teksteo
      for (String m : senesteMålinger) {
        tekst=tekst+"\n\n"+m;
      }
      textView.setText(tekst);
    }

    public void onAccuracyChanged(Sensor sensor, int præcision) {
      // ignorér - men vi er nødt til at have metoden for at implementere interfacet
    }
  }; // Sensorlytter slut
}
