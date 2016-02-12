package tie.hackathon.travelguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import Util.Constants;

public class DetectedBeacon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_beacon);

        Intent i = getIntent();

        String major = i.getStringExtra(Constants.CUR_MAJOR);
        Log.e("goit the beacon",major+" ");



    }
}
