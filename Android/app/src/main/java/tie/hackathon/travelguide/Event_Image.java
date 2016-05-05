package tie.hackathon.travelguide;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Util.Constants;
import adapters.ImageAdapter;


public class Event_Image extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event__image);

        Intent i = getIntent();
        String name = i.getStringExtra(Constants.EVENT_NAME);
        int pos = i.getIntExtra(Constants.IMAGE_NO, -1);


        ArrayList<String> x = i.getStringArrayListExtra(Constants.EVENT_IMG);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this, x);
        viewPager.setAdapter(adapter);
        if (pos != -1)
            viewPager.setCurrentItem(pos);


        setTitle(name);


        getSupportActionBar().hide();

    }

}
