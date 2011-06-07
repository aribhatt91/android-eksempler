/*
 * Copyright (C) 2010 The Android Open Source Project
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

package eks.fragmenter.dialoger;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import dk.nordfalk.android30.elementer.R;
import eks.livscyklus.LogFragmentActivity;

public class BenytDialogFragment extends LogFragmentActivity {
    int dialognummer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialog);

        View tv = findViewById(R.id.text);
        ((TextView)tv).setText("Example of displaying dialogs with a DialogFragment.  "
                + "Press the show button below to see the first dialog; pressing "
                + "successive show buttons will display other dialog styles as a "
                + "stack, with dismissing or back going to the previous dialog.");

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.show);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                visDialogFragment();
            }
        });

        if (savedInstanceState != null) {
            dialognummer = savedInstanceState.getInt("level");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", dialognummer);
    }


    void visDialogFragment() {
        dialognummer++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        // Denne transaktion skal være et selvstændigt element i backstack
        ft.addToBackStack(null);

        // Create and show the dialog.
        MyDialogFragment f = new MyDialogFragment();

        // Sæt nummeret
        f.mNum = dialognummer;
        // Ovenstående bliver dog ikke persisteret ved ændring af skærmorientering
        // eller nedlukning af JVM, men det gør argumenter, derfor er nedenstående måde bedre
        Bundle fragmentargumenter = new Bundle();
        fragmentargumenter.putInt("num", dialognummer);
        f.setArguments(fragmentargumenter);

        // Vis dialogen (og commit transaktionen)
        f.show(ft, "dialog");
    }


}
