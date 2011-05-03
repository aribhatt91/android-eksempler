/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.example.android.hcgallery;

import com.example.android.hcgallery.R;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContentFragment extends Fragment {
    private View mContentView;

    // The bitmap currently used by ImageView
    private Bitmap mBitmap = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.content_welcome, null);
        final ImageView imageView = (ImageView) mContentView.findViewById(R.id.image);
        mContentView.setDrawingCacheEnabled(false);

        // When long-pressing a photo, activate the action mode for selection, showing the
        // contextual action bar (CAB).


        return mContentView;
    }

    void updateContentAndRecycleBitmap(int category, int position) {

        if (mBitmap != null) {
            // This is an advanced call and should be used if you
            // are working with a lot of bitmaps. The bitmap is dead
            // after this call.
            mBitmap.recycle();
        }

        // Get the bitmap that needs to be drawn and update the ImageView
        mBitmap = Directory.getCategory(category).getEntry(position)
                .getBitmap(getResources());
        ((ImageView) getView().findViewById(R.id.image)).setImageBitmap(mBitmap);
    }

    void shareCurrentPhoto() {
        File externalCacheDir = new File(Environment.getExternalStorageDirectory()+"/Android/data/"+getActivity().getPackageName()+"/cache/"); //getActivity().getExternalCacheDir();
        if (externalCacheDir == null) {
            Toast.makeText(getActivity(), "Error writing to USB/external storage.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Prevent media scanning of the cache directory.
        final File noMediaFile = new File(externalCacheDir, ".nomedia");
        try {
            noMediaFile.createNewFile();
        } catch (IOException e) {
        }

        // Write the bitmap to temporary storage in the external storage directory (e.g. SD card).
        // We perform the actual disk write operations on a separate thread using the
        // {@link AsyncTask} class, thus avoiding the possibility of stalling the main (UI) thread.

        final File tempFile = new File(externalCacheDir, "tempfile.jpg");

        new AsyncTask<Void, Void, Boolean>() {
            /**
             * Compress and write the bitmap to disk on a separate thread.
             * @return TRUE if the write was successful, FALSE otherwise.
             */
            protected Boolean doInBackground(Void... voids) {
                try {
                    FileOutputStream fo = new FileOutputStream(tempFile, false);
                    if (!mBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fo)) {
                        Toast.makeText(getActivity(), "Error writing bitmap data.",
                                Toast.LENGTH_SHORT).show();
                        return Boolean.FALSE;
                    }
                    return Boolean.TRUE;

                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), "Error writing to USB/external storage.",
                            Toast.LENGTH_SHORT).show();
                    return Boolean.FALSE;
                }
            }

            /**
             * After doInBackground completes (either successfully or in failure), we invoke an
             * intent to share the photo. This code is run on the main (UI) thread.
             */
            protected void onPostExecute(Boolean result) {
                if (result != Boolean.TRUE) {
                    return;
                }

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, "Share photo"));
            }
        }.execute();
    }

}
