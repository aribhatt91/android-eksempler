/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.fragmenter.backstack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dk.nordfalk.android30.elementer.R;

/**
 *
 * @author j
 */
public class CountingFragment extends Fragment {
  int mNum;

  /**
   * Create a new instance of CountingFragment, providing "num"
   * as an argument.
   */
  static CountingFragment newInstance(int num) {
    CountingFragment f = new CountingFragment();
    // Supply num input as an argument.
    Bundle args = new Bundle();
    args.putInt("num", num);
    f.setArguments(args);
    return f;
  }

  /**
   * When creating, retrieve this instance's number from its arguments.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mNum = getArguments().getInt("num");
  }

  /**
   * The Fragment's UI is just a simple text view showing its
   * instance number.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //View v = inflater.inflate(R.layout.hello_world, container, false);
    //View tv = v.findViewById(R.id.text);
    TextView tv = new TextView(getActivity());
    tv.setText("Fragment #" + mNum);
    //tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
    tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.ic_media_ff+mNum));
    return tv;
  }

}
