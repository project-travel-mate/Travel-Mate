package dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import io.github.project_travel_mate.roompersistence.BooleanConverter;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import objects.ChecklistItem;

public class WidgetCheckListDao_Impl implements WidgetCheckListDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfChecklistItem;

  private final EntityInsertionAdapter __insertionAdapterOfChecklistItem_1;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfChecklistItem;

  private final SharedSQLiteStatement __preparedStmtOfUpdateName;

  private final SharedSQLiteStatement __preparedStmtOfUpdateIsDone;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public WidgetCheckListDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChecklistItem = new EntityInsertionAdapter<ChecklistItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `events_new`(`id`,`name`,`isDone`,`position`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChecklistItem value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        final String _tmp;
        _tmp = BooleanConverter.boolToString(value.getIsDone());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        stmt.bindLong(4, value.getPosition());
      }
    };
    this.__insertionAdapterOfChecklistItem_1 = new EntityInsertionAdapter<ChecklistItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `events_new`(`id`,`name`,`isDone`,`position`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChecklistItem value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        final String _tmp;
        _tmp = BooleanConverter.boolToString(value.getIsDone());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        stmt.bindLong(4, value.getPosition());
      }
    };
    this.__deletionAdapterOfChecklistItem = new EntityDeletionOrUpdateAdapter<ChecklistItem>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `events_new` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChecklistItem value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__preparedStmtOfUpdateName = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE events_new SET name = ? WHERE id IS ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateIsDone = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE events_new SET isDone = ? WHERE id IS ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM events_new";
        return _query;
      }
    };
  }

  @Override
  public void insert(ChecklistItem... checkItem) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfChecklistItem.insert(checkItem);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(List<ChecklistItem> checkItem) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfChecklistItem_1.insert(checkItem);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(ChecklistItem... checkItem) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfChecklistItem.handleMultiple(checkItem);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateName(String name, int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateName.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (name == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, name);
      }
      _argIndex = 2;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateName.release(_stmt);
    }
  }

  @Override
  public void updateIsDone(int id, boolean done) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateIsDone.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      final String _tmp;
      _tmp = BooleanConverter.boolToString(done);
      if (_tmp == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _tmp);
      }
      _argIndex = 2;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateIsDone.release(_stmt);
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public ChecklistItem[] loadAll() {
    final String _sql = "Select * FROM events_new";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
      final int _cursorIndexOfMPosition = _cursor.getColumnIndexOrThrow("position");
      final ChecklistItem[] _result = new ChecklistItem[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final ChecklistItem _item;
        final String _tmpMName;
        _tmpMName = _cursor.getString(_cursorIndexOfMName);
        final boolean _tmpMIsDone;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfMIsDone);
        _tmpMIsDone = BooleanConverter.boolFromString(_tmp);
        final int _tmpMPosition;
        _tmpMPosition = _cursor.getInt(_cursorIndexOfMPosition);
        _item = new ChecklistItem(_tmpMName,_tmpMIsDone,_tmpMPosition);
        final int _tmpMId;
        _tmpMId = _cursor.getInt(_cursorIndexOfMId);
        _item.setId(_tmpMId);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
