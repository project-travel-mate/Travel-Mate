package io.github.project_travel_mate.destinations.offlinedata;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Ajay Deepak on 29-10-2018.
 */

@Dao
public interface OfflineDataDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMap(OfflineData maps);

    @Query("SELECT * FROM offline_maps ORDER BY created_date ASC")
    Flowable<List<OfflineData>> getMaps();

    @Query("DELETE FROM offline_maps")
    void deleteMaps();

}
