package tie.hackathon.travelguide;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyTrips extends AppCompatActivity {

    GridView g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);


        List<String> a = new ArrayList<>();
        a.add("yo");
        a.add("yo");
        a.add("yo");
        a.add("yo");
        a.add("yo");
        a.add("yo");
        a.add("yo");

        g = (GridView) findViewById(R.id.gv);
        MyTripsadapter ad = new MyTripsadapter(this,a);
        g.setAdapter(ad);

        setTitle("My Trips");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    public class MyTripsadapter extends ArrayAdapter<String> {
        private final Activity context;
        private final List<String> ids;


        public MyTripsadapter(Activity context, List<String> id) {
            super(context, R.layout.trip_listitem, id);
            this.context = context;
            ids = id;


        }

        private class ViewHolder {

            ImageView city;
            TextView cityname;

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = mInflater.inflate(R.layout.trip_listitem, null);
                holder = new ViewHolder();
                holder.city = (ImageView) view.findViewById(R.id.profile_image);
                holder.cityname = (TextView) view.findViewById(R.id.tv);

                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();


            if(position == ids.size()-1) {
                holder.city.setImageResource(R.drawable.ic_add_circle_black_24dp);
                holder.cityname.setText("Add New Trip");

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                    }
                });

            }
            else {
                holder.city.setImageResource(R.drawable.delhi);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }




            return view;
        }


    }
}
