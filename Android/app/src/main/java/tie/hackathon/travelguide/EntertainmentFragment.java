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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EntertainmentFragment extends Fragment {


    private Activity activity;

    public EntertainmentFragment() {
    }

    LinearLayout books;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_entertainment, container, false);

        ButterKnife.bind(this,v);

        return v;
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
