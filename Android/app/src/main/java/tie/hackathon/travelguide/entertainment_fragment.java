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


public class entertainment_fragment extends Fragment implements View.OnClickListener {


    Activity activity;

    public entertainment_fragment() {
    }

    LinearLayout music, books;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.content_entertainment, container, false);

        music = (LinearLayout) v.findViewById(R.id.music);
        books = (LinearLayout) v.findViewById(R.id.book);

        music.setOnClickListener(this);
        books.setOnClickListener(this);

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

            case R.id.book:
                i = new Intent(activity, Books_new.class);
                startActivity(i);
                break;
        }

    }
}
