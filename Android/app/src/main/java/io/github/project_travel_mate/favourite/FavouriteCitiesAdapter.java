package io.github.project_travel_mate.favourite;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.City;

public class FavouriteCitiesAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<City> mCities;
    private final LayoutInflater mInflater;

    FavouriteCitiesAdapter(Context context, List<City> cities) {
        this.mContext = context;
        this.mCities = cities;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCities.size();
    }

    @Override
    public Object getItem(int i) {
        return mCities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fav_city_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fav_name.setText(mCities.get(position).getNickname());
        Picasso.with(mContext).load(mCities.get(position).mAvatar).into(holder.fav_image);

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.fav_name)
        TextView fav_name;
        @BindView(R.id.fav_image)
        ImageView fav_image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
