package adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.rey.material.widget.CheckBox;

import java.util.List;

import database.DBhelp_new;
import database.TableEntry_new;
import tie.hackathon.travelguide.R;

/**
 * Created by swati on 9/10/15.
 */
public class CheckList_adapter extends ArrayAdapter<String> {


    private final Activity context;
    private final List<String> task, id, isdone;
    DBhelp_new dbhelp;
    SQLiteDatabase db;


    public CheckList_adapter(Activity context, List<String> i, List<String> t, List<String> d) {
        super(context, R.layout.checklist_item, t);
        this.context = context;
        task = t;
        id = i;
        isdone = d;
    }

    CheckBox c;

    CheckBox bi;

    @Override
    public View getView(final int position, View view2, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.checklist_item, null, true);
        c = (CheckBox) view.findViewById(R.id.cb1);

        dbhelp = new DBhelp_new(context);
        db = dbhelp.getWritableDatabase();

        if (isdone.get(position).equals("1")) {
            c.setPaintFlags(c.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            c.setChecked(true);
        } else {
            c.setPaintFlags(c.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            c.setChecked(false);
        }
        c.setText(task.get(position));

        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    c.setPaintFlags(c.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                    String x = "UPDATE " + TableEntry_new.TABLE_NAME + " SET " + TableEntry_new.COLUMN_NAME_ISDONE + " = 1 WHERE " +
                            TableEntry_new.COLUMN_NAME_ID + " IS " + id.get(position);

                    db.execSQL(x);
                    Log.e("execited", x + " ");
                } else {
                    c.setPaintFlags(c.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));


                    db.execSQL("UPDATE " + TableEntry_new.TABLE_NAME + " SET " + TableEntry_new.COLUMN_NAME_ISDONE + " = 0 WHERE " +
                            TableEntry_new.COLUMN_NAME_ID + " IS " + id.get(position));
                }
            }
        });

        return view;
    }

}
