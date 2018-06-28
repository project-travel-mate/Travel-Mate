package io.github.project_travel_mate.utilities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;

/**
 * Displays emergency contact numbers
 */
public class EmergencyFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.police)
    Button police;
    @BindView(R.id.fire)
    Button fire;
    @BindView(R.id.ambulance)
    Button ambulance;
    @BindView(R.id.blood_bank)
    Button blood_bank;
    @BindView(R.id.bomb)
    Button bomb;
    @BindView(R.id.railways)
    Button railways;

    public EmergencyFragment() {
    }

    public static EmergencyFragment newInstance() {
        EmergencyFragment fragment = new EmergencyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        ButterKnife.bind(this, view);

        police.setOnClickListener(this);
        fire.setOnClickListener(this);
        ambulance.setOnClickListener(this);
        blood_bank.setOnClickListener(this);
        bomb.setOnClickListener(this);
        railways.setOnClickListener(this);

        return view;
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
