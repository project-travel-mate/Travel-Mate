package tie.hackathon.travelguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import adapters.CheckList_adapter;
import database.DBhelp_new;
import database.TableEntry_new;


public class CheckList_fragment extends Fragment {


    CheckList_adapter ad;
    static Activity activity;
    List<String> id = new ArrayList<>();
    List<String> task = new ArrayList<>();
    List<String> isdone = new ArrayList<>();
    DBhelp_new dbhelp;
    SQLiteDatabase db;

    List<String> base_task = new ArrayList<>();

    public CheckList_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.content_check_list, container, false);

        dbhelp = new DBhelp_new(getContext());
        db = dbhelp.getWritableDatabase();


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor e = s.edit();
        String x = s.getString(Constants.ID_ADDED_INDB,"null");
        if(x.equals("null")){
            base_task.add("Bags");
            base_task.add("Keys");
            base_task.add("Charger");
            base_task.add("Earphones");
            base_task.add("Clothes");
            base_task.add("Food");
            base_task.add("Tickets");
            for (int i = 0 ; i<base_task.size();i++) {

                ContentValues insertValues = new ContentValues();
                insertValues.put(TableEntry_new.COLUMN_NAME, base_task.get(i));
                insertValues.put(TableEntry_new.COLUMN_NAME_ISDONE, "0");
                db.insert(TableEntry_new.TABLE_NAME, null, insertValues);
            }

            e.putString(Constants.ID_ADDED_INDB,"yes");
            e.commit();
        }





        ListView lv = (ListView) v.findViewById(R.id.lv);
        ad = new CheckList_adapter(activity, id, task, isdone);
        lv.setAdapter(ad);

        refresh();


        LinearLayout l = (LinearLayout) v.findViewById(R.id.add);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = (activity).getLayoutInflater();
                builder.setTitle("Add new item");
                builder.setCancelable(false);
                final View dialogv = inflater.inflate(R.layout.dialog, null);
                builder.setView(dialogv)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                EditText e = (EditText) dialogv.findViewById(R.id.task);
                                Log.e("added", "a"+e.getText() + "a");

                                if (!e.getText().toString().equals("")) {
                                    ContentValues insertValues = new ContentValues();
                                    insertValues.put(TableEntry_new.COLUMN_NAME, e.getText().toString());
                                    insertValues.put(TableEntry_new.COLUMN_NAME_ISDONE, "0");
                                    db.insert(TableEntry_new.TABLE_NAME, null, insertValues);
                                    refresh();
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        return v;
    }


    public void refresh() {
        id.clear();
        task.clear();
        isdone.clear();


        Cursor c = db.rawQuery("SELECT * FROM " + TableEntry_new.TABLE_NAME + " ORDER BY " +
                TableEntry_new.COLUMN_NAME_ISDONE, null);
        if (c.moveToFirst()) {

            do {

                id.add(c.getString(c.getColumnIndex(TableEntry_new.COLUMN_NAME_ID)));
                task.add(c.getString(c.getColumnIndex(TableEntry_new.COLUMN_NAME)));
                isdone.add(c.getString(c.getColumnIndex(TableEntry_new.COLUMN_NAME_ISDONE)));

            } while (c.moveToNext());
        }

        ad.notifyDataSetChanged();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


}
