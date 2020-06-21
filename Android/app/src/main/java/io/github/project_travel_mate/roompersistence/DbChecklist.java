package io.github.project_travel_mate.roompersistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.Objects;

import objects.ChecklistItem;

@Database(entities = objects.ChecklistItem.class, version = 6, exportSchema = false)
public abstract class DbChecklist extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String OLD_DATABASE_NAME = "TravelMate.db";
    private static final String DATABASE_BASE_NAME = "TravelMate";
    private static final String DATABASE_DELIMITER = "-";
    private static final String DATABASE_EXT = ".db";
    private static DbChecklist sInstance;

    public static DbChecklist getsInstance(Context context, int userId) {
        //to make sure that Singleton Pattern is followed
        //i.e. only one object of the class is created.
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DbChecklist.class, DbChecklist.getDatabaseName(userId))
                        .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                        .build();

                new MigrateChecklistFromOldDbTask().execute(context, sInstance);
            }
        }
        return sInstance;
    }

    private static String getDatabaseName(int userId) {
        return DbChecklist.DATABASE_BASE_NAME + DbChecklist.DATABASE_DELIMITER + userId
                + DbChecklist.DATABASE_EXT;
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

    //migration from database version 4 to 5
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table to be used
            database.execSQL(
                    "CREATE TABLE checklist_items (id INTEGER PRIMARY KEY NOT NULL, name TEXT," +
                            " isDone TEXT NOT NULL, position INTEGER DEFAULT 0 NOT NULL)");

            // Create a temp table to generate positions
            database.execSQL("CREATE TABLE seq_generator(pos INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id INTEGER)");

            // Copy each of the existing id(s) into this; `pos` generated with 1-indexing
            database.execSQL("INSERT INTO seq_generator (id)" +
                    "SELECT id from events_new");

            // Get old data, JOIN the position column, and insert into new table
            // `pos`-1 is done to achieve 0-indexing
            database.execSQL(
                    "INSERT INTO checklist_items " +
                            "SELECT old.id, old.name, old.isDone, t.pos-1 " +
                            "FROM events_new old JOIN seq_generator t ON old.id = t.id");

            // Remove the temp table
            database.execSQL("DROP TABLE seq_generator");

            // Remove the old table
            database.execSQL("DROP TABLE events_new");

            // Change the table name to the correct one
            database.execSQL("ALTER TABLE checklist_items RENAME TO events_new");
        }
    };

    // migration from database version 5 to 6
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table to be used
            database.execSQL(
                    "CREATE TABLE checklist_items (id INTEGER PRIMARY KEY NOT NULL, name TEXT," +
                            " isDone TEXT NOT NULL, position INTEGER DEFAULT 0 NOT NULL," +
                            " user_id INTEGER DEFAULT 0 NOT NULL)");

            // Remove the old table
            database.execSQL("DROP TABLE events_new");

            // Change the table name to the correct one
            database.execSQL("ALTER TABLE checklist_items RENAME TO events_new");
        }
    };

    public abstract ChecklistItemDAO checklistItemDAO();

    private static class MigrateChecklistFromOldDbTask extends AsyncTask {
        protected Object doInBackground(Object... objects) {
            Context context = (Context) objects[0];
            DbChecklist sInstance = (DbChecklist) objects[1];

            DbChecklist oldDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    DbChecklist.class, DbChecklist.OLD_DATABASE_NAME)
                    .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
                    .build();

            ChecklistItemDAO oldChecklistItemDAO = oldDatabase.checklistItemDAO();
            ChecklistItemDAO newChecklistItemDAO = sInstance.checklistItemDAO();

            List<ChecklistItem> oldChecklistItems = oldChecklistItemDAO.getAll();
            for (ChecklistItem item : oldChecklistItems) {
                newChecklistItemDAO.insertItem(item);
                oldChecklistItemDAO.deleteItem(item);
            }
            return null;
        }
    }

}
