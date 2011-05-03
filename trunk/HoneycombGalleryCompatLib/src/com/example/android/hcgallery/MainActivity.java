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
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity {

    private static final int NOTIFICATION_DEFAULT = 1;
    private static final String ACTION_DIALOG = "com.example.android.hcgallery.action.DIALOG";

    private View mActionBarView;
    private String[] mToggleLabels = {"Show Titles", "Hide Titles"};
    private int mLabelIndex = 1;
    private int mThemeId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.getInt("theme", -1) != -1) {
            mThemeId = savedInstanceState.getInt("theme");
            this.setTheme(mThemeId);
        }

        setContentView(R.layout.main);

        Directory.initializeDirectory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case R.id.toggleTitles:
            toggleVisibleTitles();
            return true;

        case R.id.toggleTheme:
            if (mThemeId == R.style.AppTheme_Dark) {
                mThemeId = R.style.AppTheme_Light;
            } else {
                mThemeId = R.style.AppTheme_Dark;
            }
            //this.recreate();
            return true;

        case R.id.showDialog:
            showDialog("This is indeed an awesome dialog.");
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void toggleVisibleTitles() {
        // Use these for custom animations.
        final FragmentManager fm = getSupportFragmentManager();// getFragmentManager();
        final TitlesFragment f = (TitlesFragment) fm
                .findFragmentById(R.id.frag_title);
        final View titlesView = f.getView();
        mLabelIndex = 1 - mLabelIndex;

        // Determine if we're in portrait, and whether we're showing or hiding the titles
        // with this toggle.
        final boolean isPortrait = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;

        final boolean shouldShow = f.isHidden();


        if (shouldShow) {
            fm.beginTransaction().show(f).commit();
        } else {
            fm.beginTransaction().hide(f).commit();
        }


        // Manually trigger onNewIntent to check for ACTION_DIALOG.
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (ACTION_DIALOG.equals(intent.getAction())) {
            showDialog(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    void showDialog(String text) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        DialogFragment newFragment = MyDialogFragment.newInstance(text);

        // Show the dialog.
        newFragment.show(ft, "dialog");
    }


    PendingIntent getDialogPendingIntent(String dialogText) {
        return PendingIntent.getActivity(
                this,
                dialogText.hashCode(), // Otherwise previous PendingIntents with the same
                                       // requestCode may be overwritten.
                new Intent(ACTION_DIALOG)
                        .putExtra(Intent.EXTRA_TEXT, dialogText)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                0);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(1).setTitle(mToggleLabels[mLabelIndex]);
        return true;
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", mThemeId);
    }


    public static class MyDialogFragment extends DialogFragment {

        public static MyDialogFragment newInstance(String title) {
            MyDialogFragment frag = new MyDialogFragment();
            Bundle args = new Bundle();
            args.putString("text", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String text = getArguments().getString("text");

            return new AlertDialog.Builder(getActivity())
                    .setTitle("A Dialog of Awesome")
                    .setMessage(text)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }
                    )
                    .create();
        }
    }
}
