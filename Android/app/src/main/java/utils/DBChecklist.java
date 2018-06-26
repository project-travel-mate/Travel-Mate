package utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import objects.ChecklistEntry;

/**
 * Created by Swati garg on 30-06-2015.
 * <p>
 * Helper class for database operations on checklist database
 */
public class DBChecklist extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TravelGuide.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ChecklistEntry.TABLE_NAME + " (" +
                    ChecklistEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" + COMMA_SEP +
                    ChecklistEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ChecklistEntry.COLUMN_NAME_ISDONE
                    + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ChecklistEntry.TABLE_NAME;

    public DBChecklist(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}