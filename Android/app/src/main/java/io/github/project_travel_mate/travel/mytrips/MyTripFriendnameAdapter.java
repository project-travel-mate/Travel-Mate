package io.github.project_travel_mate.travel.mytrips;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import io.github.project_travel_mate.R;

class MyTripFriendnameAdapter extends ArrayAdapter<String> {
    private final Activity mContext;
    private final List<String> mName;

    MyTripFriendnameAdapter(Activity context, List<String> name) {
        super(context, R.layout.home_city_listitem, name);
        this.mContext = context;
        this.mName = name;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = Objects.requireNonNull(mInflater).inflate(R.layout.home_city_listitem, parent, false);
            holder = new ViewHolder();
            holder.iv = view.findViewById(R.id.name);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        holder.iv.setText(mName.get(position));
        return view;
    }

    private class ViewHolder {
        TextView iv;
    }
}
