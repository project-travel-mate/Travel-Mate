package dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import io.github.project_travel_mate.roompersistence.BooleanConverter;
import objects.ChecklistItem;

/**
 * Created by Santosh on 05/09/18.
 */

@Dao
@TypeConverters({BooleanConverter.class})
public interface WidgetCheckListDao {
    @Insert
    void insert(ChecklistItem... checkItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    void insertAll(List<ChecklistItem> checkItem);

    @Query("UPDATE events_new SET name = :name WHERE id IS :id")
    void updateName(String name, int id);

    @Query("UPDATE events_new SET isDone = :done WHERE id IS :id")
    void updateIsDone(int id, boolean done);

    @Delete
    void delete(ChecklistItem... checkItem);

    @Query("Select * FROM events_new")
    ChecklistItem[] loadAll();

    @Query("DELETE FROM events_new")
    void deleteAll();
}
