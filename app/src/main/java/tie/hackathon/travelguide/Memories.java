package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Memories extends Fragment {


    Activity activity;
    Button capt;

    public Memories() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_memories, container, false);
        capt = (Button) v.findViewById(R.id.cap);

        capt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(activity,CaptureImage.class);
                startActivity(t);
            }
        });


        return v;
    }


    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        this.activity = (Activity) c;
    }


}
