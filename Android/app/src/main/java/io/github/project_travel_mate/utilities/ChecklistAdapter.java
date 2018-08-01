package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.roompersistence.ChecklistViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import objects.ChecklistItem;

class ChecklistAdapter extends ArrayAdapter<ChecklistItem> {

    private final Activity mContext;
    private  List<ChecklistItem> mItems;
    private ChecklistViewModel mViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    ChecklistAdapter(Activity context, List<ChecklistItem> items, ChecklistViewModel model) {
        super(context, R.layout.checklist_item);
        this.mContext = context;
        this.mItems = items;
        mViewModel = model;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.checklist_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (mItems.get(position).getIsDone().equals("1")) {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setText(mItems.get(position).getName());

        holder.checkBox.setOnClickListener(view1 -> {
            CheckBox c2 = (CheckBox) view1;
            if (c2.isChecked()) {
                //updating isDone to 1 in database
                mDisposable.add(mViewModel.updateIsDone(mItems.get(position).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
            } else {
                //updating isDone to 0 in database
                mDisposable.add(mViewModel.updateUndone(mItems.get(position).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe());
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.cb1)
        CheckBox checkBox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
