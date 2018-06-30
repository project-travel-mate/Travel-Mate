package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import objects.ChecklistItem;
import utils.AppExecutors;
import utils.DbChecklist;

import static utils.Constants.BASE_TASKS;
import static utils.Constants.ID_ADDED_INDB;


public class ChecklistFragment extends Fragment {

    private final ArrayList<ChecklistItem> mItems = new ArrayList<>();
    @BindView(R.id.listview)
    ListView listview;
    private CheckListAdapter mAdapter;
    private DbChecklist mChecklistDatabase;
    private Activity mActivity;
    private Executor mAppExecutor = AppExecutors.getInstance().getDiskIO();

    public ChecklistFragment() {
    }

    public static ChecklistFragment newInstance() {
        ChecklistFragment fragment = new ChecklistFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_list, container, false);

        mChecklistDatabase = DbChecklist.getsInstance(mActivity);

        ButterKnife.bind(this, view);

        addDefaultItems();

        mAdapter = new CheckListAdapter(mActivity, mItems);
        listview.setAdapter(mAdapter);

        refresh();

        return view;
    }

    @OnClick(R.id.add)
    void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = (mActivity).getLayoutInflater();
        builder.setTitle("Add new item");
        builder.setCancelable(false);
        final View dialogv = inflater.inflate(R.layout.dialog, null, false);
        builder.setView(dialogv)
                .setPositiveButton("OK", (dialog, id1) -> {
                    EditText e1 = dialogv.findViewById(R.id.task);
                    if (!e1.getText().toString().equals("")) {
                        ChecklistItem checklistItem = new ChecklistItem(e1.getText().toString(), String.valueOf(0));
                        mAppExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                mChecklistDatabase.checklistItemDAO().insertItems(checklistItem);
                            }
                        } );
                        ChecklistFragment.this.refresh();
                    }
                });
        builder.create();
        builder.show();
    }

    private void addDefaultItems() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //First time users
        String isAlreadyAdded = sharedPreferences.getString(ID_ADDED_INDB, "null");
        if (isAlreadyAdded.equals("null")) {
            for (int i = 0; i < BASE_TASKS.size(); i++) {
                ChecklistItem checklistItem = new ChecklistItem(BASE_TASKS.get(i), String.valueOf(0));
                mAppExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mChecklistDatabase.checklistItemDAO().insertItems(checklistItem);
                    }
                });
            }
            editor.putString(ID_ADDED_INDB, "yes");
            editor.apply();
        }
    }

    private void refresh() {
        mItems.clear();
        mAdapter.notifyDataSetChanged();

        mAppExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<ChecklistItem> checklistItems = mChecklistDatabase.checklistItemDAO().getSortedItems();
                for (int i = 0; i < checklistItems.size(); i++)
                    mItems.add(checklistItems.get(i));
            }
        } );

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    // TODO :: Move CheckListAdapter to another file
    class CheckListAdapter extends ArrayAdapter<ChecklistItem> {

        private final Activity mContext;
        private final List<ChecklistItem> mItems;

        CheckListAdapter(Activity context, List<ChecklistItem> items) {
            super(context, R.layout.checklist_item, items);
            this.mContext = context;
            this.mItems = items;
        }

        @NonNull
        @Override
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.checklist_item, parent, false);
                holder = new ViewHolder();
                holder.checkBox = view.findViewById(R.id.cb1);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (mItems.get(position).getIsDone().equals("1")) {
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.checkBox.setChecked(false);
            }
            holder.checkBox.setText(mItems.get(position).getName());

            holder.checkBox.setOnClickListener(view1 -> {
                CheckBox c2 = (CheckBox) view1;
                if (c2.isChecked()) {
                    //updating isDone to 1 in database
                    mAppExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (!mItems.isEmpty())
                             mChecklistDatabase.checklistItemDAO().updateIsDone(mItems.get(position).getId());
                        }
                    } );
                } else {
                    //updating isDone to 0 in database
                    mAppExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (!mItems.isEmpty())
                             mChecklistDatabase.checklistItemDAO().updateUndone(mItems.get(position).getId());
                        }
                    } );
                }

                refresh();
            });
            return view;
        }

        class ViewHolder {
            CheckBox checkBox;
        }
    }
}




