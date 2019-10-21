package io.github.project_travel_mate.roompersistence;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import objects.ChecklistItem;

/**
 * Access point for using ChecklistItem data using
 * room database
 */
public class ChecklistDataSource {

    private final ChecklistItemDAO mDao;

    public ChecklistDataSource(ChecklistItemDAO dao) {
        mDao = dao;
    }

    Flowable<List<ChecklistItem>> getSortedItems() {
        return mDao.getSortedItems().distinctUntilChanged();
    }

    Flowable<List<ChecklistItem>> getPendingItems() {
        return mDao.getPendingItems().distinctUntilChanged();
    }

    Flowable<List<ChecklistItem>> getFinishedItems() {
        return mDao.getFinishedItems().distinctUntilChanged();
    }

    int getMaxPosition() {
        return mDao.getMaxPosition();
    }

    void updateName(String name, int id) {
        mDao.updateName(name, id);
    }

    void updateIsDone(int id, boolean done) {
        mDao.updateIsDone(id, done);
    }

    void updatePositions(int pos) {
        mDao.updatePositions(pos);
    }

    void movePositions(int from, int to) {
        if (from < to) {
            mDao.moveItemDown(from, to);
        } else {
            // should be (from > to) since method not called when equal
            mDao.moveItemUp(from, to);
        }

    }

    void insertItem(ChecklistItem item) {
        mDao.insertItem(item);
    }

    void deleteItem(ChecklistItem item) {
        mDao.deleteItem(item);
    }

    void deleteCompletedTasks() {
        mDao.deleteCompletedTasks();
    }

    Single<List<ChecklistItem>> getCompletedItems() {
        return mDao.getCompletedItems();
    }
}
