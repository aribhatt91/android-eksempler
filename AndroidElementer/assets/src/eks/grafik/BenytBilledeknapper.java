package eks.grafik;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import dk.nordfalk.android.elementer.R;

/**
 *
 * @author Jacob Nordfalk
 */
public class BenytBilledeknapper extends Activity implements OnClickListener {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    TextView tv = new TextView(this);
    tv.setText("Dette eksempel viser billeder der skal fungere som knapper.\n"
            +"Men hvis man vil slippe for knap-rammen så kan man ikke se at knapppen bliver trykket ned.\n"
            +"Det kan løses ved at farve billedet når det berøres ('trykkes ned').\n"
    );

    ImageButton knap1 = new ImageButton(this);
    knap1.setImageResource(R.drawable.logo);
    tl.addView(knap1);

    ImageButton knap2 = new ImageButton(this);
    knap2.setImageResource(R.drawable.logo);
    knap2.setOnTouchListener(farvKnapNårDenErTrykketNed);
    tl.addView(knap2);

    ImageButton knap3 = new ImageButton(this);
    knap3.setImageResource(R.drawable.logo);
    knap3.setBackgroundDrawable(null);
    tl.addView(knap3);

    ImageButton knap4 = new ImageButton(this);
    knap4.setImageResource(R.drawable.logo);
    knap4.setBackgroundDrawable(null);
    knap4.setOnTouchListener(farvKnapNårDenErTrykketNed);
    tl.addView(knap4);


    ImageView knap5 = new ImageView(this);
    knap5.setImageResource(R.drawable.logo);
    tl.addView(knap5);

    ImageView knap6 = new ImageView(this);
    knap6.setImageResource(R.drawable.logo);
    knap6.setOnTouchListener(farvKnapNårDenErTrykketNed);
    tl.addView(knap6);


    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
    knap4.setOnClickListener(this);
    knap5.setOnClickListener(this);
    knap6.setOnClickListener(this);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  private static OnTouchListener farvKnapNårDenErTrykketNed = new OnTouchListener() {

    public boolean onTouch(View view, MotionEvent me) {
      ImageView ib = (ImageView) view;
      if (me.getAction() == MotionEvent.ACTION_DOWN) {
        //log("farve "+me);
        //log(++xxx);
        ib.setColorFilter(0xFFA0A0A0, PorterDuff.Mode.MULTIPLY);
      } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
      } else {
        //log("ikke farve "+me);
        ib.setColorFilter(null);
      }
      return false;
    }
  };

  public void onClick(View hvadBlevDerKlikketPå) {
    Toast.makeText(this, "Klik "+hvadBlevDerKlikketPå, Toast.LENGTH_SHORT).show();
  }
}
