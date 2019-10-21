package io.github.project_travel_mate.roompersistence;

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
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class DbChecklist_Impl extends DbChecklist {
  private volatile ChecklistItemDAO _checklistItemDAO;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(5) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `events_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `isDone` TEXT NOT NULL, `position` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"16d140d38d8902c57ec18fb830ecf7bf\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
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
    }, "16d140d38d8902c57ec18fb830ecf7bf");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "events_new");
  }

  @Override
  public ChecklistItemDAO checklistItemDAO() {
    if (_checklistItemDAO != null) {
      return _checklistItemDAO;
    } else {
      synchronized(this) {
        if(_checklistItemDAO == null) {
          _checklistItemDAO = new ChecklistItemDAO_Impl(this);
        }
        return _checklistItemDAO;
      }
    }
  }
}
