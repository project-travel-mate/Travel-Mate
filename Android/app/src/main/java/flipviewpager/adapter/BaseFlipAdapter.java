package flipviewpager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import flipviewpager.utils.FlipSettings;
import flipviewpager.view.FlipViewPager;
import io.github.project_travel_mate.R;

/**
 * @author Yalantis
 */
@SuppressWarnings("unchecked")
public abstract class BaseFlipAdapter<T> extends BaseAdapter {
    private final List<T> mItems;
    private final FlipSettings mSettings;
    private final LayoutInflater mInflater;

    protected BaseFlipAdapter(Context context, List<T> items, FlipSettings settings) {
        this.mItems = items;
        this.mSettings = settings;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // Checking if we need an additional row for single item
        return mItems.size() % 2 != 0 ? ((mItems.size() / 2) + 1) : (mItems.size() / 2);
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Trick to divide list into 2 parts
        T item1 = getItem(position * 2);
        // Used for cases when we have not an even size in incoming list
        T item2 = mItems.size() > (position * 2 + 1) ? getItem(position * 2 + 1) : null;

        final ViewHolder viewHolder;
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.flipper, parent, false);
        if (convertView.getTag() != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.mFlipViewPager = convertView.findViewById(R.id.flip_view);
        }

        // Listener to store flipped page
        viewHolder.mFlipViewPager.setOnChangePageListener(page -> mSettings.savePageState(position, page));

        if (viewHolder.mFlipViewPager.getAdapter() == null) {
            viewHolder.mFlipViewPager.setAdapter(
                    new MergeAdapter(item1, item2), mSettings.getDefaultPage(), position, mItems.size());
        } else {
            // Recycling internal adapter
            // So, it's double recycling - we have only 4-5 mFlipViewPager objects
            // and each of them have an adapter
            MergeAdapter adapter = (MergeAdapter) viewHolder.mFlipViewPager.getAdapter();
            adapter.updateData(item1, item2);
            viewHolder.mFlipViewPager.setAdapter(adapter,
                    mSettings.getPageForPosition(position), position, mItems.size());
        }
        return convertView;
    }

    protected abstract View getPage(int position, View convertView, ViewGroup parent, T item1, T item2);

    protected abstract int getPagesCount();

    class ViewHolder {
        FlipViewPager mFlipViewPager;
    }

    // Adapter merges 2 mItems together
    private class MergeAdapter extends BaseAdapter {
        private T mItem1;
        private T mItem2;

        MergeAdapter(T item1, T item2) {
            this.mItem1 = item1;
            this.mItem2 = item2;
        }

        void updateData(T item1, T item2) {
            this.mItem1 = item1;
            this.mItem2 = item2;
        }

        @Override
        public int getCount() {
            return mItem2 == null ? getPagesCount() - 1 : getPagesCount();
        }

        @Override
        public Object getItem(int position) {
            return position; // Stub
        }

        @Override
        public long getItemId(int position) {
            return position; // Stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getPage(position, convertView, parent, mItem1, mItem2);
        }
    }
}
