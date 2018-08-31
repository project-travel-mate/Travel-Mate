package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import utils.Compass;
public class CompassActivity extends AppCompatActivity {
    private static final String TAG = "CompassActivity";
    @BindView(R.id.compass_image_hands)
    ImageView mArrowView;
    private Compass mCompass;
    private float mCurrentAzimuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_compass);
        ButterKnife.bind(this);
        setTitle("Compass");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //Setup compass
        setupCompass();
    }
    /**
     * setup compass
     */
    private void setupCompass() {
        mCompass = new Compass(this);
        Compass.CompassListener cl = new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth) {
                adjustArrow(azimuth);
            }
        };
        mCompass.setListener(cl);
    }
    /**
     * adjuts arrow
     *
     * @param azimuth  azimuth
     */
    private void adjustArrow(float azimuth) {
        Log.d(TAG, "will set rotation from " + mCurrentAzimuth + " to "
                + azimuth);
        Animation an = new RotateAnimation(-mCurrentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mCurrentAzimuth = azimuth;
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        mArrowView.startAnimation(an);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CompassActivity.class);
        return intent;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        mCompass.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mCompass.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCompass.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        mCompass.stop();
    }
}
