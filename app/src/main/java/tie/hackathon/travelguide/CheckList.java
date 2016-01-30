package tie.hackathon.travelguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Util.Constants;
import adapters.CheckList_adapter;
import adapters.SongAdapter;
import database.DBhelp_new;
import database.TableEntry_new;
import objects.MusicController;
import objects.Song;

public class CheckList extends AppCompatActivity {

    CheckList_adapter ad;
    List<String> id = new ArrayList<>();
    List<String> task = new ArrayList<>();
    List<String> isdone = new ArrayList<>();
    DBhelp_new dbhelp;
    SQLiteDatabase db;

    List<String> base_task = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_check_list);

        dbhelp = new DBhelp_new(this);
        db = dbhelp.getWritableDatabase();


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(CheckList.this);
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





        ListView lv = (ListView) findViewById(R.id.lv);
        ad = new CheckList_adapter(CheckList.this, id, task, isdone);
        lv.setAdapter(ad);

        refresh();


        LinearLayout l = (LinearLayout) findViewById(R.id.add);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckList.this);
                LayoutInflater inflater = (CheckList.this).getLayoutInflater();
                builder.setTitle("Add new item");
                builder.setCancelable(false);
                final View dialogv = inflater.inflate(R.layout.dialog, null);
                builder.setView(dialogv)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                EditText e = (EditText) dialogv.findViewById(R.id.task);
                                Log.e("added", "a" + e.getText() + "a");

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
//        setTitle("Check List");

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
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {

            case android.R.id.home :
                finish();
        }
        return super.onOptionsItemSelected(item);
    }



}
