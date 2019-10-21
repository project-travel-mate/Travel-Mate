package dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import objects.City;

public class CityDao_Impl implements CityDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCity;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfCity;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfCity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CityDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCity = new EntityInsertionAdapter<City>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `city`(`mId`,`city_latitude`,`city_longitude`,`city_funfact`,`city_background`,`city_favourite`,`city_avatar`,`city_nickname`,`city_description`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, City value) {
        if (value.mId == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.mId);
        }
        if (value.mLatitude == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.mLatitude);
        }
        if (value.mLongitude == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.mLongitude);
        }
        stmt.bindLong(4, value.mFunFactsCount);
        stmt.bindLong(5, value.mBackgroundColor);
        stmt.bindLong(6, value.mFavouriteCity);
        if (value.mAvatar == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.mAvatar);
        }
        if (value.mNickname == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.mNickname);
        }
        if (value.mDescription == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.mDescription);
        }
      }
    };
    this.__deletionAdapterOfCity = new EntityDeletionOrUpdateAdapter<City>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `city` WHERE `mId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, City value) {
        if (value.mId == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.mId);
        }
      }
    };
    this.__updateAdapterOfCity = new EntityDeletionOrUpdateAdapter<City>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `city` SET `mId` = ?,`city_latitude` = ?,`city_longitude` = ?,`city_funfact` = ?,`city_background` = ?,`city_favourite` = ?,`city_avatar` = ?,`city_nickname` = ?,`city_description` = ? WHERE `mId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, City value) {
        if (value.mId == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.mId);
        }
        if (value.mLatitude == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.mLatitude);
        }
        if (value.mLongitude == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.mLongitude);
        }
        stmt.bindLong(4, value.mFunFactsCount);
        stmt.bindLong(5, value.mBackgroundColor);
        stmt.bindLong(6, value.mFavouriteCity);
        if (value.mAvatar == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.mAvatar);
        }
        if (value.mNickname == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.mNickname);
        }
        if (value.mDescription == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.mDescription);
        }
        if (value.mId == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.mId);
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM city";
        return _query;
      }
    };
  }

  @Override
  public void insert(City... city) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfCity.insert(city);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(City... city) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfCity.handleMultiple(city);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(City... city) {
    __db.beginTransaction();
    try {
      __updateAdapterOfCity.handleMultiple(city);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
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
  public City[] loadAll() {
    final String _sql = "Select * FROM city";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("mId");
      final int _cursorIndexOfMLatitude = _cursor.getColumnIndexOrThrow("city_latitude");
      final int _cursorIndexOfMLongitude = _cursor.getColumnIndexOrThrow("city_longitude");
      final int _cursorIndexOfMFunFactsCount = _cursor.getColumnIndexOrThrow("city_funfact");
      final int _cursorIndexOfMBackgroundColor = _cursor.getColumnIndexOrThrow("city_background");
      final int _cursorIndexOfMFavouriteCity = _cursor.getColumnIndexOrThrow("city_favourite");
      final int _cursorIndexOfMAvatar = _cursor.getColumnIndexOrThrow("city_avatar");
      final int _cursorIndexOfMNickname = _cursor.getColumnIndexOrThrow("city_nickname");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("city_description");
      final City[] _result = new City[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final City _item;
        _item = new City();
        _item.mId = _cursor.getString(_cursorIndexOfMId);
        _item.mLatitude = _cursor.getString(_cursorIndexOfMLatitude);
        _item.mLongitude = _cursor.getString(_cursorIndexOfMLongitude);
        _item.mFunFactsCount = _cursor.getInt(_cursorIndexOfMFunFactsCount);
        _item.mBackgroundColor = _cursor.getInt(_cursorIndexOfMBackgroundColor);
        _item.mFavouriteCity = _cursor.getInt(_cursorIndexOfMFavouriteCity);
        _item.mAvatar = _cursor.getString(_cursorIndexOfMAvatar);
        _item.mNickname = _cursor.getString(_cursorIndexOfMNickname);
        _item.mDescription = _cursor.getString(_cursorIndexOfMDescription);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public City[] loadAllFavourite() {
    final String _sql = "Select * FROM city WHERE city_favourite = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMId = _cursor.getColumnIndexOrThrow("mId");
      final int _cursorIndexOfMLatitude = _cursor.getColumnIndexOrThrow("city_latitude");
      final int _cursorIndexOfMLongitude = _cursor.getColumnIndexOrThrow("city_longitude");
      final int _cursorIndexOfMFunFactsCount = _cursor.getColumnIndexOrThrow("city_funfact");
      final int _cursorIndexOfMBackgroundColor = _cursor.getColumnIndexOrThrow("city_background");
      final int _cursorIndexOfMFavouriteCity = _cursor.getColumnIndexOrThrow("city_favourite");
      final int _cursorIndexOfMAvatar = _cursor.getColumnIndexOrThrow("city_avatar");
      final int _cursorIndexOfMNickname = _cursor.getColumnIndexOrThrow("city_nickname");
      final int _cursorIndexOfMDescription = _cursor.getColumnIndexOrThrow("city_description");
      final City[] _result = new City[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final City _item;
        _item = new City();
        _item.mId = _cursor.getString(_cursorIndexOfMId);
        _item.mLatitude = _cursor.getString(_cursorIndexOfMLatitude);
        _item.mLongitude = _cursor.getString(_cursorIndexOfMLongitude);
        _item.mFunFactsCount = _cursor.getInt(_cursorIndexOfMFunFactsCount);
        _item.mBackgroundColor = _cursor.getInt(_cursorIndexOfMBackgroundColor);
        _item.mFavouriteCity = _cursor.getInt(_cursorIndexOfMFavouriteCity);
        _item.mAvatar = _cursor.getString(_cursorIndexOfMAvatar);
        _item.mNickname = _cursor.getString(_cursorIndexOfMNickname);
        _item.mDescription = _cursor.getString(_cursorIndexOfMDescription);
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
