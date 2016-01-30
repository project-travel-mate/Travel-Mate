package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class plan_journey_fragment extends Fragment implements View.OnClickListener {


    static Activity activity;

    public plan_journey_fragment() {
        // Required empty public constructor
    }

    LinearLayout vehicle, acc, shop, city, check;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.content_plan_journey, container, false);

        check = (LinearLayout) v.findViewById(R.id.checklist);
        vehicle = (LinearLayout) v.findViewById(R.id.vehicle);
        acc = (LinearLayout) v.findViewById(R.id.accomo);
        shop = (LinearLayout) v.findViewById(R.id.shopping);
        city = (LinearLayout) v.findViewById(R.id.city);


        check.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        acc.setOnClickListener(this);
        shop.setOnClickListener(this);
        city.setOnClickListener(this);


        Log.e("vds","vdsvsd");

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public void onClick(View view) {
        Intent i;


        Log.e("click","fv");
        switch (view.getId()) {


            case R.id.checklist:
                i = new Intent(activity, CheckList.class);
                startActivity(i);
                break;

            case R.id.vehicle:
                i = new Intent(activity, Select_ModeOfTransport.class);
                startActivity(i);
                break;

            case R.id.city:
                i = new Intent(activity, CityInfo.class);
                startActivity(i);
                break;

            case R.id.shopping:
                i = new Intent(activity, Shopping_currentcity.class);
                startActivity(i);
                break;

            case R.id.accomo:
                i = new Intent(activity, Hotels.class);
                startActivity(i);
                break;
        }

    }
}
