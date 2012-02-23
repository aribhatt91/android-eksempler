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
// Simplificeret udgave af Android ApiDemos (com.example.android.apis.graphics)
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

public class Kompasspil extends Activity implements SensorEventListener {
	SensorManager sensorManager;
	Sensor sensor;
	float vinkelTilNord, hældning, krængning, stjerneX, stjerneY;
	boolean død;
	KompasView kompasView;
	WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		kompasView = new KompasView(this);
		setContentView(kompasView);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "");
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		/*
		 * when the activity is resumed, we acquire a wake-lock so that the
		 * screen stays on, since the user will likely not be fiddling with the
		 * screen or buttons.
		 */
		wakeLock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
		// and release our wake-lock
		wakeLock.release();
	}

	long sidsteTid = 0;

	public void onSensorChanged(SensorEvent event) {
		vinkelTilNord = event.values[0];
		hældning = event.values[1];
		krængning = event.values[2];
		død = false;

		if (sidsteTid!=0) {
			float dt = (event.timestamp - sidsteTid) / 10000000.0f;
			stjerneX = stjerneX - krængning * dt;
			stjerneY = stjerneY - hældning * dt;

			if (stjerneX*stjerneX + stjerneY*stjerneY > 100000) {
				// For langt væk - nulstil
				stjerneX = stjerneY = 0;
				død = true; // baggrunden tegnes rødt
			}
		}

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

		// Indlæs Android-resurse android-sdk/platforms/android-*/data/res/drawable/star_on.png
		Drawable enStjerne = getResources().getDrawable(android.R.drawable.star_on);

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
			enStjerne.setBounds(-20, -20, 20, 20);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// Tegn en hvid baggrund, eller rød hvis stjernen kom for langt væk
			canvas.drawColor( død ? Color.RED : Color.WHITE );

			// Tegn bilen
			canvas.save();
			canvas.translate(100, 100);
			canvas.rotate(hældning);
			canvas.skew(krængning / 20, 0);
			enBil.draw(canvas);
			canvas.restore();

			// Tegn stjernen
			canvas.save();
			canvas.translate(getWidth()/2 + stjerneX, getHeight()/2 + stjerneY);
			enStjerne.draw(canvas);
			canvas.restore();

			// Tegn kompasset
			canvas.save();
			canvas.translate(getWidth()-100, 100);
			canvas.rotate(-vinkelTilNord);
			canvas.drawPath(kompaspilPath, paint);
			canvas.restore();
		}
	}

}
