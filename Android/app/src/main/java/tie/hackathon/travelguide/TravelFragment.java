package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class TravelFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private LinearLayout vehicle;
    private LinearLayout acc;
    private LinearLayout shop;
    private LinearLayout realtime;
    private LinearLayout mytrips;

    public TravelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_travel, container, false);

        vehicle = (LinearLayout) v.findViewById(R.id.vehicle);
        acc = (LinearLayout) v.findViewById(R.id.accomo);
        shop = (LinearLayout) v.findViewById(R.id.shopping);
        mytrips = (LinearLayout) v.findViewById(R.id.mytrips);
        realtime = (LinearLayout) v.findViewById(R.id.realtime);

        realtime.setOnClickListener(this);
        mytrips.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        acc.setOnClickListener(this);
        shop.setOnClickListener(this);

        return v;
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = (Activity) activity;
    }


    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {

            case R.id.vehicle:
                i = new Intent(activity, SelectModeOfTransport.class);
                startActivity(i);
                break;

            case R.id.shopping:
                i = new Intent(activity, ShoppingCurrentCity.class);
                startActivity(i);
                break;

            case R.id.accomo:
                i = new Intent(activity, Hotels.class);
                startActivity(i);
                break;

            case R.id.realtime:
                i = new Intent(activity, MapRealTimeActivity.class);
                startActivity(i);
                break;

            case R.id.mytrips:
                i = new Intent(activity, MyTrips.class);
                startActivity(i);
                break;
        }
    }
}
