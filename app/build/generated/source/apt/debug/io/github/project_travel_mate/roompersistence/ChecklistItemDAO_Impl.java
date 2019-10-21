package io.github.project_travel_mate.roompersistence;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EmptyResultSetException;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.RxRoom;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import objects.ChecklistItem;

public class ChecklistItemDAO_Impl implements ChecklistItemDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfChecklistItem;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfChecklistItem;

  private final SharedSQLiteStatement __preparedStmtOfUpdateName;

  private final SharedSQLiteStatement __preparedStmtOfUpdateIsDone;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePositions;

  private final SharedSQLiteStatement __preparedStmtOfMoveItemDown;

  private final SharedSQLiteStatement __preparedStmtOfMoveItemUp;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCompletedTasks;

  public ChecklistItemDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChecklistItem = new EntityInsertionAdapter<ChecklistItem>(__db) {
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
    this.__preparedStmtOfUpdatePositions = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE events_new SET position = position - 1 WHERE position > ?";
        return _query;
      }
    };
    this.__preparedStmtOfMoveItemDown = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE events_new SET position =(CASE WHEN position = ? THEN ? ELSE position-1 END)WHERE position BETWEEN ? AND ?";
        return _query;
      }
    };
    this.__preparedStmtOfMoveItemUp = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE events_new SET position =(CASE WHEN position = ? THEN ? ELSE position+1 END)WHERE position BETWEEN ? AND ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCompletedTasks = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM events_new WHERE isDone = 1";
        return _query;
      }
    };
  }

  @Override
  public void insertItem(ChecklistItem item) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfChecklistItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteItem(ChecklistItem item) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfChecklistItem.handle(item);
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
      final int _tmp;
      _tmp = done ? 1 : 0;
      _stmt.bindLong(_argIndex, _tmp);
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
  public void updatePositions(int pos) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePositions.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      _stmt.bindLong(_argIndex, pos);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdatePositions.release(_stmt);
    }
  }

  @Override
  public void moveItemDown(int from, int to) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfMoveItemDown.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      _stmt.bindLong(_argIndex, from);
      _argIndex = 2;
      _stmt.bindLong(_argIndex, to);
      _argIndex = 3;
      _stmt.bindLong(_argIndex, from);
      _argIndex = 4;
      _stmt.bindLong(_argIndex, to);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfMoveItemDown.release(_stmt);
    }
  }

  @Override
  public void moveItemUp(int from, int to) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfMoveItemUp.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      _stmt.bindLong(_argIndex, from);
      _argIndex = 2;
      _stmt.bindLong(_argIndex, to);
      _argIndex = 3;
      _stmt.bindLong(_argIndex, to);
      _argIndex = 4;
      _stmt.bindLong(_argIndex, from);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfMoveItemUp.release(_stmt);
    }
  }

  @Override
  public void deleteCompletedTasks() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCompletedTasks.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteCompletedTasks.release(_stmt);
    }
  }

  @Override
  public Flowable<List<ChecklistItem>> getSortedItems() {
    final String _sql = "SELECT * FROM events_new ORDER BY isDone";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, new String[]{"events_new"}, new Callable<List<ChecklistItem>>() {
      public List<ChecklistItem> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPosition = _cursor.getColumnIndexOrThrow("position");
          final List<ChecklistItem> _result = new ArrayList<ChecklistItem>(_cursor.getCount());
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
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<ChecklistItem>> getPendingItems() {
    final String _sql = "SELECT * FROM events_new WHERE isDone = 0 ORDER BY position";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, new String[]{"events_new"}, new Callable<List<ChecklistItem>>() {
      public List<ChecklistItem> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPosition = _cursor.getColumnIndexOrThrow("position");
          final List<ChecklistItem> _result = new ArrayList<ChecklistItem>(_cursor.getCount());
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
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<ChecklistItem>> getFinishedItems() {
    final String _sql = "SELECT * FROM events_new WHERE isDone = 1 ORDER BY position";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, new String[]{"events_new"}, new Callable<List<ChecklistItem>>() {
      public List<ChecklistItem> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPosition = _cursor.getColumnIndexOrThrow("position");
          final List<ChecklistItem> _result = new ArrayList<ChecklistItem>(_cursor.getCount());
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
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int getMaxPosition() {
    final String _sql = "SELECT max(position) FROM events_new";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Single<List<ChecklistItem>> getCompletedItems() {
    final String _sql = "SELECT * FROM events_new WHERE isDone = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return Single.fromCallable(new Callable<List<ChecklistItem>>() {
      public List<ChecklistItem> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfMName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfMIsDone = _cursor.getColumnIndexOrThrow("isDone");
          final int _cursorIndexOfMPosition = _cursor.getColumnIndexOrThrow("position");
          final java.util.List<objects.ChecklistItem> _result = new java.util.ArrayList<objects.ChecklistItem>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final objects.ChecklistItem _item;
            final java.lang.String _tmpMName;
            _tmpMName = _cursor.getString(_cursorIndexOfMName);
            final boolean _tmpMIsDone;
            final java.lang.String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMIsDone);
            _tmpMIsDone = io.github.project_travel_mate.roompersistence.BooleanConverter.boolFromString(_tmp);
            final int _tmpMPosition;
            _tmpMPosition = _cursor.getInt(_cursorIndexOfMPosition);
            _item = new objects.ChecklistItem(_tmpMName,_tmpMIsDone,_tmpMPosition);
            final int _tmpMId;
            _tmpMId = _cursor.getInt(_cursorIndexOfMId);
            _item.setId(_tmpMId);
            _result.add(_item);
          }
          if(_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    });
  }
}
