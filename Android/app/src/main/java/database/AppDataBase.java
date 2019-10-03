package database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import android.content.Context;

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


    public abstract CityDao cityDao();
    public abstract WidgetCheckListDao widgetCheckListDao();

    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    "city-travel-mate-db")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build();
        }
        return instance;
    }

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
}

