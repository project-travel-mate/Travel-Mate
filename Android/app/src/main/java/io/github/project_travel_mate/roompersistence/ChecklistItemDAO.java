package io.github.project_travel_mate.roompersistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import objects.ChecklistItem;

/**
 * For various operations on database
 * Contains various queries
 */
@Dao
public interface ChecklistItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(ChecklistItem items);

    @Query("SELECT * FROM events_new ORDER BY isDone")
    Flowable<List<ChecklistItem>> getSortedItems();

    @Query("UPDATE events_new SET isDone = 1 WHERE id IS :id")
    void updateIsDone(int id);

    @Query("UPDATE events_new SET isDone = 0 WHERE id IS :id")
    void updateUndone(int id);

}
