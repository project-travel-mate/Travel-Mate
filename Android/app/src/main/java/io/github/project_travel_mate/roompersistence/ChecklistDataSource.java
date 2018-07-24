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
        return mDao.getSortedItems();
    }

    void updateIsDone(int id) {
        mDao.updateIsDone(id);
    }

    void updateUndone(int id) {
        mDao.updateUndone(id);
    }

    void insertItem(ChecklistItem item) {
        mDao.insertItems(item);
    }

    void deleteCompletedTasks() {
        mDao.deleteCompletedTasks();
    }

    Single<List<ChecklistItem>> getCompletedItems() {
        return  mDao.getCompletedItems();
    }
}
