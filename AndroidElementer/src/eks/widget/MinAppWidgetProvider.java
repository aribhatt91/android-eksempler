package eks.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import dk.nordfalk.android.elementer.R;
import eks.grafik.Tegneprogram;
import java.util.Arrays;
import java.util.Date;


public class MinAppWidgetProvider extends AppWidgetProvider {
  private static final String TAG = "MinAppwidgetProvider";

  @Override
  public void onUpdate(Context ctx, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {

    Log.d(TAG, "onUpdate "+Arrays.asList(appWidgetIds));
    final RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.appwidgetlayout);

    remoteViews.setTextViewText(R.id.etTextView, "KL er:\n"+new Date());

    // Vis en tilfældig farve på TextViewet
    int farve = (int) System.currentTimeMillis() | 0xff0000ff;
    remoteViews.setTextColor(R.id.etTextView, farve);

    // generisk måde at gøre det samme på
    remoteViews.setInt(R.id.etTextView, "setTextColor", farve);


    // Lav et intent der skal affyres hvis knapppen trykkes
    PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
          new Intent(ctx, Tegneprogram.class), 0);

    remoteViews.setOnClickPendingIntent(R.id.enKnap, pendingIntent);


    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    // Lad uret opdatere hvert sekund i det næste minut. Burde gøres fra en service!!!
    new Thread() {
      public void run() {
        for (int i=0; i<60; i++) {
          remoteViews.setTextViewText(R.id.etTextView, "Klokken er:\n"+new Date());
          appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ex) {
          }
        }
      }
    }.start();

  }

  @Override
  public void onReceive(Context c,Intent intent) {
    super.onReceive(c, intent);
    Log.d(TAG, "onReceive:" + intent);
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    Log.d(TAG, "onDeleted");
  }

  @Override
  public void onEnabled(Context context) {
    Log.d(TAG, "onEnabled");
  }

  @Override
  public void onDisabled(Context context) {
    Log.d(TAG, "onDisabled");
  }
}
