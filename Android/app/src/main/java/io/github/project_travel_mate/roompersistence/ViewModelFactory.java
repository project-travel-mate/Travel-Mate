package io.github.project_travel_mate.roompersistence;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

/**
 * Factory for ViewModels
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final ChecklistDataSource mDataSource;

    public ViewModelFactory(ChecklistDataSource dataSource) {
        mDataSource = dataSource;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChecklistViewModel.class)) {
            return (T) new ChecklistViewModel(mDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

