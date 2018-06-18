package io.github.project_travel_mate.travel.mytrips;


import android.app.Activity;
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
import java.util.Objects;

import io.github.project_travel_mate.R;
import objects.Trip;

import static utils.Constants.EXTRA_MESSAGE_TRIP_OBJECT;

class MyTripsAdapter extends ArrayAdapter<Trip> {
    private final Activity mContext;
    private final List<Trip> mTrips;

    MyTripsAdapter(Activity context,
                   List<Trip> trips) {
        super(context, R.layout.trip_listitem, trips);
        this.mContext = context;
        this.mTrips = trips;
    }

    @NonNull
    @Override
    public View getView(final int position, View view2, @NonNull ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(mInflater).inflate(R.layout.trip_listitem, parent, false);
        ImageView city = view.findViewById(R.id.profile_image);
        TextView cityname = view.findViewById(R.id.tv);
        TextView date = view.findViewById(R.id.date);

        if (position == 0) {
            city.setImageResource(R.drawable.ic_add_circle_black_24dp);
            cityname.setText(mContext.getResources().getString(R.string.prompt_add_new_trip));
            date.setText("");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, AddNewTrip.class);
                    mContext.startActivity(i);
                }
            });

        } else {
            Picasso.with(mContext).load(mTrips.get(position).getImage()).placeholder(R.drawable.delhi)
                    .into(city);
            cityname.setText(mTrips.get(position).getName());
            date.setText(mTrips.get(position).getStart());
            Log.v("time", mTrips.get(position).getStart() + " " + mTrips.get(position).getImage());
            final Calendar cal = Calendar.getInstance();
            try {
                cal.setTimeInMillis(Long.parseLong(mTrips.get(position).getStart()) * 1000);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            final String timeString =
                    new SimpleDateFormat("dd-MMM", Locale.US).format(cal.getTime());
            date.setText(timeString);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyTripInfo.class);
                    intent.putExtra(EXTRA_MESSAGE_TRIP_OBJECT, mTrips.get(position));
                    mContext.startActivity(intent);
                }
            });
        }
        return view;
    }
}