package io.github.project_travel_mate.roompersistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = objects.ChecklistItem.class, version = 4, exportSchema = false)
public abstract class   DbChecklist extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "TravelMate.db";
    private static DbChecklist sInstance;

    public static DbChecklist getsInstance(Context context) {
        //to make sure that Singleton Pattern is followed
        //i.e. only one object of the class is created.
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DbChecklist.class, DbChecklist.DATABASE_NAME)
                        .addMigrations(MIGRATION_3_4)
                        .build();
            }
        }
        return sInstance;
    }


    //migration from database version 3 to 4
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE checklist_items (id INTEGER PRIMARY KEY NOT NULL, name TEXT," +
                            " isDone TEXT)");
            // Copy the data
            database.execSQL(
                    "INSERT INTO checklist_items (id, name, isDone) " +
                            "SELECT id, name, isDone FROM events_new");
            // Remove the old table
            database.execSQL("DROP TABLE events_new");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE checklist_items RENAME TO events_new");
        }
    };

    public abstract ChecklistItemDAO checklistItemDAO();

}
