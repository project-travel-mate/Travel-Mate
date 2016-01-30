package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Swati garg on 30-06-2015.
 */
public class DBhelp_new extends SQLiteOpenHelper {
    
    
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TravelGuide.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
    
            "CREATE TABLE " + TableEntry_new.TABLE_NAME + " (" +
                    TableEntry_new.COLUMN_NAME_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"+ COMMA_SEP +
                    TableEntry_new.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    TableEntry_new.COLUMN_NAME_ISDONE
                    + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TableEntry_new.TABLE_NAME;

    public DBhelp_new(Context context) {
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