package leg.lion.mensawidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class MensaWidget extends AppWidgetProvider {

    /**
     * Feedback:
     * Farbe einstellbar machen
     * Schrift mitskalieren
     * Updatefrequenz anpassen
     */



    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String headerText, String widgetText) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mensa_widget);
        views.setTextViewText(R.id.header, headerText);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.footer, "Last updated at "+timeFormat.format(new Date()));
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(100), rnd.nextInt(100), rnd.nextInt(100));
        //views.setInt(R.id.appwidget_text, "setBackgroundColor", color);
        views.setInt(R.id.appwidget_layout, "setBackgroundColor", color);

        Intent intentUpdate = new Intent(context, MensaWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.button_update, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Log.d("==============Received:","Update======================================");
        String headerText = Requester.header();
        String widgetText = Requester.fullString();
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, headerText, widgetText);
        }
    }

    /*@Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }*/
}

