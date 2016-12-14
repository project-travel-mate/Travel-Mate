package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class EntertainmentFragment extends Fragment implements View.OnClickListener {


    private Activity activity;

    public EntertainmentFragment() {
    }

    private LinearLayout music;
    LinearLayout books;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.content_entertainment, container, false);
        music = (LinearLayout) v.findViewById(R.id.music);
        music.setOnClickListener(this);
        return v;
    }


    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        this.activity = (Activity) c;
    }


    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.music:
                i = new Intent(activity, Music.class);
                startActivity(i);
                break;
        }

    }
}
