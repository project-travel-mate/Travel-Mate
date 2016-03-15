package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dd.processbutton.FlatButton;


public class utilities_fragment extends Fragment implements View.OnClickListener {


    static Activity activity;
    FlatButton sharecontact,checklist,clock,currency;

    public utilities_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_utility, container, false);
        sharecontact = (FlatButton) v.findViewById(R.id.sharecontact);
        checklist = (FlatButton) v.findViewById(R.id.checklist);
        clock = (FlatButton) v.findViewById(R.id.cworldclock);
        currency = (FlatButton) v.findViewById(R.id.currency);


        sharecontact.setOnClickListener(this);
        checklist.setOnClickListener(this);
        clock.setOnClickListener(this);
        currency.setOnClickListener(this);



        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public void onClick(View view) {
        Intent i;


        switch (view.getId()) {

            case R.id.sharecontact :
                 i =new Intent(activity, shareContact.class);
                startActivity(i);
                break;

            case R.id.checklist :
                i =new Intent(activity, CheckList.class);
                startActivity(i);
                break;

            case R.id.cworldclock :
                 break;

            case R.id.currency :
                  break;





        }

    }
}
