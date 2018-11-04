package io.github.project_travel_mate.destinations.offlinedata;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by Ajay Deepak on 30-10-2018.
 *
 *
 * Factory class to create OfflineDataViewModel
 */
public class OfflineDataViewModelFactory implements ViewModelProvider.Factory {

    private OfflineDataSource mCityMapDataSource;

    @Inject
    public OfflineDataViewModelFactory(OfflineDataSource mapDataSource) {
        mCityMapDataSource = mapDataSource;
    }
    /**
     * Creates a new instance of the given {@code Class}.
     * <p>
     *
     * @param modelClass a {@code Class} whose instance is requested
     * @return a newly created ViewModel
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OfflineDataViewmodel(mCityMapDataSource);
    }
}
