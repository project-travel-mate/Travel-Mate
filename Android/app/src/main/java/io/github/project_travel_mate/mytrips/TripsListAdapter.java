package io.github.project_travel_mate.mytrips;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Trip;

class TripsListAdapter extends ArrayAdapter<Trip> {
    private final Context mContext;
    private final List<Trip> mTrips;
    private LayoutInflater mInflater;

    TripsListAdapter(Context context,
                   List<Trip> trips) {
        super(context, R.layout.trip_listitem, trips);
        this.mContext = context;
        this.mTrips = trips;
        mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.trip_listitem, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mTrips.size() < position)
            return view;

        Picasso.with(mContext).load(mTrips.get(position).getImage()).placeholder(R.drawable.placeholder_image)
                .into(holder.city);
        holder.cityname.setText(mTrips.get(position).getName());
        holder.date.setText(mTrips.get(position).getStart());
        Log.v("time", mTrips.get(position).getStart() + " " + mTrips.get(position).getImage());
        final Calendar cal = Calendar.getInstance();
        try {
            cal.setTimeInMillis(Long.parseLong(mTrips.get(position).getStart()) * 1000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        final String timeString =
                new SimpleDateFormat("dd-MMM", Locale.US).format(cal.getTime());
        holder.date.setText(timeString);
        view.setOnClickListener(v -> {
            Intent intent = MyTripInfoActivity.getStartIntent(mContext, mTrips.get(position));
            mContext.startActivity(intent);
        });

        return view;
    }

    class ViewHolder {

        @BindView(R.id.profile_image)
        ImageView city;
        @BindView(R.id.tv)
        TextView cityname;
        @BindView(R.id.date)
        TextView date;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}