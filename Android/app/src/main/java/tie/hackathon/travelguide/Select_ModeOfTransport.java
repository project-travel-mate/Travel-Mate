package tie.hackathon.travelguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Select_ModeOfTransport extends AppCompatActivity implements View.OnClickListener {

    LinearLayout car,train,bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__mode_of_transport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        car = (LinearLayout) findViewById(R.id.car);
        train = (LinearLayout) findViewById(R.id.train);
        bus = (LinearLayout) findViewById(R.id.bus);

        car.setOnClickListener(this);
        train.setOnClickListener(this);
        bus.setOnClickListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Select Mode of Transport");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() ==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        Intent i ;
        switch(view.getId()){

            case R.id.car : i = new Intent(Select_ModeOfTransport.this,CarDirections.class);
                startActivity(i);
                break;

            case R.id.bus : i = new Intent(Select_ModeOfTransport.this,Bus_list.class);
                startActivity(i);
                break;


            case R.id.train : i = new Intent(Select_ModeOfTransport.this,TrainList.class);
                startActivity(i);
                break;
        }

    }
}
