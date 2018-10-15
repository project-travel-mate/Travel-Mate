package io.github.project_travel_mate.mytrips;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Trip;

class TripsListAdapter extends RecyclerView.Adapter<TripsListAdapter.TimelineViewHolder> {
    private Context mContext;
    private final List<Trip> mTrips;
    private LayoutInflater mInflater;
    private static ClickListener mClickListener;

    TripsListAdapter(List<Trip> trips) {
        this.mTrips = trips;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mInflater = LayoutInflater.from(mContext);
        View view;

        view = mInflater.inflate(R.layout.trip_listitem, parent, false);

        return new TimelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        Trip trip = mTrips.get(position);

        Calendar calendar = new GregorianCalendar();
        Long currentTime = calendar.getTimeInMillis() / 1000;

        if (Long.compare(Long.valueOf(trip.getStart()), currentTime) > 0) {
            holder.timelineView.setMarker(VectorDrawableCompat.create(mContext.getResources(),
                    R.drawable.ic_marker_inactive , mContext.getTheme()));
        } else if (Long.compare(Long.valueOf(trip.getStart()), currentTime) < 0) {
            holder.timelineView.setMarker(VectorDrawableCompat.create(mContext.getResources(),
                    R.drawable.ic_marker_active , mContext.getTheme()));
        } else {
            holder.timelineView.setMarker(VectorDrawableCompat.create(mContext.getResources(),
                    R.drawable.ic_marker , mContext.getTheme()));
        }

        Picasso.with(mContext).load(trip.getImage()).placeholder(R.drawable.placeholder_image)
                .into(holder.city);
        holder.cityname.setText(trip.getName());
        holder.date.setText(trip.getStart());
        Log.v("time", trip.getStart() + " " + trip.getImage());
        final Calendar cal = Calendar.getInstance();
        try {
            cal.setTimeInMillis(Long.parseLong(trip.getStart()) * 1000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        final String timeString =
                new SimpleDateFormat("dd-MMM", Locale.US).format(cal.getTime());
        holder.date.setText(timeString);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mTrips != null ? mTrips.size() : 0;
    }

    public void addItem(Trip item, int index) {
        mTrips.add(index, item);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mTrips.remove(index);
        notifyItemRemoved(index);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TripsListAdapter.mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
    static class TimelineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.profile_image)
        ImageView city;
        @BindView(R.id.tv)
        TextView cityname;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.timeline_view)
        TimelineView timelineView;

        TimelineViewHolder(View view, int viewType) {
            super(view);

            ButterKnife.bind(this, view);
            timelineView.initLine(viewType);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(getAdapterPosition(), view);
        }
    }
}