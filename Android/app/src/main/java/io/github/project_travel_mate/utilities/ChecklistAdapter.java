package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTouch;
import database.AppDataBase;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.roompersistence.ChecklistViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import objects.ChecklistItem;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {

    private List<ChecklistItem> mItems;
    private ChecklistViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private AppDataBase mDatabase;
    private ChecklistEventListener mListener;
    // dummy item to display the "Add another item" entry
    private static final ChecklistItem ADD_NEW_ENTRY = new ChecklistItem("", false, -1);
    // if true, this is an adapter for pending / unticked items.
    // Else, it is for finished / ticked items
    private final boolean mCanAddItems;

    public ChecklistAdapter(Activity context, ChecklistViewModel mViewModel, boolean canAddItems) {
        mItems = new ArrayList<>(); // initialize with empty list
        this.mCanAddItems = canAddItems;
        this.mViewModel = mViewModel;
        mDatabase = AppDataBase.getAppDatabase(context);
    }

    public void setEventListener(ChecklistEventListener mListener) {
        this.mListener = mListener;
    }

    // replaces the list of items efficiently using DiffUtil
    void updateChecklist(List<ChecklistItem> items) {
        ChecklistItemDiffCallback diffCallback = new ChecklistItemDiffCallback(mItems, items);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        mItems.clear();
        mItems.addAll(items);
        // this handles all updates, so we don't need manual notifyItem* calls
        diffResult.dispatchUpdatesTo(ChecklistAdapter.this);

        if (mCanAddItems) checkLastItem();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checklist_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mItems.get(position).equals(ADD_NEW_ENTRY)) {
            // Set up the "Add another entry" item
            holder.mPlus.setVisibility(View.VISIBLE);
            holder.mCheckBox.setVisibility(View.GONE);
            holder.mHandle.setVisibility(View.INVISIBLE);
            holder.mText.setText("");
        } else {
            // Set up normal items
            holder.mPlus.setVisibility(View.GONE);
            holder.mCheckBox.setVisibility(View.VISIBLE);
            // no drag and drop for ticked items
            if (mCanAddItems) holder.mHandle.setVisibility(View.VISIBLE);
            else holder.mHandle.setVisibility(View.INVISIBLE);

            holder.mCheckBox.setChecked(mItems.get(position).getIsDone());
            holder.mText.setText(mItems.get(position).getName());

            if (holder.mCheckBox.isChecked()) {
                holder.mText.setPaintFlags(holder.mText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mText.setPaintFlags(holder.mText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    void onItemMove(int fromPosition, int toPosition) {
        // Move the items in the local list, but do not modify db
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    void saveItemMove(int fromPosition, int toPosition) {
        // This method is called when the ViewHolder has been dropped
        // The new positions of items are then persisted in the database

        // Use the position values from the database rather than those of the adapter
        // Keep in mind that the adapter is already in desired order, so old indices do not match
        int fromItemPos = mItems.get(toPosition).getPosition();
        int toItemPos;

        if (fromPosition < toPosition) {
            // moved down
            toItemPos = mItems.get(toPosition - 1).getPosition();
        } else {
            // moved up
            toItemPos = mItems.get(toPosition + 1).getPosition();
        }

        mDisposable.add(mViewModel.movePositions(fromItemPos, toItemPos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    // Checks if last item is ADD_NEW_ENTRY, else adds it to end
    void checkLastItem() {
        // add ADD_NEW_ENTRY to items if empty
        if (mItems.isEmpty()) {
            mItems.add(ADD_NEW_ENTRY);
        } else if (!mItems.get(mItems.size() - 1).equals(ADD_NEW_ENTRY)) {
            // add last item again if missing
            mItems.add(ADD_NEW_ENTRY);
            // need to manually call, since this item is not in Db and hence ignored by DiffUtil
            notifyItemInserted(mItems.size() - 1);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_cb)
        CheckBox mCheckBox;

        @BindView(R.id.row_handle)
        ImageView mHandle;

        @BindView(R.id.row_plus)
        ImageView mPlus;

        @BindView(R.id.row_text)
        EditText mText;

        @BindView(R.id.row_delete)
        ImageView mDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnTouch(R.id.row_handle)
        boolean onTouchHandle(View v, MotionEvent e) {
            if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                mListener.onStartDrag(ViewHolder.this);
            }
            return true;
        }

        @OnClick(R.id.row_delete)
        public void onClickDelete(View v) {

            if (mItems.get(getAdapterPosition()).equals(ADD_NEW_ENTRY)) {
                // first clear the item, and then the focus. Rest should be handled by onFocusChange
                mText.setText("");
                mText.clearFocus();
            } else {
                // deletes item from database
                ChecklistItem currItem = mItems.get(getAdapterPosition());

                // use the database position value rather than adapter value
                int currItemPos = currItem.getPosition();

                mDisposable.add(mViewModel.deleteItem(currItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

                // Update positions of all items below deleted row.
                // DiffUtil will not consider it a change, and skip animations.
                mDisposable.add(mViewModel.updatePositions(currItemPos)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

                mDatabase.widgetCheckListDao().delete(mItems.get(getAdapterPosition()));
            }
        }

        @OnFocusChange(R.id.row_text)
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                // focus gained
                mDelete.setVisibility(View.VISIBLE);

                if (mItems.get(getAdapterPosition()).equals(ADD_NEW_ENTRY)) {
                    mPlus.setVisibility(View.GONE);
                    mCheckBox.setVisibility(View.VISIBLE);
                    mCheckBox.setChecked(false);
                    mHandle.setVisibility(View.VISIBLE);
                }

            } else {
                // focus lost
                mDelete.setVisibility(View.GONE);

                if (getAdapterPosition() != -1) {
                    // adapterPosition is -1 when row has been deleted
                    boolean isNewEntry = mItems.get(getAdapterPosition()).equals(ADD_NEW_ENTRY);
                    boolean isEmpty = mText.getText().toString().isEmpty();

                    if (isNewEntry) {
                        // user was on the "Add entry" item

                        if (isEmpty) {
                            // no new data inserted, revert UI
                            mPlus.setVisibility(View.VISIBLE);
                            mCheckBox.setVisibility(View.GONE);
                            mHandle.setVisibility(View.INVISIBLE);
                            mText.setText("");
                        } else {
                            // new data added, insert entry into database

                            int maxPos = 0; // decide better default value

                            try {
                                maxPos = mViewModel.getMaxPosition();
                            } catch (InterruptedException | ExecutionException e) {
                                Log.i("Adapter", "onFocusChange: Exception in getting maxPos\n" + e.getMessage());
                            }

                            ChecklistItem newItem = new ChecklistItem(
                                    mText.getText().toString(),
                                    false,
                                    maxPos + 1);

                            mDisposable.add(mViewModel.insertItem(newItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe());

                            mDatabase.widgetCheckListDao().insert(newItem);
                        }
                    } else {
                        // user was on an existing entry

                        if (isEmpty) {
                            // the data was deleted; remove item from database
                            mDelete.performClick();
                        } else {

                            String oldName = mItems.get(getAdapterPosition()).getName();
                            String newName = mText.getText().toString();
                            int id = mItems.get(getAdapterPosition()).getId();

                            if (!oldName.equals(newName)) {
                                // update entry if changes made
                                mDisposable.add(mViewModel.updateName(newName, id)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe());

                                mDatabase.widgetCheckListDao().updateName(newName, id);
                            }
                        }
                    }
                }
            }
        }

        @OnClick(R.id.row_cb)
        public void onClickCheckbox(View v) {

            int position = getAdapterPosition();

            if (((CheckBox) v).isChecked()) {
                // updating isDone to true in database
                mDisposable.add(mViewModel.updateIsDone(mItems.get(position).getId(), true)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

                mDatabase.widgetCheckListDao().updateIsDone(mItems.get(position).getId(), true);
            } else {
                // updating isDone to false in database
                mDisposable.add(mViewModel.updateIsDone(mItems.get(position).getId(), false)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());

                mDatabase.widgetCheckListDao().updateIsDone(mItems.get(position).getId(), false);
            }

            if (mListener != null) {
                mListener.onItemCheckedChange();
            }
        }
    }

    public interface ChecklistEventListener {
        void onItemCheckedChange();

        void onStartDrag(ViewHolder holder);
    }
}
