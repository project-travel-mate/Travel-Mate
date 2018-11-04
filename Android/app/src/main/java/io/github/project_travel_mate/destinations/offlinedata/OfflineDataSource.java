package io.github.project_travel_mate.destinations.offlinedata;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Ajay Deepak on 29-10-2018.
 *
 * Class to access data items of OfflineData from Room db.
 */
public class OfflineDataSource {

    private final OfflineDataDAO mOfflineDataDAO;

    public OfflineDataSource(OfflineDataDAO dao) {
        this.mOfflineDataDAO = dao;
    }

    public Flowable<List<OfflineData>> getMaps() {
        return mOfflineDataDAO.getMaps();
    }

    public void insertMap(OfflineData map) {
        mOfflineDataDAO.insertMap(map);
    }

    public void deleteMap() {
        mOfflineDataDAO.deleteMaps();
    }




}
