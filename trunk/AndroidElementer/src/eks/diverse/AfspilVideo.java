/*
 * Copyright (C) 2009 The Android Open Source Project
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
package eks.diverse;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class AfspilVideo extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		VideoView videoView = new VideoView(this);
		setContentView(videoView);

		videoView.setVideoURI(Uri.parse("file:///sdcard/DCIM/100MEDIA/VIDEO0025.3gp"));
		videoView.setMediaController(new MediaController(this));
		videoView.requestFocus();
		videoView.start();

	}
}
