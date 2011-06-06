/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.fragmenter.retain_instance;

import android.os.Bundle;
import android.widget.ProgressBar;
import dk.nordfalk.android30.elementer.R;
import eks.livscyklus.LogFragment;

/**
 * This is the Fragment implementation that will be retained across
 * activity instances.  It represents some ongoing work, here a thread
 * we have that sits around incrementing a progress indicator.
 */
/**
 *
 * @author j
 */
public class RetainedFragment extends LogFragment {
  ProgressBar mProgressBar;
  int mPosition;
  boolean mReady = false;
  boolean mQuiting = false;
  /**
   * This is the thread that will do our work.  It sits in a loop running
   * the progress up until it has reached the top, then stops and waits.
   */
  final Thread mThread = new Thread() {

    @Override
    public void run() {
      // We'll figure the real value out later.
      int max = 10000;
      // This thread runs almost forever.
      while (true) {
        // Update our shared state with the UI.
        synchronized (this) {
          // Our thread is stopped if the UI is not ready
          // or it has completed its work.
          while (!mReady || mPosition >= max) {
            if (mQuiting) {
              return;
            }
            try {
              wait();
            } catch (InterruptedException e) {
            }
          }
          // Now update the progress.  Note it is important that
          // we touch the progress bar with the lock held, so it
          // doesn't disappear on us.
          mPosition++;
          max = mProgressBar.getMax();
          mProgressBar.setProgress(mPosition);
        }
        // Normally we would be doing some work, but put a kludge
        // here to pretend like we are.
        synchronized (this) {
          try {
            wait(50);
          } catch (InterruptedException e) {
          }
        }
      }
    }
  };

  /**
   * Fragment initialization.  We way we want to be retained and
   * start our thread.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Tell the framework to try to keep this fragment around
    // during a configuration change.
    setRetainInstance(true);
    // Start up the worker thread.
    mThread.start();
  }

  /**
   * This is called when the Fragment's Activity is ready to go, after
   * its content view has been installed; it is called both after
   * the initial fragment creation and after the fragment is re-attached
   * to a new activity.
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Retrieve the progress bar from the target's view hierarchy.
    mProgressBar = (ProgressBar) getTargetFragment().getView().findViewById(R.id.progress_horizontal);
    // We are ready for our thread to go.
    synchronized (mThread) {
      mReady = true;
      mThread.notify();
    }
  }

  /**
   * This is called when the fragment is going away.  It is NOT called
   * when the fragment is being propagated between activity instances.
   */
  @Override
  public void onDestroy() {
    // Make the thread go away.
    synchronized (mThread) {
      mReady = false;
      mQuiting = true;
      mThread.notify();
    }
    super.onDestroy();
  }

  /**
   * This is called right before the fragment is detached from its
   * current activity instance.
   */
  @Override
  public void onDetach() {
    // This fragment is being detached from its activity.  We need
    // to make sure its thread is not going to touch any activity
    // state after returning from this function.
    synchronized (mThread) {
      mProgressBar = null;
      mReady = false;
      mThread.notify();
    }
    super.onDetach();
  }

  /**
   * API for our UI to restart the progress thread.
   */
  public void genstartArbejdet() {
    synchronized (mThread) {
      mPosition = 0;
      mThread.notify();
    }
  }

}
