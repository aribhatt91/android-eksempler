package eks.simpla;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import eks.vejret.ByvejrAktivitet;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Eks5_vejrudsigt extends Activity {

  Bitmap vejrUdsigt;

  public class MinGrafik extends View {

    public MinGrafik(Context context) {
		super(context);
	}

	@Override
    protected void onDraw(Canvas canvas) {
      Paint stregtype=new Paint();
      canvas.drawText("Dagens vejrudsigt:", 10, 5, stregtype);

      canvas.rotate( (float) 10.3);
      canvas.scale(0.5f, 0.5f);
      stregtype.setAntiAlias(true);
      canvas.drawBitmap(vejrUdsigt, 0, 20, stregtype);
    }

  }

  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TextView intro= new TextView(this);
    intro.setText("Vent, indlæser vejrudsigt...");
    intro.setTextSize(36);
    setContentView(intro);
    int postnr = 2500;
    try {
	    vejrUdsigt=ByvejrAktivitet.opretBitmapFraUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by="+postnr+"&mode=long");
	    // Opret grafik-view og vis det
	    MinGrafik minGrafik=new MinGrafik(this);
	    setContentView(minGrafik);
  	} catch (Exception ex) {
      intro.append("\n\nBeklager, den ku ikke hentes");
    }
  }
}
