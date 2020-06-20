package database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import dao.CityDao;
import dao.WidgetCheckListDao;
import objects.ChecklistItem;
import objects.City;

/**
 * Created by Santosh on 05/09/18.
 */

@Database(entities = {City.class, ChecklistItem.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;

    private static final String OLD_DATABASE_NAME = "city-travel-mate-db";
    private static final String DATABASE_BASE_NAME = "city-travel-mate-db";
    private static final String DATABASE_DELIMITER = "-";
    private static final String DATABASE_EXT = ".db";

    public abstract CityDao cityDao();
    public abstract WidgetCheckListDao widgetCheckListDao();

    public static AppDataBase getAppDatabase(Context context, String userId) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, AppDataBase.getDatabaseName(userId))
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build();

//            new MoveChecklistTask().execute(context, instance);
            AppDataBase.migrateChecklistToNewDB(context, instance);
        }
        return instance;
    }

    private static String getDatabaseName(String userId) {
        return AppDataBase.DATABASE_BASE_NAME + AppDataBase.DATABASE_DELIMITER + userId
                + AppDataBase.DATABASE_EXT;
    }

    private static void migrateChecklistToNewDB(Context context, AppDataBase instance) {
        AppDataBase oldDatabase = Room.databaseBuilder(context.getApplicationContext(),
                AppDataBase.class, AppDataBase.OLD_DATABASE_NAME)
                .addMigrations(AppDataBase.MIGRATION_1_2, AppDataBase.MIGRATION_2_3, AppDataBase.MIGRATION_3_4)
                .build();

        WidgetCheckListDao oldWidgetCheckListDao = oldDatabase.widgetCheckListDao();
        ChecklistItem[] oldChecklistItems = oldWidgetCheckListDao.loadAll();

        WidgetCheckListDao newWidgetCheckListDao = instance.widgetCheckListDao();
        List<ChecklistItem> oldChecklistItemsList = Arrays.asList(oldChecklistItems);
        newWidgetCheckListDao.insertAll(oldChecklistItemsList);
    }

    // migration from database version 2 to 3
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
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

    //migration from database version 2 to 3
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add column to the table
            database.execSQL("ALTER TABLE city ADD city_favourite INTEGER DEFAULT 0 NOT NULL");
        }
    };

    //migration from database version 1 t o2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
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

    private static class MoveChecklistTask extends AsyncTask {
        protected Object doInBackground(Object... objects) {
            Context context = (Context) objects[0];
            AppDataBase sInstance = (AppDataBase) objects[1];
            AppDataBase oldDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, AppDataBase.OLD_DATABASE_NAME)
                    .addMigrations(AppDataBase.MIGRATION_1_2, AppDataBase.MIGRATION_2_3, AppDataBase.MIGRATION_3_4)
                    .build();

            WidgetCheckListDao oldWidgetCheckListDao = oldDatabase.widgetCheckListDao();
            ChecklistItem[] oldChecklistItems = oldWidgetCheckListDao.loadAll();

            WidgetCheckListDao newWidgetCheckListDao = sInstance.widgetCheckListDao();
            List<ChecklistItem> oldChecklistItemsList = Arrays.asList(oldChecklistItems);
            newWidgetCheckListDao.insertAll(oldChecklistItemsList);
            return null;
        }
    };
}




