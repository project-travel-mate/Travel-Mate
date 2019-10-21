package widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import database.AppDataBase;
import objects.ChecklistItem;

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

        mDatabase = AppDataBase.getAppDatabase(getApplicationContext());

        ChecklistItem[] mChecklistItems = mDatabase.widgetCheckListDao().loadAll();


        return (new CheckListProvider(this.getApplicationContext(), mChecklistItems, intent));
    }

}
