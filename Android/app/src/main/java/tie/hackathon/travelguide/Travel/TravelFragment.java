package tie.hackathon.travelguide.Travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import tie.hackathon.travelguide.Hotels;
import tie.hackathon.travelguide.MapRealTimeActivity;
import tie.hackathon.travelguide.MyTrips;
import tie.hackathon.travelguide.R;
import tie.hackathon.travelguide.SelectModeOfTransport;
import tie.hackathon.travelguide.ShoppingCurrentCity;


public class TravelFragment extends Fragment implements View.OnClickListener,TravelView {

    private Activity activity;
    private TravelPresenter presenter = new TravelPresenter();

    @BindView(R.id.vehicle)     LinearLayout vehicle;
    @BindView(R.id.accomo)      LinearLayout acc;
    @BindView(R.id.shopping)    LinearLayout shop;
    @BindView(R.id.realtime)    LinearLayout realtime;
    @BindView(R.id.mytrips)     LinearLayout mytrips;

    public TravelFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_travel, container, false);

        ButterKnife.bind(this,v);
        presenter.bind(this);

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
        presenter.onMenuButtonClick(view.getId());
    }

    @Override
    public void startVehicleActivity() {
        startActivity(new Intent(activity, SelectModeOfTransport.class));
    }

    @Override
    public void startShoppingCurrentCityActivity() {
        startActivity(new Intent(activity, ShoppingCurrentCity.class));
    }

    @Override
    public void startHotelsActivity() {
        startActivity(new Intent(activity, Hotels.class));
    }

    @Override
    public void startMapRealTimeActivity() {
        startActivity(new Intent(activity, MapRealTimeActivity.class));
    }

    @Override
    public void startMyTripsActivity() {
        startActivity(new Intent(activity, MyTrips.class));
    }
}
