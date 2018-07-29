package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class ChecklistFragment extends Fragment implements TravelmateSnackbars {

    @BindView(R.id.listview)
    ListView listview;
    private Activity mActivity;
    private ChecklistViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private View mChecklistView;
    private List<ChecklistItem> mItems = new ArrayList<>();

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

        attachAdapter();

        return mChecklistView;
    }

    @OnClick(R.id.add)
    void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = (mActivity).getLayoutInflater();
        builder.setTitle("Add new item");
        builder.setCancelable(false);
        final View dialogv = inflater.inflate(R.layout.dialog, null, false);
        builder.setView(dialogv)
                .setPositiveButton("OK", (dialog, id1) -> {
                    EditText e1 = dialogv.findViewById(R.id.task);
                    if (!e1.getText().toString().equals("")) {
                        ChecklistItem checklistItem = new ChecklistItem(e1.getText().toString(), String.valueOf(0));
                        mDisposable.add(mViewModel.insertItem(checklistItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe());
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.checklist_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete :
                initiateDeletion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attachAdapter() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //First time users
        String isAlreadyAdded = sharedPreferences.getString(IS_ADDED_INDB, "null");
        if (isAlreadyAdded.equals("null")) {
            for (int i = 0; i < BASE_TASKS.size(); i++) {
                ChecklistItem checklistItem = new ChecklistItem(BASE_TASKS.get(i), String.valueOf(0));
                mDisposable.add(mViewModel.insertItem(checklistItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
            }
            editor.putString(IS_ADDED_INDB, "yes");
            editor.apply();
        }
        mDisposable.add(mViewModel.getSortedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> listview.setAdapter(new ChecklistAdapter(mActivity, items, mViewModel))));
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
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
                        (dialog, which) -> deleteCompletedTasks())
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
        //creates a snackbar with undo option
        TravelmateSnackbars.createSnackBar(mChecklistView.findViewById(R.id.fragment_checklist),
                R.string.deleted_task_message,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> {
                    for (int i = 0; i < mItems.size(); i++) {
                        //adds all completed task in database again
                        ChecklistItem checklistItem = new ChecklistItem(mItems.get(i).getName(), String.valueOf(1));
                        mDisposable.add(mViewModel.insertItem(checklistItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe());
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }
}



