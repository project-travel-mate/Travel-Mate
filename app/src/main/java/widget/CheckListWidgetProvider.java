package widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import io.github.project_travel_mate.R;
import io.github.project_travel_mate.utilities.ChecklistActivity;

/**
 * Created by Santosh on 11/09/2018.
 */
public class CheckListWidgetProvider extends AppWidgetProvider {

    private static final String MyOnClick = "myOnClickTag"; //static variable, which will be your onClick name tag

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


            remoteViews.setOnClickPendingIntent(R.id.image_checkbox, getPendingSelfIntent(context, MyOnClick));
            // initialization of Image button


            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    //Define a helper method to automate the creation of each PendingIntent
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    /*
    Whenever the view that you set the tag is pressed,
    onReceive will capture that and will do the action just the same as our everyday, standard onClick event.
     */
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MyOnClick.equals(intent.getAction())) {
            //your onClick action is here
            Toast.makeText(context, "Widget Button", Toast.LENGTH_SHORT).show();
            /*  Image button check;
                if (check.getVisibility() == View.VISIBLE) {
                    check.setVisibility(View.INVISIBLE);
                } else {
                    check.setVisibility(View.VISIBLE);
                }

             */
        }
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
        remoteViews.setRemoteAdapter(R.id.check_list_view, svcIntent);

        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.check_list_view, R.id.empty_view);
        return remoteViews;
    }

}