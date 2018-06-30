package utils;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import objects.ChecklistItem;

// For various operations to be performed on database

@Dao
public interface ChecklistItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(ChecklistItem items);

    @Query("SELECT * FROM events_new ORDER BY isDone")
    List<ChecklistItem> getSortedItems();

    @Query("UPDATE events_new SET isDone = 1 WHERE id IS :id")
    void updateIsDone(int id);

    @Query("UPDATE events_new SET isDone = 0 WHERE id IS :id")
    void updateUndone(int id);

    @Update
    void update(ChecklistItem checklistItems);

    @Delete
    void delete(ChecklistItem checklistItem);
}
