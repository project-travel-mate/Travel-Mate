package widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;

import io.github.project_travel_mate.R;
import objects.ChecklistItem;

/**
 * Created by Santosh on 11/09/2018.
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */

public class CheckListProvider implements RemoteViewsFactory {
    private ArrayList<CheckListWidgetItem> mListItemCheckList = new ArrayList<CheckListWidgetItem>();
    private Context mContext;
    private int mAppWidgetId;

    public CheckListProvider(Context context, ChecklistItem[] checklistItems, Intent intent) {
        this.mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem(checklistItems);
    }

    private void populateListItem(ChecklistItem[] checklistItems) {

        for (ChecklistItem checkItem : checklistItems) {
            CheckListWidgetItem checkListItem = new CheckListWidgetItem();
            if (checkItem.getIsDone()) {
                checkListItem.heading = checkItem.getName();
                mListItemCheckList.add(checkListItem);
            }
        }
    }

    @Override
    public int getCount() {
        return mListItemCheckList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.checklist_widget_item);
        CheckListWidgetItem checkListItem = mListItemCheckList.get(position);
        remoteView.setTextViewText(R.id.check_item_title, checkListItem.heading);

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}
