package eks.simpla;

import org.simpla.SimplaView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.TextView;
import org.simpla.SimplaActivity;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Eks5_vejrudsigt extends SimplaActivity {

  Bitmap vejrUdsigt;

  public class MinGrafik extends SimplaView {

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

  public void simplaMain() {

    TextView intro=simpla.createTextView("Vent, indlæser vejrudsigt...");
    intro.setTextSize(36);
    simpla.setContentView(intro);
    int postnr = 2500;
    vejrUdsigt=simpla.createBitmapFromUrl("http://servlet.dmi.dk/byvejr/servlet/byvejr_dag1?by="+postnr+"&mode=long");

    if (vejrUdsigt==null) {
      simpla.println(intro, "\n\nBeklager, den ku ikke hentes");
    } else {
      // Opret grafik-view og vis det
      MinGrafik minGrafik=new MinGrafik();
      simpla.setContentView(minGrafik);
    }

  }
}
