package widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import io.github.project_travel_mate.R;
import io.github.project_travel_mate.utilities.ChecklistActivity;

/**
 * Created by Santosh on 11/09/2018.
 */
public class CheckListWidgetProvider extends AppWidgetProvider {

    /*
     * this method is called every 30 mins as specified on widgetinfo.xml
     * this method is also called on every phone reboot
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        /*int[] appWidgetIds holds ids of multiple instance of your widget
         * meaning you are placing more than one widgets on your homescreen*/
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetId);

            Intent startActivityIntent = new Intent(context, ChecklistActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context,
                    0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.check_list_view, startActivityPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.checklist_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, CheckListWidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.check_list_view,
                svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.check_list_view, R.id.empty_view);
        return remoteViews;
    }

}