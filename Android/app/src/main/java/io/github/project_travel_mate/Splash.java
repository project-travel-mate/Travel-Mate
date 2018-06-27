package io.github.project_travel_mate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;

import java.util.Objects;

import io.github.project_travel_mate.login.LoginActivity;

public class Splash extends AppCompatActivity {

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
            Intent i = new Intent(Splash.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 2000);
    }
}
