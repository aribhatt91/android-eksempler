package eks.simpla;

import org.simpla.SimplaView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import org.simpla.SimplaActivity;

/**
 * 
 * @author Jacob Nordfalk
 */
public class Eks4_grafik extends SimplaActivity {

  int rotation = -45;

  public class MinGrafik extends SimplaView {

    @Override
    protected void onDraw(Canvas canvas) {

      Paint paint=new Paint();
      paint.setStyle(Paint.Style.FILL);

      // lav baggrunden hvid
      paint.setColor(Color.WHITE);
      canvas.drawPaint(paint);

      int blå = Color.BLUE;  // blå
      blå = 0xff0000ff;  // også blå
      blå = Color.argb(255, 0, 0, 255);  // også blå
      blå = 255*256*256*256 + 255;  // også blå
      paint.setColor(blå);

      canvas.drawCircle(20, 20, 15, paint);

      paint.setAntiAlias(true);  // antialias - tegn udjævnede kanter
      canvas.drawCircle(60, 20, 15, paint);

      paint.setStrokeWidth(2); // sæt stregtykkelsen til 2 punkter
      paint.setColor(Color.RED);

      Path trekantPath=new Path();
      trekantPath.moveTo(0, -10);
      trekantPath.lineTo(5, 0);
      trekantPath.lineTo(-5, 0);
      trekantPath.close();

      paint.setStyle(Paint.Style.STROKE); // tegn streger
      trekantPath.offset(100, 50);  // flyt trekanten ned og til højre
      canvas.drawPath(trekantPath, paint); // tegn den

      trekantPath.offset(40, 0);  // ryk koordinater til højre
      paint.setStyle(Paint.Style.FILL);     // udfyld området - ignorér stregtykkelsen
      canvas.drawPath(trekantPath, paint);
      
      trekantPath.offset(40, 0);  // ryk koordinater til højre
      paint.setStyle(Paint.Style.FILL_AND_STROKE); // udfyld området og tegn stregen med stregtykkelsen
      canvas.drawPath(trekantPath, paint);

      // draw some text using STROKE style
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(1);
      paint.setColor(Color.MAGENTA);
      paint.setTextSize(30);
      canvas.drawText("Style.STROKE", 75, 85, paint);

      // draw some text using FILL style
      paint.setStyle(Paint.Style.FILL);
      paint.setTextSize(30);
      canvas.drawText("Style.FILL", 75, 120, paint);


      // tegn en tyk stiplet linje
      DashPathEffect dashPath=new DashPathEffect(new float[] {20, 5}, 1);
      paint.setPathEffect(dashPath);
      paint.setStrokeWidth(5);
      canvas.drawLine(10, 130, 300, 150, paint);


      // tegn noget roteret tekst
      paint.setPathEffect(null);
      paint.setColor(Color.argb(128, 0, 255, 0)); // halvgennemsigtig grøn
      paint.setTextSize(48);
      String roteretTekst = "Roteret !";

      // find tekstens størrelse på skærmen
      Rect tekstomrids=new Rect();
      paint.getTextBounds(roteretTekst, 0, roteretTekst.length(), tekstomrids);

      int x=75;
      int y=175;
      // rotér lærredet omkring tekstens center
      canvas.rotate( rotation , x+tekstomrids.centerX(), y+tekstomrids.centerY());
      // tegn teksten
      canvas.drawText(roteretTekst, x, y, paint);

      // nulstil rotation (og translation etc)
      canvas.restore();

    }
  }

  public void simplaMain() {

    // Opret grafik-view
    MinGrafik minGrafik=new MinGrafik();
    simpla.setContentView(minGrafik);
    simpla.sleep(250); // vent 1/4 sekund

    for (int n=-45; n<360; n++) {
      rotation = n;
      minGrafik.postInvalidate(); // Får Android til at kalde onDraw() på viewet
      simpla.sleep(100); // vent 0.1 sekund
    }

  }
}
