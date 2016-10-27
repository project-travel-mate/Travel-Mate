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

/**
 * Displays emergency contact numbers
 */
public class EmergencyFragment extends Fragment implements View.OnClickListener {

    Button police, fire, ambulance, blood_bank, bomb, railways;
    Activity activity;

    public EmergencyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_emergency, container, false);

        police = (Button) v.findViewById(R.id.police);
        fire = (Button) v.findViewById(R.id.fire);
        ambulance = (Button) v.findViewById(R.id.ambulance);
        blood_bank = (Button) v.findViewById(R.id.blood_bank);
        bomb = (Button) v.findViewById(R.id.bomb);
        railways = (Button) v.findViewById(R.id.railways);

        police.setOnClickListener(this);
        fire.setOnClickListener(this);
        ambulance.setOnClickListener(this);
        blood_bank.setOnClickListener(this);
        bomb.setOnClickListener(this);
        railways.setOnClickListener(this);

        return v;
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.activity = (Activity) activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        switch (v.getId()) {
            case R.id.police:
                intent.setData(Uri.parse("tel:100"));
                break;
            case R.id.fire:
                intent.setData(Uri.parse("tel:101"));
                break;
            case R.id.ambulance:
                intent.setData(Uri.parse("tel:102"));
                break;
            case R.id.blood_bank:
                intent.setData(Uri.parse("tel:25752924"));
                break;
            case R.id.bomb:
                intent.setData(Uri.parse("tel:22512201"));
                break;
            case R.id.railways:
                intent.setData(Uri.parse("tel:23366177"));
        }
        startActivity(intent);
    }
}
