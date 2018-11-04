package io.github.project_travel_mate.roompersistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import io.github.project_travel_mate.destinations.offlinedata.OfflineData;
import io.github.project_travel_mate.destinations.offlinedata.OfflineDataDAO;
import utils.DateConverter;
import utils.GeopointsConverter;


@Database(entities = {objects.ChecklistItem.class, OfflineData.class}, version = 4, exportSchema = false)
@TypeConverters({DateConverter.class, GeopointsConverter.class})
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
                    "CREATE TABLE offline_maps (id INTEGER PRIMARY KEY NOT NULL, geoPoint TEXT," +
                            " zoomIn INTEGER, " + " zoomOut INTEGER, " + " createdDate INTEGER)");
            database.execSQL(
                    "CREATE TABLE checklist_items (id INTEGER PRIMARY KEY NOT NULL, name TEXT," +
                            " isDone TEXT)");
            // Copy the data
            database.execSQL(
                    "INSERT INTO checklist_items (id, name, isDone) " +
                            "SELECT id, name, isDone FROM events_new");
            database.execSQL(
                    "INSERT INTO offline_data (id, geoPoint, zoomIn, zoomOut, createdDate) " +
                            "SELECT id, geoPoint, zoomIn, zoomOut, createdDate FROM offline_maps");
            // Remove the old table
            database.execSQL("DROP TABLE events_new ");
            database.execSQL("DROP TABLE offline_maps ");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE checklist_items RENAME TO events_new");
            database.execSQL("ALTER TABLE offline_data RENAME TO offline_maps");
        }
    };

    //migration from database version 4 to 5
  /*  static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create the new table to be used
            database.execSQL(
                    "CREATE TABLE checklist_items (id INTEGER PRIMARY KEY NOT NULL, name TEXT," +
                            " isDone TEXT NOT NULL, position INTEGER DEFAULT 0 NOT NULL)");

            database.execSQL(
                    "CREATE TABLE offline_routes (id INTEGER PRIMARY KEY NOT NULL, geoPoint TEXT," +
                            " zoomIn INTEGER, " + " zoomOut INTEGER, " + " createdDate INTEGER)");

            // Create a temp table to generate positions
            database.execSQL("CREATE TABLE seq_generator(pos INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id INTEGER)");

            database.execSQL(
                    "INSERT INTO offline_routes (id, geoPoint, zoomIn, zoomOut, createdDate) " +
                            "SELECT id, geoPoint, zoomIn, zoomOut, createdDate FROM offline_maps");

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
*/
    public abstract ChecklistItemDAO checklistItemDAO();
    public abstract OfflineDataDAO offlineDataDAO();

}
