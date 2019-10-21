package database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import dao.CityDao;
import dao.CityDao_Impl;
import dao.WidgetCheckListDao;
import dao.WidgetCheckListDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class AppDataBase_Impl extends AppDataBase {
  private volatile CityDao _cityDao;

  private volatile WidgetCheckListDao _widgetCheckListDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(3) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `city` (`mId` TEXT NOT NULL, `city_latitude` TEXT, `city_longitude` TEXT, `city_funfact` INTEGER NOT NULL, `city_background` INTEGER NOT NULL, `city_favourite` INTEGER NOT NULL, `city_avatar` TEXT, `city_nickname` TEXT, `city_description` TEXT, PRIMARY KEY(`mId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `events_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `isDone` TEXT NOT NULL, `position` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"0fe7294c7f8eb50de91b851dbfd0a48c\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `city`");
        _db.execSQL("DROP TABLE IF EXISTS `events_new`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCity = new HashMap<String, TableInfo.Column>(9);
        _columnsCity.put("mId", new TableInfo.Column("mId", "TEXT", true, 1));
        _columnsCity.put("city_latitude", new TableInfo.Column("city_latitude", "TEXT", false, 0));
        _columnsCity.put("city_longitude", new TableInfo.Column("city_longitude", "TEXT", false, 0));
        _columnsCity.put("city_funfact", new TableInfo.Column("city_funfact", "INTEGER", true, 0));
        _columnsCity.put("city_background", new TableInfo.Column("city_background", "INTEGER", true, 0));
        _columnsCity.put("city_favourite", new TableInfo.Column("city_favourite", "INTEGER", true, 0));
        _columnsCity.put("city_avatar", new TableInfo.Column("city_avatar", "TEXT", false, 0));
        _columnsCity.put("city_nickname", new TableInfo.Column("city_nickname", "TEXT", false, 0));
        _columnsCity.put("city_description", new TableInfo.Column("city_description", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCity = new TableInfo("city", _columnsCity, _foreignKeysCity, _indicesCity);
        final TableInfo _existingCity = TableInfo.read(_db, "city");
        if (! _infoCity.equals(_existingCity)) {
          throw new IllegalStateException("Migration didn't properly handle city(objects.City).\n"
                  + " Expected:\n" + _infoCity + "\n"
                  + " Found:\n" + _existingCity);
        }
        final HashMap<String, TableInfo.Column> _columnsEventsNew = new HashMap<String, TableInfo.Column>(4);
        _columnsEventsNew.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsEventsNew.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsEventsNew.put("isDone", new TableInfo.Column("isDone", "TEXT", true, 0));
        _columnsEventsNew.put("position", new TableInfo.Column("position", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEventsNew = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEventsNew = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEventsNew = new TableInfo("events_new", _columnsEventsNew, _foreignKeysEventsNew, _indicesEventsNew);
        final TableInfo _existingEventsNew = TableInfo.read(_db, "events_new");
        if (! _infoEventsNew.equals(_existingEventsNew)) {
          throw new IllegalStateException("Migration didn't properly handle events_new(objects.ChecklistItem).\n"
                  + " Expected:\n" + _infoEventsNew + "\n"
                  + " Found:\n" + _existingEventsNew);
        }
      }
    }, "0fe7294c7f8eb50de91b851dbfd0a48c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "city","events_new");
  }

  @Override
  public CityDao cityDao() {
    if (_cityDao != null) {
      return _cityDao;
    } else {
      synchronized(this) {
        if(_cityDao == null) {
          _cityDao = new CityDao_Impl(this);
        }
        return _cityDao;
      }
    }
  }

  @Override
  public WidgetCheckListDao widgetCheckListDao() {
    if (_widgetCheckListDao != null) {
      return _widgetCheckListDao;
    } else {
      synchronized(this) {
        if(_widgetCheckListDao == null) {
          _widgetCheckListDao = new WidgetCheckListDao_Impl(this);
        }
        return _widgetCheckListDao;
      }
    }
  }
}
