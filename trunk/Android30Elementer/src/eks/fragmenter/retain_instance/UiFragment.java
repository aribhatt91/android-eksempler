/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.fragmenter.retain_instance;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import dk.nordfalk.android30.elementer.R;
import eks.livscyklus.LogFragment;

/**
 * This is a fragment showing UI that will be updated from work done
 * in the retained fragment.
 */
public class UiFragment extends LogFragment {
  RetainedFragment mWorkFragment;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_retain_instance, container, false);
    // Watch for button clicks.
    Button button = (Button) v.findViewById(R.id.restart);
    button.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        mWorkFragment.genstartArbejdet();
      }
    });
    return v;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    FragmentManager fm = getFragmentManager();
    // Check to see if we have retained the worker fragment.
    mWorkFragment = (RetainedFragment) fm.findFragmentByTag("work");
    // If not retained (or first time running), we need to create it.
    if (mWorkFragment == null) {
      mWorkFragment = new RetainedFragment();
      // Tell it who it is working with.
      mWorkFragment.setTargetFragment(this, 0);
      fm.beginTransaction().add(mWorkFragment, "work").commit();
    }
  }

}
