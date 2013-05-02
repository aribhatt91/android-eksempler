/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eks.grafik;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import dk.nordfalk.android.elementer.R;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Simplificeret udgave af Android ApiDemos kompas-eksempel
 *	(com.example.android.apis.graphics.Compass)
 */
public class Kompas extends Activity implements SensorEventListener {
	SensorManager sensorManager;
	Sensor sensor;
	float vinkelTilNord, hældning, krængning;
	KompasView kompasView;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		kompasView = new KompasView(this);
		setContentView(kompasView);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this); // Stop med at modtage sensordata
	}

	public void onSensorChanged(SensorEvent event) {
		vinkelTilNord = event.values[0];
		hældning = event.values[1];
		krængning = event.values[2];
		kompasView.invalidate();

    SensorObs so = new SensorObs(event.timestamp, event.values);
    SensorData.liste.add(so);
    System.out.println("&t="+so.timestamp+"&v="+so.values[0]);

    /*
    long nu = System.currentTimeMillis();
    if (SensorData.sidstDerBlevSendtData < nu - 1000*10) {
      SensorData.sidstDerBlevSendtData = nu;
      System.out.println("SensorData.sidstDerBlevSendtData = "+SensorData.sidstDerBlevSendtData);

      new AsyncTask() {
        int n = 0;
        @Override
        protected Object doInBackground(Object... params) {
          System.out.println("doInBackground START");

          try {
            String data = "";
            for (; n<SensorData.liste.size(); n++) {
              SensorObs sobs = SensorData.liste.get(n);
              data = data + "_t="+sobs.timestamp+"_v="+sobs.values[0];
            }

            URL url = new URL("http://javabog.dk/sensorObs_id=jacob"+data);
            URLConnection uc = url.openConnection();
            uc.setDoOutput(true);
            uc.connect();
            uc.getOutputStream();
            System.out.println(""+url);
            Object c = url.getContent();
            System.out.println("c="+c);

          } catch (Exception ex) {
            ex.printStackTrace();
            n = 0;
          }
          System.out.println("doInBackground SLUT");

          return "ok";
        }

        @Override
        protected void onPostExecute(Object result) {
          SensorData.liste = new ArrayList<SensorObs>(
              SensorData.liste.subList(n, SensorData.liste.size()));
        }

      }.execute();

      
    }
    */

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}


	class KompasView extends View {
		Paint paint = new Paint();
		Path kompaspilPath = new Path();

		// Indlæs res/drawable/bil.png
		Drawable enBil = getResources().getDrawable(R.drawable.bil);

		public KompasView(Context context) {
			super(context);

			// Lav en form som en kompas-pil
			kompaspilPath.moveTo(0, -50);
			kompaspilPath.lineTo(-20, 60);
			kompaspilPath.lineTo(0, 50);
			kompaspilPath.lineTo(20, 60);
			kompaspilPath.close();

			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.FILL);

			enBil.setBounds(-100, -100, 100, 100);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// Tegn en hvid baggrund
			canvas.drawColor( Color.WHITE );

			// Tegn kompas-pilen
			canvas.save();
			canvas.translate(getWidth()/2, getHeight()/2);
			canvas.rotate(-vinkelTilNord);
			canvas.drawPath(kompaspilPath, paint);
			canvas.restore();

			// Tegn også en bil for sjovs skyld
			// Den drejer afhængig af hældning og bliver trykket skæv afhængig af krængning
			canvas.save();
			canvas.translate(100, 100);
			canvas.rotate(hældning);
			canvas.skew(krængning / 20, 0);
			enBil.draw(canvas);
			canvas.restore();

		}
	}

}
