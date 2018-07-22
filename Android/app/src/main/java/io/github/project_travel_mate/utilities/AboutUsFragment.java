package io.github.project_travel_mate.utilities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {

    @BindView(R.id.tv_version_code)
    TextView mVersionCode;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AboutUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, view);

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        mVersionCode.setText(version);
        return view;
    }

    @OnClick(R.id.cv_fork)
    public void onForkClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/project-travel-mate/Travel-Mate/"));
        startActivity(viewIntent);
    }

    @OnClick(R.id.cv_privacy_policy)
    public void onPrivacyPolicyClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/privacy-policy-travel-mate/home"));
        startActivity(viewIntent);
    }

    @OnClick(R.id.cv_website)
    public void onWebsiteClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("http://project-travel-mate.github.io/Travel-Mate/"));
        startActivity(viewIntent);
    }

    @OnClick(R.id.cv_contact_us)
    public void onContactUsClicked() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "projecttravelmate.github.io@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @OnClick(R.id.cv_share)
    public void onShareClicked() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hi! I found out this app for the travellers. Do download it : https://play.google.com/store/apps/details?id=io.github.project_travel_mate");
        startActivity(sendIntent);
    }

    @OnClick(R.id.cv_report_bug)
    public void onReportBugClicked() {
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragment = BugReportFragment.newInstance();
        if (fragment != null) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.inc, fragment).commit();
        }
    }
}
