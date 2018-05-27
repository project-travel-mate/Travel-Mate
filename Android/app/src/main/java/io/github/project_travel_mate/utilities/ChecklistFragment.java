package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import objects.ChecklistItem;
import utils.ChecklistEntry;
import utils.Constants;
import utils.DBhelp;


public class ChecklistFragment extends Fragment {

    private CheckListAdapter    adapter;
    private SQLiteDatabase      db;
    private Activity            activity;

    private ArrayList<ChecklistItem> items = new ArrayList<>();

    @BindView(R.id.lv)
    ListView lv;

    public ChecklistFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_check_list, container, false);

        DBhelp dbhelp = new DBhelp(getContext());
        db = dbhelp.getWritableDatabase();

        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //First time uers
        String isAlreadyAdded = sharedPreferences.getString(Constants.ID_ADDED_INDB, "null");
        if (isAlreadyAdded.equals("null")) {

            for (int i = 0; i < Constants.baseTask.size(); i++) {
                ContentValues insertValues = new ContentValues();
                insertValues.put(ChecklistEntry.COLUMN_NAME, Constants.baseTask.get(i));
                insertValues.put(ChecklistEntry.COLUMN_NAME_ISDONE, "0");
                db.insert(ChecklistEntry.TABLE_NAME, null, insertValues);
            }

            editor.putString(Constants.ID_ADDED_INDB, "yes");
            editor.apply();
        }

        adapter = new CheckListAdapter(activity, items);
        lv.setAdapter(adapter);

        refresh();

        return view;
    }

    @OnClick(R.id.add) void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (activity).getLayoutInflater();
        builder.setTitle("Add new item");
        builder.setCancelable(false);
        final View dialogv = inflater.inflate(R.layout.dialog, (ViewGroup) null);
        builder.setView(dialogv)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id1) {
                        EditText e1 = dialogv.findViewById(R.id.task);
                        if (!e1.getText().toString().equals("")) {
                            ContentValues insertValues = new ContentValues();
                            insertValues.put(ChecklistEntry.COLUMN_NAME, e1.getText().toString());
                            insertValues.put(ChecklistEntry.COLUMN_NAME_ISDONE, "0");
                            db.insert(ChecklistEntry.TABLE_NAME, null, insertValues);
                            ChecklistFragment.this.refresh();
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    private void refresh() {
        items.clear();
        adapter.notifyDataSetChanged();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ChecklistEntry.TABLE_NAME + " ORDER BY " +
                ChecklistEntry.COLUMN_NAME_ISDONE, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ChecklistEntry.COLUMN_NAME_ID));
                String task = cursor.getString(cursor.getColumnIndex(ChecklistEntry.COLUMN_NAME));
                String isdone = cursor.getString(cursor.getColumnIndex(ChecklistEntry.COLUMN_NAME_ISDONE));
                items.add(new ChecklistItem(id, task, isdone));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    class CheckListAdapter extends ArrayAdapter<ChecklistItem> {

        private final Activity context;
        private final List<ChecklistItem> items;
        DBhelp dbhelp;
        SQLiteDatabase db;

        CheckListAdapter(Activity context, List<ChecklistItem> items) {
            super(context, R.layout.checklist_item, items);
            this.context = context;
            this.items = items;
        }

        class ViewHolder {
            CheckBox c;
        }

        @NonNull
        @Override
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View vi = view;             //trying to reuse a recycled view
            ViewHolder holder;
            if (vi == null) {
                vi = inflater.inflate(R.layout.checklist_item, parent, false);
                holder = new ViewHolder();
                holder.c = vi.findViewById(R.id.cb1);
                vi.setTag(holder);

            } else {
                holder = (ViewHolder) vi.getTag();
            }

            dbhelp = new DBhelp(context);
            db = dbhelp.getWritableDatabase();

            if (items.get(position).getIsDone().equals("1")) {
                holder.c.setPaintFlags(holder.c.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.c.setChecked(true);
            } else {
                holder.c.setPaintFlags(holder.c.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.c.setChecked(false);
            }
            holder.c.setText(items.get(position).getName());

            holder.c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    CheckBox c2 = (CheckBox) view1;
                    if (c2.isChecked()) {
                        String query = "UPDATE " + ChecklistEntry.TABLE_NAME +
                                " SET " + ChecklistEntry.COLUMN_NAME_ISDONE + " = 1 WHERE " +
                                ChecklistEntry.COLUMN_NAME_ID + " IS " + items.get(position).getId();

                        db.execSQL(query);
                        Log.e("EXECUTED : ", query);
                    } else {
                        String query = "UPDATE " + ChecklistEntry.TABLE_NAME +
                                " SET " + ChecklistEntry.COLUMN_NAME_ISDONE + " = 0 WHERE " +
                                ChecklistEntry.COLUMN_NAME_ID + " IS " + items.get(position).getId();
                        db.execSQL(query);
                        Log.e("EXECUTED : ", query);
                    }
                    refresh();
                }
            });
            return vi;
        }
    }
}
