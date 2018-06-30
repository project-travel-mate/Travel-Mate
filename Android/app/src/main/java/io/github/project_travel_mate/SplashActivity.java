package io.github.project_travel_mate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;

import io.github.project_travel_mate.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final PathView pathView = findViewById(R.id.pathView);
        pathView.getPathAnimator()
                .delay(1000)
                .duration(1000)
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();

        pathView.useNaturalColors();
        pathView.setFillAfter(true);

        // TODO :: check for the user_token here & redirect to corresponding class
        // If token is null -> LoginActivity, else MainActivity
        new Handler().postDelayed(() -> {
            Intent i = LoginActivity.getStartIntent(SplashActivity.this);
            startActivity(i);
            finish();
        }, 2000);
    }
}
