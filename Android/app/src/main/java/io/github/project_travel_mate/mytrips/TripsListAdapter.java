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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Trip;

class TripsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 100;

    private Context mContext;
    private ArrayList<Trip> mTrips;
    private static ClickListener mClickListener;
    private int mOldTripsCount;
    private int mActualTripsCount;

    TripsListAdapter(ArrayList<Trip> trips) {
        initData(trips);
    }

    private boolean dateIsOlderThenNow(String dateInString) {
        final Calendar cal = Calendar.getInstance();
        try {
            cal.setTimeInMillis(Long.parseLong(dateInString) * 1000);
            Date date = cal.getTime();
            return date.before(new Date());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = mInflater.inflate(R.layout.trip_header, parent, false);
            return new TimelineHeader(view);
        } else {
            view = mInflater.inflate(R.layout.trip_listitem, parent, false);
            return new TimelineViewHolder(view, viewType);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TimelineViewHolder) {
            TimelineViewHolder tripHolder = (TimelineViewHolder) holder;
            Trip trip;
            if (position >= mActualTripsCount) {
                trip = mTrips.get(position - 1);
            } else {
                trip = mTrips.get(position);
            }
            tripHolder.mTrip = trip;

            Calendar calendar = new GregorianCalendar();
            Long currentTime = calendar.getTimeInMillis() / 1000;

            if (Long.compare(Long.valueOf(trip.getStart()), currentTime) > 0) {
                tripHolder.timelineView.setMarker(VectorDrawableCompat.create(mContext.getResources(),
                        R.drawable.ic_marker_inactive, mContext.getTheme()));
            } else if (Long.compare(Long.valueOf(trip.getStart()), currentTime) < 0) {
                tripHolder.timelineView.setMarker(VectorDrawableCompat.create(mContext.getResources(),
                        R.drawable.ic_marker_active, mContext.getTheme()));
            } else {
                tripHolder.timelineView.setMarker(VectorDrawableCompat.create(mContext.getResources(),
                        R.drawable.ic_marker, mContext.getTheme()));
            }

            Picasso.with(mContext).load(trip.getImage()).placeholder(R.drawable.placeholder_image)
                    .into(tripHolder.city);
            tripHolder.cityname.setText(trip.getName());
            tripHolder.date.setText(trip.getStart());
            Log.v("time", trip.getStart() + " " + trip.getImage());
            final Calendar cal = Calendar.getInstance();
            try {
                cal.setTimeInMillis(Long.parseLong(trip.getStart()) * 1000);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            final String timeString =
                    new SimpleDateFormat("dd-MMM", Locale.US).format(cal.getTime());
            tripHolder.date.setText(timeString);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mActualTripsCount) {
            return VIEW_TYPE_HEADER;
        } else {
            return TimelineView.getTimeLineViewType(position, getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        int headerAddition = mOldTripsCount > 0 ? 1 : 0;
        return mTrips != null ? mTrips.size() + headerAddition : 0;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TripsListAdapter.mClickListener = clickListener;
    }

    public void initData(ArrayList<Trip> trips) {
        this.mTrips = new ArrayList<>();
        ArrayList<Trip> actualTrips = new ArrayList<>();
        ArrayList<Trip> oldTrips = new ArrayList<>();
        for (Trip trip : trips) {
            if (dateIsOlderThenNow(trip.getStart())) {
                oldTrips.add(trip);
            } else {
                actualTrips.add(trip);
            }
        }
        mOldTripsCount = oldTrips.size();
        mActualTripsCount = actualTrips.size();
        this.mTrips.addAll(actualTrips);
        this.mTrips.addAll(oldTrips);
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onItemClick(Trip trip);
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Trip mTrip;
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
            mClickListener.onItemClick(mTrip);
        }
    }
    class TimelineHeader extends RecyclerView.ViewHolder {

        public TimelineHeader(@NonNull View itemView) {
            super(itemView);
        }
    }
}