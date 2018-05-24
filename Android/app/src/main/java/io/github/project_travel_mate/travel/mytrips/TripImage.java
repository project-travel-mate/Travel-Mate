package io.github.project_travel_mate.travel.mytrips;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import adapters.ImageAdapter;
import io.github.project_travel_mate.R;
import utils.Constants;


public class TripImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trip_image);

        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.EVENT_NAME);
        int pos = intent.getIntExtra(Constants.IMAGE_NO, -1);

        ArrayList<String> images = intent.getStringArrayListExtra(Constants.EVENT_IMG);

        ViewPager viewPager = findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);
        if (pos != -1)
            viewPager.setCurrentItem(pos);

        setTitle(name);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}
