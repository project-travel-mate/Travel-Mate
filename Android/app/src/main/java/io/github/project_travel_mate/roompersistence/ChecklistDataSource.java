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

    Flowable<List<ChecklistItem>> getSortedItems(int userId) {
        return mDao.getSortedItems(userId).distinctUntilChanged();
    }

    Flowable<List<ChecklistItem>> getPendingItems(int userId) {
        return mDao.getPendingItems(userId).distinctUntilChanged();
    }

    Flowable<List<ChecklistItem>> getFinishedItems(int userId) {
        return mDao.getFinishedItems(userId).distinctUntilChanged();
    }

    int getMaxPosition(int userId) {
        return mDao.getMaxPosition(userId);
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

    void deleteCompletedTasks(int userId) {
        mDao.deleteCompletedTasks(userId);
    }

    Single<List<ChecklistItem>> getCompletedItems(int userId) {
        return mDao.getCompletedItems(userId);
    }
}
