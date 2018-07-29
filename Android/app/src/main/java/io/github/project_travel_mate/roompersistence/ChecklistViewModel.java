package io.github.project_travel_mate.roompersistence;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import objects.ChecklistItem;

/**
 * View Model for the {@link io.github.project_travel_mate.utilities.ChecklistFragment}
 */
public class ChecklistViewModel extends ViewModel {


    private final ChecklistDataSource mDataSource;

    public ChecklistViewModel(ChecklistDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<ChecklistItem>> getSortedItems() {
        return mDataSource.getSortedItems();
    }

    public Completable updateIsDone(final int id) {
        return Completable.fromAction(() -> mDataSource.updateIsDone(id));
    }

    public Completable updateUndone(final int id) {
        return Completable.fromAction(() -> mDataSource.updateUndone(id));
    }

    public Completable insertItem(ChecklistItem item) {
        return Completable.fromAction(() -> mDataSource.insertItem(item));
    }

    public Completable deleteCompletedTasks() {
        return Completable.fromAction(mDataSource::deleteCompletedTasks);
    }

    public Single<List<ChecklistItem>> getCompletedItems() {
        return mDataSource.getCompletedItems();

    }
}
