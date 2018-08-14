package io.github.project_travel_mate.friend;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.mytrips.MyTripInfoActivity;
import objects.Trip;

public class MutualTripsAdapter extends RecyclerView.Adapter<MutualTripsAdapter.ViewHolder> {

    private Context mContext;
    private List<Trip>  mTrips;

    MutualTripsAdapter(Context context, List<Trip> trips) {
        mContext = context;
        mTrips = trips;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        ImageView cityImage;
        @BindView(R.id.tv)
        TextView cityName;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.trip_linear_layout)
        LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.trip_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //changing width so that more than one trip list item is displayed on screen
        holder.linearLayout.getLayoutParams().width = 500;
        holder.linearLayout.requestLayout();

        Picasso.with(mContext).load(mTrips.get(position).getImage()).placeholder(R.drawable.placeholder_image)
                .into(holder.cityImage);
        holder.cityName.setText(mTrips.get(position).getName());
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
        holder.cityImage.setOnClickListener(v -> {
            Intent intent = MyTripInfoActivity.getStartIntent(mContext, mTrips.get(position));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }
}
