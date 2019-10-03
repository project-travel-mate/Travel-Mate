package io.github.project_travel_mate.utilities;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import objects.ChecklistItem;

public class ChecklistItemDiffCallback extends DiffUtil.Callback {

    private final List<ChecklistItem> mOldList;
    private final List<ChecklistItem> mNewList;

    public ChecklistItemDiffCallback(List<ChecklistItem> oldItemsList, List<ChecklistItem> newItemsList) {
        this.mOldList = oldItemsList;
        this.mNewList = newItemsList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // items are the same if their "id" matches
        return (mOldList.get(oldItemPosition).getId() == mNewList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
    }
}
