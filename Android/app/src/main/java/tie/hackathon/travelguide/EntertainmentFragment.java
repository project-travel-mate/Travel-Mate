package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class EntertainmentFragment extends Fragment {

    private Activity activity;
    public EntertainmentFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_entertainment, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        this.activity = (Activity) c;
    }

    @OnClick(R.id.music) void onClick(){
        Intent i = new Intent(activity, Music.class);
        startActivity(i);
    }
}
