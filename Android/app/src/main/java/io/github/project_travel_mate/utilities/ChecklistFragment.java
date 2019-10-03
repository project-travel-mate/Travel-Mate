package io.github.project_travel_mate.utilities;

import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import database.AppDataBase;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.roompersistence.ChecklistViewModel;
import io.github.project_travel_mate.roompersistence.Injection;
import io.github.project_travel_mate.roompersistence.ViewModelFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import objects.ChecklistItem;
import utils.TravelmateSnackbars;

import static utils.Constants.BASE_TASKS;
import static utils.Constants.IS_ADDED_INDB;

public class ChecklistFragment extends Fragment implements TravelmateSnackbars,
        ChecklistAdapter.ChecklistEventListener {

    @BindView(R.id.recycler_pending)
    RecyclerView mPendingRecycler;

    @BindView(R.id.layout_divider)
    LinearLayout mTickedBanner;

    @BindView(R.id.divider_tv_count)
    TextView mTickedCounter;

    @BindView(R.id.divider_ic_arrow)
    ImageView mBannerArrow;

    @BindView(R.id.recycler_finished)
    RecyclerView mFinishedRecycler;

    private Activity mActivity;
    private ChecklistViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private View mChecklistView;
    private List<ChecklistItem> mItems = new ArrayList<>();
    private AppDataBase mDatabase;
    private MenuItem mActionDeleteMenuItem;
    private boolean mFinishedHidden = false;
    private ItemTouchHelper mTouchHelper;

    public ChecklistFragment() {
    }

    public static ChecklistFragment newInstance() {
        ChecklistFragment fragment = new ChecklistFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChecklistView = inflater.inflate(R.layout.fragment_check_list, container, false);

        ButterKnife.bind(this, mChecklistView);

        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(mActivity);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ChecklistViewModel.class);
        mDatabase = AppDataBase.getAppDatabase(mActivity);

        attachAdapter();

        return mChecklistView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mActivity.getMenuInflater().inflate(R.menu.checklist_menu, menu);
        mActionDeleteMenuItem = menu.findItem(R.id.action_delete);
        checkForCompletedItem();
    }

    private void checkForCompletedItem() {

        mDisposable.add(mViewModel.getCompletedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    if (items.size() > 0) {
                        mActionDeleteMenuItem.setVisible(true);
                    } else {
                        mActionDeleteMenuItem.setVisible(false);
                    }
                }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                initiateDeletion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void attachAdapter() {

        ChecklistAdapter pendingAdapter = new ChecklistAdapter(mActivity, mViewModel, true);
        pendingAdapter.setEventListener(ChecklistFragment.this);
        mPendingRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        mPendingRecycler.setAdapter(pendingAdapter);

        ItemTouchHelper.Callback callback = new ChecklistDragCallback(pendingAdapter);
        mTouchHelper = new ItemTouchHelper(callback);
        mTouchHelper.attachToRecyclerView(mPendingRecycler);

        ChecklistAdapter finishedAdapter = new ChecklistAdapter(mActivity, mViewModel, false);
        finishedAdapter.setEventListener(ChecklistFragment.this);
        mFinishedRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        mFinishedRecycler.setAdapter(finishedAdapter);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //First time users
        String isAlreadyAdded = sharedPreferences.getString(IS_ADDED_INDB, "null");
        if (isAlreadyAdded.equals("null")) {
            for (int i = 0; i < BASE_TASKS.size(); i++) {
                ChecklistItem checklistItem = new ChecklistItem(BASE_TASKS.get(i), false, i);
                mDisposable.add(mViewModel.insertItem(checklistItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
                mDatabase.widgetCheckListDao().insert(checklistItem);
            }
            editor.putString(IS_ADDED_INDB, "yes");
            editor.apply();
        }

        mDisposable.add(mViewModel.getPendingItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pendingAdapter::updateChecklist)
        );

        mDisposable.add(mViewModel.getFinishedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    // This check is needed to ensure the Fragment is attached to
                    // an activity context before we attempt to modify the TextView
                    if (getActivity() != null) {
                        mTickedCounter.setText(getString(R.string.ticked_items_count, items.size()));
                    }
                    finishedAdapter.updateChecklist(items);
                })
        );

        // Subscribe to complete list for the widget
        mDisposable.add(mViewModel.getSortedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> mDatabase.widgetCheckListDao().insertAll(items)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @OnClick(R.id.layout_divider)
    public void onTickedBannerClick() {
        if (mFinishedHidden) {
            // should make finished items visible
            mFinishedRecycler.setVisibility(View.VISIBLE);
            mBannerArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        } else {
            // should hide finished items
            mFinishedRecycler.setVisibility(View.GONE);
            mBannerArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        }

        mFinishedHidden = !mFinishedHidden;
    }

    /**
     * Initiates deletion of completed tasks by showing
     * an alert dialog asking the user to confirm deletion
     */
    public void initiateDeletion() {

        //First add all completed tasks in mItems
        //so that on clicking undo,tasks can added again
        mDisposable.add(mViewModel.getCompletedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    mItems.clear();
                    mItems.addAll(items);
                }));
        //set AlertDialog before deleting all tasks
        ContextThemeWrapper crt = new ContextThemeWrapper(mActivity, R.style.AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(crt);
        builder.setMessage(R.string.delete_tasks)
                .setPositiveButton(R.string.positive_button,
                        (dialog, which) -> {
                            mActionDeleteMenuItem.setVisible(false);
                            deleteCompletedTasks();
                        })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {
                            //do nothing on clicking cancel
                        });
        builder.create().show();
    }

    /**
     * Deletes completed tasks from database and provides user
     * the option to undo this task.
     */
    private void deleteCompletedTasks() {

        //deletes all completed task from database
        mDisposable.add(mViewModel.deleteCompletedTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
        // TODO make this deleteCompleted tasks
        mDatabase.widgetCheckListDao().deleteAll();
        //creates a snackbar with undo option
        TravelmateSnackbars.createSnackBar(mActivity.findViewById(R.id.checklist_root_layout),
                R.string.deleted_task_message,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> {
                    // TODO can replace this with a single multi-item insert statement
                    for (int i = 0; i < mItems.size(); i++) {
                        //adds all completed task in database again
                        mDisposable.add(mViewModel.insertItem(mItems.get(i))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe());
                        mDatabase.widgetCheckListDao().insert(mItems.get(i));
                    }

                    if (mItems.size() > 0) mActionDeleteMenuItem.setVisible(true);
                })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    @Override
    public void onItemCheckedChange() {
        checkForCompletedItem();
    }

    @Override
    public void onStartDrag(ChecklistAdapter.ViewHolder holder) {
        mTouchHelper.startDrag(holder);
    }
}



