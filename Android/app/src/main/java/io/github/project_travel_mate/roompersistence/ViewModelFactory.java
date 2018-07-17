package io.github.project_travel_mate.roompersistence;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

/**
 * Factory for ViewModels
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final ChecklistDataSource mDataSource;

    public ViewModelFactory(ChecklistDataSource dataSource) {
        mDataSource = dataSource;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChecklistViewModel.class)) {
            return (T) new ChecklistViewModel(mDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

