package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import Util.Constants;


public class start_journey_fragment extends Fragment implements View.OnClickListener {


    static Activity activity;

    public start_journey_fragment() {
    }

    LinearLayout music, books,  food, hangout, monuments, busstop, shopping, petrol, atm, hospital,realtime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.content_start_journey, container, false);

        music = (LinearLayout) v.findViewById(R.id.music);
        books = (LinearLayout) v.findViewById(R.id.book);
        food = (LinearLayout) v.findViewById(R.id.food);
        hangout = (LinearLayout) v.findViewById(R.id.hangoutplaces);
        monuments = (LinearLayout) v.findViewById(R.id.monuments);
        busstop = (LinearLayout) v.findViewById(R.id.buisstop);
        shopping = (LinearLayout) v.findViewById(R.id.shopping);
        petrol = (LinearLayout) v.findViewById(R.id.petrol);
        atm = (LinearLayout) v.findViewById(R.id.atm);
        hospital = (LinearLayout) v.findViewById(R.id.hospital);
        realtime = (LinearLayout) v.findViewById(R.id.realtime);

        realtime.setOnClickListener(this);
        music.setOnClickListener(this);
        books.setOnClickListener(this);
        food.setOnClickListener(this);
        hangout.setOnClickListener(this);
        monuments.setOnClickListener(this);
        busstop.setOnClickListener(this);
        shopping.setOnClickListener(this);
        petrol.setOnClickListener(this);
        atm.setOnClickListener(this);
        hospital.setOnClickListener(this);

        return v;
    }


    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        this.activity = (Activity)c;
    }


    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {


            case R.id.realtime :
                i = new Intent(activity, MapRealTimeActivity.class);
                startActivity(i);

                break;


            case R.id.music:
                i = new Intent(activity, Music.class);
                startActivity(i);
                break;


            case R.id.book:
                i = new Intent(activity, Books_new.class);
                startActivity(i);
                break;


            case R.id.food:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "0");
                startActivity(i);
                break;


            case R.id.hangoutplaces:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "1");
                startActivity(i);
                break;


            case R.id.monuments:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "2");
                startActivity(i);
                break;
            case R.id.buisstop:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "3");
                startActivity(i);
                break;


            case R.id.shopping:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "4");
                startActivity(i);
                break;
            case R.id.petrol:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "5");
                startActivity(i);
                break;


            case R.id.atm:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "6");
                startActivity(i);
                break;
            case R.id.hospital:
                i = new Intent(activity, MapActivity.class);
                i.putExtra(Constants.MODE, "7");
                startActivity(i);
                break;


        }

    }
}
