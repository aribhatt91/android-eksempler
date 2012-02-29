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
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import dk.nordfalk.android.elementer.R;

/** Simplificeret udgave af Android ApiDemos kompas-eksempel
 *	(com.example.android.apis.graphics.Compass)
 */
public class Kompas extends Activity implements SensorEventListener {
	SensorManager sensorManager;
	Sensor sensor;
	float vinkelTilNord, hældning, krængning;
	KompasView kompasView;
	WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		kompasView = new KompasView(this);
		setContentView(kompasView);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Kompas");
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		wakeLock.acquire(); // Hold skærmen tændt
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this); // Stop med at modtage sensordata
		wakeLock.release(); // Frigiv låsen på at holde skærmen tændt
	}

	long sidsteTid = 0;

	public void onSensorChanged(SensorEvent event) {
		vinkelTilNord = event.values[0];
		hældning = event.values[1];
		krængning = event.values[2];

		sidsteTid = event.timestamp;

		kompasView.invalidate();
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
