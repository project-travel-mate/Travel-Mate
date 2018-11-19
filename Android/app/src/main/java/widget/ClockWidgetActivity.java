package widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import java.text.DateFormat;

import io.github.project_travel_mate.R;

/**
 * Implementation of App Widget functionality.
 */

public class ClockWidgetActivity extends AppWidgetProvider {
    private static DateFormat mCurrentTime = new SimpleDateFormat("hh:mm:ss");
    private static Date mDateBasis = Calendar.getInstance().getTime();
    private static DateFormat mCurrentDate = new SimpleDateFormat("dd/MM/yyyy");
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget_activity);
        //Set Time, Date, and TimeZone of the widget
        TimeZone currentTZ = TimeZone.getDefault();
        views.setTextViewText(R.id.tView_digital_clock, mCurrentTime.format(new Date()));
        views.setTextViewText(R.id.tView_timezone, currentTZ.getDisplayName());
        views.setTextViewText(R.id.tView_tzdate, mCurrentDate.format(mDateBasis));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

