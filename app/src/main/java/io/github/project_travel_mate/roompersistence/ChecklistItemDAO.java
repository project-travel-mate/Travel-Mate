package io.github.project_travel_mate.roompersistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import objects.ChecklistItem;

/**
 * For various operations on database
 * Contains various queries
 */
@Dao
public interface ChecklistItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(ChecklistItem item);

    @Query("SELECT * FROM events_new ORDER BY isDone")
    Flowable<List<ChecklistItem>> getSortedItems();

    @Query("SELECT * FROM events_new WHERE isDone = 0 ORDER BY position")
    Flowable<List<ChecklistItem>> getPendingItems();

    @Query("SELECT * FROM events_new WHERE isDone = 1 ORDER BY position")
    Flowable<List<ChecklistItem>> getFinishedItems();

    @Query("SELECT max(position) FROM events_new")
    int getMaxPosition();

    @Query("UPDATE events_new SET name = :name WHERE id IS :id")
    void updateName(String name, int id);

    @Query("UPDATE events_new SET isDone = :done WHERE id IS :id")
    void updateIsDone(int id, boolean done);

    @Query("UPDATE events_new SET position = position - 1 WHERE position > :pos")
    void updatePositions(int pos);

    @Query("UPDATE events_new SET position =" +
            "(CASE WHEN position = :from THEN :to " +
            "ELSE position-1 END)" +
            "WHERE position BETWEEN :from AND :to")
    void moveItemDown(int from, int to);

    @Query("UPDATE events_new SET position =" +
            "(CASE WHEN position = :from THEN :to " +
            "ELSE position+1 END)" +
            "WHERE position BETWEEN :to AND :from")
    void moveItemUp(int from, int to);

    @Delete
    void deleteItem(ChecklistItem item);

    @Query("DELETE FROM events_new WHERE isDone = 1")
    void deleteCompletedTasks();

    @Query("SELECT * FROM events_new WHERE isDone = 1")
    Single<List<ChecklistItem>> getCompletedItems();

}
