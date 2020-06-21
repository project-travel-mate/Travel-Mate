package widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViewsService;

import database.AppDataBase;
import objects.ChecklistItem;

import static utils.Constants.USER_ID;

/**
 * Created by Santosh on 11/09/2018.
 */
public class CheckListWidgetService extends RemoteViewsService {

    private AppDataBase mDatabase;
    /*
     * So pretty simple just defining the Adapter of the listview
     * here Adapter is CheckListProvider
     * */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = sharedPreferences.getString(USER_ID, "0");

        mDatabase = AppDataBase.getAppDatabase(getApplicationContext(), userId);

        ChecklistItem[] mChecklistItems = mDatabase.widgetCheckListDao().loadAll(Integer.parseInt(userId));

        return (new CheckListProvider(this.getApplicationContext(), mChecklistItems, intent));
    }

}
