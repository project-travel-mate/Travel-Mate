package io.github.project_travel_mate.roompersistence;

import android.arch.lifecycle.ViewModel;

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

    public Flowable<List<ChecklistItem>> getSortedItems(int userId) {
        return mDataSource.getSortedItems(userId);
    }

    public Flowable<List<ChecklistItem>> getPendingItems(int userId) {
        return mDataSource.getPendingItems(userId);
    }

    public Flowable<List<ChecklistItem>> getFinishedItems(int userId) {
        return mDataSource.getFinishedItems(userId);
    }

    public int getMaxPosition(int userId) throws InterruptedException, ExecutionException {
        Callable<Integer> callable = () -> mDataSource.getMaxPosition(userId);

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

    public Completable deleteCompletedTasks(int userId) {
        return Completable.fromAction(() -> mDataSource.deleteCompletedTasks(userId));
    }

    public Single<List<ChecklistItem>> getCompletedItems(int userId) {
        return mDataSource.getCompletedItems(userId);

    }
}
