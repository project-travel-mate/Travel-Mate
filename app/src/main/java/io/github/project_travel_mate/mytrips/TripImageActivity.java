package io.github.project_travel_mate.mytrips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import adapters.ImageAdapter;
import io.github.project_travel_mate.R;

import static utils.Constants.EVENT_IMG;
import static utils.Constants.EVENT_NAME;
import static utils.Constants.IMAGE_NO;


public class TripImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trip_image);

        Intent intent = getIntent();
        String name = intent.getStringExtra(EVENT_NAME);
        int pos = intent.getIntExtra(IMAGE_NO, -1);

        ArrayList<String> images = intent.getStringArrayListExtra(EVENT_IMG);

        ViewPager viewPager = findViewById(R.id.view_pager);
        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);
        if (pos != -1)
            viewPager.setCurrentItem(pos);

        setTitle(name);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, TripImageActivity.class);
        return intent;
    }
}
