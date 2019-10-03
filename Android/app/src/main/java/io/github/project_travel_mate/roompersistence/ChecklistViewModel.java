package io.github.project_travel_mate.roompersistence;

import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public Flowable<List<ChecklistItem>> getPendingItems() {
        return mDataSource.getPendingItems();
    }

    public Flowable<List<ChecklistItem>> getFinishedItems() {
        return mDataSource.getFinishedItems();
    }

    public int getMaxPosition() throws InterruptedException, ExecutionException {
        Callable<Integer> callable = mDataSource::getMaxPosition;

        Future<Integer> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    public Completable updateName(final String name, final int id) {
        return Completable.fromAction(() -> mDataSource.updateName(name, id));
    }

    public Completable updateIsDone(final int id, final boolean done) {
        return Completable.fromAction(() -> mDataSource.updateIsDone(id, done));
    }

    public Completable updatePositions(final int pos) {
        return Completable.fromAction(() -> mDataSource.updatePositions(pos));
    }

    public Completable movePositions(final int from, final int to) {
        return Completable.fromAction(() -> mDataSource.movePositions(from, to));
    }

    public Completable insertItem(ChecklistItem item) {
        return Completable.fromAction(() -> mDataSource.insertItem(item));
    }

    public Completable deleteItem(ChecklistItem item) {
        return Completable.fromAction(() -> mDataSource.deleteItem(item));
    }

    public Completable deleteCompletedTasks() {
        return Completable.fromAction(mDataSource::deleteCompletedTasks);
    }

    public Single<List<ChecklistItem>> getCompletedItems() {
        return mDataSource.getCompletedItems();

    }
}
