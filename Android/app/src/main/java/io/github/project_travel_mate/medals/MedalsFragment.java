package io.github.project_travel_mate.medals;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.utilities.AppConnectivity;

public class MedalsFragment extends Fragment {

    Activity mActivity;

    public MedalsFragment() {
    }

    public static MedalsFragment newInstance() {
        MedalsFragment fragment = new MedalsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppConnectivity mConnectivity = new AppConnectivity(getContext());
        View view = inflater.inflate(R.layout.fragment_medals, container, false);

        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

}
