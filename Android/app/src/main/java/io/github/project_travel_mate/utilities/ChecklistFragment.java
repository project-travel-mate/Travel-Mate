package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import io.github.project_travel_mate.R;
import io.github.project_travel_mate.roompersistence.Injection;
import io.github.project_travel_mate.roompersistence.ViewModelFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import objects.ChecklistItem;

import io.github.project_travel_mate.roompersistence.ChecklistViewModel;

import static utils.Constants.IS_ADDED_INDB;
import static utils.Constants.BASE_TASKS;

public class ChecklistFragment extends Fragment {

    @BindView(R.id.listview)
    ListView listview;
    private Activity mActivity;
    private ChecklistViewModel mViewModel;
    private ViewModelFactory mViewModelFactory;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public ChecklistFragment() {
    }

    public static ChecklistFragment newInstance() {
        ChecklistFragment fragment = new ChecklistFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_list, container, false);

        ButterKnife.bind(this, view);

        mViewModelFactory = Injection.provideViewModelFactory(mActivity);
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ChecklistViewModel.class);

        attachAdapter();

        return view;
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
}



