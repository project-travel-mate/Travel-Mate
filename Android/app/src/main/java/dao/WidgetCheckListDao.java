package dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import objects.ChecklistItem;
import objects.City;

/**
 * Created by Santosh on 05/09/18.
 */

@Dao
public interface WidgetCheckListDao {
    @Insert
    void insert(ChecklistItem... checkItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    void insertAll(List<ChecklistItem> checkItem);

    @Query("UPDATE events_new SET isDone = 1 WHERE id IS :id")
    void updateIsDone(int id);

    @Query("UPDATE events_new SET isDone = 0 WHERE id IS :id")
    void updateUndone(int id);

    @Delete
    void delete(ChecklistItem... checkItem);

    @Query("Select * FROM events_new")
    ChecklistItem[] loadAll();

    @Query("DELETE FROM events_new")
    void deleteAll();
}
