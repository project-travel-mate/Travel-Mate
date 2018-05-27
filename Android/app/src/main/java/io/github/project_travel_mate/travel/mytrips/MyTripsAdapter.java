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

class MyTripsAdapter extends ArrayAdapter<Trip> {
    private final Activity context;
    private final List<Trip> trips;

    MyTripsAdapter(Activity context,
                   List<Trip> trips) {
        super(context, R.layout.trip_listitem, trips);
        this.context    = context;
        this.trips       = trips;
    }

    @NonNull
    @Override
    public View getView(final int position, View view2, @NonNull ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(mInflater).inflate(R.layout.trip_listitem, (ViewGroup) null);
        ImageView city = view.findViewById(R.id.profile_image);
        TextView cityname = view.findViewById(R.id.tv);
        TextView date = view.findViewById(R.id.date);

        if (position == 0) {
            city.setImageResource(R.drawable.ic_add_circle_black_24dp);
            cityname.setText(context.getResources().getString(R.string.prompt_add_new_trip));
            date.setText("");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, AddNewTrip.class);
                    context.startActivity(i);
                }
            });

        } else {
            Picasso.with(context).load(trips.get(position).getImage()).placeholder(R.drawable.add_list_item)
                    .into(city);
            cityname.setText(trips.get(position).getName());
            date.setText(trips.get(position).getStart());
            Log.e("time", trips.get(position).getStart() + " " + trips.get(position).getImage());
            final Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(trips.get(position).getStart()) * 1000);
            final String timeString =
                    new SimpleDateFormat("dd-MMM", Locale.US).format(cal.getTime());
            date.setText(timeString);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MyTripInfo.class);
                    i.putExtra("_id", trips.get(position).getId());
                    i.putExtra("_image", trips.get(position).getImage());
                    context.startActivity(i);
                }
            });
        }
        return view;
    }
}