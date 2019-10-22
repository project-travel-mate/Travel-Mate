package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.City;
import utils.Utils;

import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;

public class CityMapActivity extends AppCompatActivity {
    @BindView(R.id.mv_city_map)
    MapView cityMap;
    @BindView(R.id.fab_save_map)
    FloatingActionButton  saveMap;

    public static Intent getStartIntent(Context context, City city) {
        return new Intent(context, CityMapActivity.class)
                .putExtra(EXTRA_MESSAGE_CITY_OBJECT, city);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_map);
        ButterKnife.bind(this);

        final City mCity = (City) getIntent().getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(mCity.mNickname);

        cityMap.setBuiltInZoomControls(false);
        cityMap.setMultiTouchControls(true);
        cityMap.setTilesScaledToDpi(true);
        cityMap.setMinZoomLevel(14.0);
        cityMap.setMaxZoomLevel(29.0);

        if (Utils.isNetworkConnected(this)) {
            cityMap.setUseDataConnection(true);
        } else {
            cityMap.setUseDataConnection(false);
            saveMap.hide();
        }

        cityMap.getController().setZoom(14.0);

        final double latitude = Double.parseDouble(mCity.getLatitude());
        final double longitude = Double.parseDouble(mCity.getLongitude());

        GeoPoint cityPoint = new GeoPoint(latitude, longitude);
        cityMap.getController().setCenter(cityPoint);

        final ArrayList<GeoPoint> points = new ArrayList<>();
        points.add(cityPoint);

        saveMap.setOnClickListener(v -> {
            CacheManager manager = new CacheManager(cityMap);
            manager.downloadAreaAsync(this, points, 14, 29);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
