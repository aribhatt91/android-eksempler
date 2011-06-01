package eks.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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
  public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    Log.d(TAG, "onUpdate "+Arrays.asList(appWidgetIds));
    RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.appwidgetlayout);

    remoteViews.setTextViewText(R.id.etTextView, "Klokken er:\n"+new Date());

    // en tilfældig farve!
    int farve = (int) System.currentTimeMillis() | 0xff0000ff;
    remoteViews.setInt(R.id.etTextView, "setTextColor", farve);


    // Lav et intent der skal affyres hvis knapppen trykkes
    PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
          new Intent(ctx, Tegneprogram.class), 0);

    remoteViews.setOnClickPendingIntent(R.id.enKnap, pendingIntent);


    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

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
