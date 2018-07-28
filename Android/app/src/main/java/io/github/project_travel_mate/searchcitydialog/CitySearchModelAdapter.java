package io.github.project_travel_mate.searchcitydialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.github.project_travel_mate.R;
import ir.mirrajabi.searchdialog.StringsHelper;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;
import utils.CircleImageView;

/**
 * Adapter fo the list (implemented as recycler view)
 */
public class CitySearchModelAdapter<T extends Searchable>
        extends RecyclerView.Adapter<CitySearchModelAdapter.ViewHolder> {

    protected Context mContext;
    private List<T> mItems = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int mLayout;
    private SearchResultListener mSearchResultListener;
    private AdapterViewBinder<T> mViewBinder;
    private String mSearchTag;
    private BaseSearchDialogCompat mSearchDialog;

    public CitySearchModelAdapter(Context context, @LayoutRes int layout, List<T> items) {
        this(context, layout, null, items);
    }

    public CitySearchModelAdapter(Context context, @LayoutRes int layout,
                                   @Nullable AdapterViewBinder<T> viewBinder,
                                   List<T> items) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mItems = items;
        this.mLayout = layout;
        this.mViewBinder = viewBinder;
    }

    public List<T> getItems() {
        return mItems;
    }

    public void setItems(List<T> objects) {
        this.mItems = objects;
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mLayoutInflater.inflate(mLayout, parent, false);
        convertView.setTag(new ViewHolder(convertView));
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CitySearchModelAdapter.ViewHolder holder, int position) {
        initializeViews(getItem(position), holder, position);
    }
    private void initializeViews(final T object, final CitySearchModelAdapter.ViewHolder holder,
                                 final int position) {
        if (mViewBinder != null)
            mViewBinder.bind(holder, object, position);

        TextView text = holder.getViewById(R.id.hotel_city_name);
        CircleImageView image = holder.getViewById(R.id.hotel_city_image);
        Picasso.with(mContext).load(((CitySearchModel) object).getImageUrl())
                .into(image);

        //highlight the letter(s) user has searched for
        boolean mHighlightPartsInCommon = true;
        if (mSearchTag != null && mHighlightPartsInCommon)
            text.setText(StringsHelper.highlightLCS(object.getTitle(), getSearchTag(),
                    Color.RED ));
        else text.setText(object.getTitle());

        if (mSearchResultListener != null)
            holder.getBaseView().setOnClickListener(
                    view -> mSearchResultListener.onSelected(mSearchDialog, object, position));
    }

    public void setSearchResultListener(SearchResultListener searchResultListener) {
        this.mSearchResultListener = searchResultListener;
    }

    public CitySearchModelAdapter<T> setSearchTag(String searchTag) {
        mSearchTag = searchTag;
        return this;
    }

    public String getSearchTag() {
        return mSearchTag;
    }

    public CitySearchModelAdapter<T> setSearchDialog(BaseSearchDialogCompat searchDialog) {
        mSearchDialog = searchDialog;
        return this;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mBaseView;

        public ViewHolder(View view) {
            super(view);
            mBaseView = view;
        }

        public View getBaseView() {
            return mBaseView;
        }
        public <T> T getViewById(@IdRes int id) {
            return (T) mBaseView.findViewById(id);
        }
    }

    public interface AdapterViewBinder<T> {
        void bind(ViewHolder holder, T item, int position);
    }
}
