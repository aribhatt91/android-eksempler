/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eks.fragmenter.dialoger;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import dk.nordfalk.android30.elementer.R;
import eks.livscyklus.LogDialogFragment;

/**
 *
 * @author j
 */
public class MyDialogFragment extends LogDialogFragment {

  int mNum;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mNum = getArguments().getInt("num");
    // Pick a style based on the num.
    int style = DialogFragment.STYLE_NORMAL;
    switch (mNum % 4) {
      case 1:
        style = DialogFragment.STYLE_NO_TITLE;
        break;
      case 2:
        style = DialogFragment.STYLE_NO_FRAME;
        break;
      case 3:
        style = DialogFragment.STYLE_NO_INPUT;
        break;
    }
    int theme = 0;
    setStyle(style, theme);
  }

  static String getNameForNum(int num) {
    switch (num % 4) {
      case 1:
        return "STYLE_NO_TITLE";
      case 2:
        return "STYLE_NO_FRAME";
      case 3:
        return "STYLE_NO_INPUT (this window can't receive input, so "
                + "you will need to press the bottom show button)";
    }
    return "STYLE_NORMAL";
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_dialog, container, false);

    TextView tv = (TextView) v.findViewById(R.id.text);
    tv.setText("Dialog #" + mNum + ": using style " + getNameForNum(mNum));

    // Watch for button clicks.
    Button button = (Button) v.findViewById(R.id.show);
    button.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
        // When button is clicked, call up to owning activity.
        ((BenytDialogFragment) getActivity()).visDialogFragment();
      }
    });
    return v;
  }
}
