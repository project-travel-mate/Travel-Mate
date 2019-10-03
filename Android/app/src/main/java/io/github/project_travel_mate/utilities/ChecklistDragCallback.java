package io.github.project_travel_mate.utilities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

public class ChecklistDragCallback extends ItemTouchHelper.Callback {

    private final ChecklistAdapter mAdapter;
    private int mDraggedFrom = -1;
    private int mDraggedTo = -1;

    public ChecklistDragCallback(ChecklistAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // swipeFlags 0 should disable swiping for all directions
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recycler, RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target) {
        int fromPos = holder.getAdapterPosition();
        int targetPos = target.getAdapterPosition();

        // Adjust for "Add new item" entry at the bottom of the list
        if (targetPos == mAdapter.getItemCount() - 1) targetPos--;

        if (mDraggedFrom == -1) {
            mDraggedFrom = fromPos;
        }

        mDraggedTo = targetPos;

        if (fromPos != targetPos) mAdapter.onItemMove(fromPos, targetPos);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // swipes not allowed
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        // This method is called when the ViewHolder being dragged has been 'dropped'
        // Update database at this point, if positions are valid
        if (mDraggedFrom != -1 && mDraggedTo != -1 && mDraggedFrom != mDraggedTo) {
            mAdapter.saveItemMove(mDraggedFrom, mDraggedTo);
        }

        // reset the variables since drag has ended
        mDraggedFrom = mDraggedTo = -1;
    }
}
