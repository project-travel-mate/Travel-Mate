package io.github.project_travel_mate.destinations.offlinedata;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Ajay Deepak on 29-10-2018.
 *
 * Handles data for UI
 */
public class OfflineDataViewmodel extends ViewModel {

    private OfflineDataSource mMapDataSource;

    public OfflineDataViewmodel(OfflineDataSource mDataSource) {
        this.mMapDataSource = mDataSource;
    }

    public Flowable<List<OfflineData>> getMaps(OfflineData map) {
        return mMapDataSource.getMaps();
    }

    public  void insertItem(OfflineData map) {
        mMapDataSource.insertMap(map);
    }

    public void deleteMap() {
        mMapDataSource.deleteMap();
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     * <p>
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
