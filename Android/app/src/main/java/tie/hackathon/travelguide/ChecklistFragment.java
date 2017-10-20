package tie.hackathon.travelguide;

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

import utils.Constants;
import utils.DBhelp;
import utils.TableEntry;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChecklistFragment extends Fragment {


    private final List<String> id       = new ArrayList<>();
    private final List<String> task     = new ArrayList<>();
    private final List<String> isdone   = new ArrayList<>();

    private CheckList_adapter   ad;
    private Activity            activity;
    private DBhelp              dbhelp;
    private SharedPreferences   s;
    private SharedPreferences.Editor e;
    private SQLiteDatabase db;
    private final List<String> base_task = new ArrayList<>();
    @BindView(R.id.lv) ListView lv;

    public ChecklistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_check_list, container, false);

        dbhelp = new DBhelp(getContext());
        db = dbhelp.getWritableDatabase();

        ButterKnife.bind(this,v);

        s = PreferenceManager.getDefaultSharedPreferences(activity);
        e = s.edit();


        //First time uers
        String x = s.getString(Constants.ID_ADDED_INDB, "null");
        if (x.equals("null")) {
            base_task.add("Bags");
            base_task.add("Keys");
            base_task.add("Charger");
            base_task.add("Earphones");
            base_task.add("Clothes");
            base_task.add("Food");
            base_task.add("Tickets");
            for (int i = 0; i < base_task.size(); i++) {
                ContentValues insertValues = new ContentValues();
                insertValues.put(TableEntry.COLUMN_NAME, base_task.get(i));
                insertValues.put(TableEntry.COLUMN_NAME_ISDONE, "0");
                db.insert(TableEntry.TABLE_NAME, null, insertValues);
            }

            e.putString(Constants.ID_ADDED_INDB, "yes");
            e.apply();
        }

        ad = new CheckList_adapter(activity, id, task, isdone);
        lv.setAdapter(ad);

        refresh();

        return v;
    }

    @OnClick(R.id.add) void onClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (activity).getLayoutInflater();
        builder.setTitle("Add new item");
        builder.setCancelable(false);
        final View dialogv = inflater.inflate(R.layout.dialog, null);
        builder.setView(dialogv)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id1) {

                        EditText e1 = (EditText) dialogv.findViewById(R.id.task);
                        Log.e("added", "a" + e1.getText() + "a");

                        if (!e1.getText().toString().equals("")) {
                            ContentValues insertValues = new ContentValues();
                            insertValues.put(TableEntry.COLUMN_NAME, e1.getText().toString());
                            insertValues.put(TableEntry.COLUMN_NAME_ISDONE, "0");
                            db.insert(TableEntry.TABLE_NAME, null, insertValues);
                            ChecklistFragment.this.refresh();
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    private void refresh() {
        id.clear();
        task.clear();
        isdone.clear();
        ad.notifyDataSetChanged();

        Cursor c = db.rawQuery("SELECT * FROM " + TableEntry.TABLE_NAME + " ORDER BY " +
                TableEntry.COLUMN_NAME_ISDONE, null);
        if (c.moveToFirst()) {
            do {
                id.add(c.getString(c.getColumnIndex(TableEntry.COLUMN_NAME_ID)));
                task.add(c.getString(c.getColumnIndex(TableEntry.COLUMN_NAME)));
                isdone.add(c.getString(c.getColumnIndex(TableEntry.COLUMN_NAME_ISDONE)));
            } while (c.moveToNext());
        }
        c.close();
        ad.notifyDataSetChanged();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public class CheckList_adapter extends ArrayAdapter<String> {

        private final Activity context;
        private final List<String> task, id, isdone;
        DBhelp dbhelp;
        SQLiteDatabase db;

        CheckList_adapter(Activity context, List<String> i, List<String> t, List<String> d) {
            super(context, R.layout.checklist_item, t);
            this.context = context;
            task = t;
            id = i;
            isdone = d;
        }

        class ViewHolder {
            CheckBox c;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View vi = view;             //trying to reuse a recycled view
            ViewHolder holder = null;
            if (vi == null) {

                vi = inflater.inflate(R.layout.checklist_item, parent, false);
                holder = new ViewHolder();
                holder.c = (CheckBox) vi.findViewById(R.id.cb1);
                vi.setTag(holder);

            } else {
                holder = (ViewHolder) vi.getTag();
            }

            dbhelp = new DBhelp(context);
            db = dbhelp.getWritableDatabase();

            if (isdone.get(position).equals("1")) {
                holder.c.setPaintFlags(holder.c.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.c.setChecked(true);
            } else {
                holder.c.setPaintFlags(holder.c.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.c.setChecked(false);
            }
            holder.c.setText(task.get(position));

            holder.c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    CheckBox c2 = (CheckBox) view1;
                    if (c2.isChecked()) {

                        String x = "UPDATE " + TableEntry.TABLE_NAME + " SET " + TableEntry.COLUMN_NAME_ISDONE + " = 1 WHERE " +
                                TableEntry.COLUMN_NAME_ID + " IS " + id.get(position);

                        db.execSQL(x);
                        Log.e("execited", x + " ");
                    } else {
                        String x = "UPDATE " + TableEntry.TABLE_NAME + " SET " + TableEntry.COLUMN_NAME_ISDONE + " = 0 WHERE " +
                                TableEntry.COLUMN_NAME_ID + " IS " + id.get(position);
                        db.execSQL(x);
                        Log.e("execited", x + " ");
                    }
                    refresh();
                }
            });
            return vi;
        }
    }
}
