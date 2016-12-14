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

public class UtilitiesFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private LinearLayout sharecontact;
    private LinearLayout checklist;

    public UtilitiesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_utility, container, false);
        sharecontact = (LinearLayout) v.findViewById(R.id.sharecontact);
        checklist = (LinearLayout) v.findViewById(R.id.checklist);

        sharecontact.setOnClickListener(this);
        checklist.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = (Activity) activity;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.sharecontact:
                intent = new Intent(activity, ShareContact.class);
                startActivity(intent);
                break;
            case R.id.checklist:
                intent = new Intent(activity, Checklist.class);
                startActivity(intent);
                break;
        }
    }
}
