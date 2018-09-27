package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import dao.CityDao;
import dao.WidgetCheckListDao;
import objects.ChecklistItem;
import objects.City;

/**
 * Created by Santosh on 05/09/18.
 */

@Database(entities = {City.class, ChecklistItem.class}, version = 1, exportSchema = false)
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
                    .build();
        }
        return instance;
    }
}

