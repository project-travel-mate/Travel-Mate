package io.github.project_travel_mate.utilities;

import android.content.Context;
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

    private static final String EMAIL = "projecttravelmate.github.io@gmail.com";
    private static final String WEBSITE = "http://project-travel-mate.github.io/Travel-Mate/";
    private static final String PRIVACY_POLICY = "https://sites.google.com/view/privacy-policy-travel-mate/home";
    private static final String GITHUB_REPO = "https://github.com/project-travel-mate/Travel-Mate/";
    private static final String SLACK_LINK = "https://join.slack.com/t/project-travel-mate/shared_invite/enQtNDE2MjgyOTA5ODg5LT" +
            "gwZGQ3NmY3Y2JjZTIxMWYwMTdkYzZiZmFjMjQ1ZDc1ZmM5NTNkYzQ3M2EwNjVmMzIyYTE4YzRiNjA4ZWRhZDc";
    private static final String BUY_ME_A_COFFEE = "https://www.buymeacoffee.com/qITGMWB";

    private Context mContext;

    @BindView(R.id.tv_version_code)
    TextView mVersionCode;

    public AboutUsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AboutUsFragment.
     */
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
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = pInfo.versionName;
            mVersionCode.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    @OnClick(R.id.cv_fork)
    public void onForkClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_REPO));
        startActivity(viewIntent);
    }

    @OnClick(R.id.cv_privacy_policy)
    public void onPrivacyPolicyClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY));
        startActivity(viewIntent);
    }

    @OnClick(R.id.cv_website)
    public void onWebsiteClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE));
        startActivity(viewIntent);
    }

    @OnClick(R.id.cv_contact_us)
    public void onContactUsClicked() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", EMAIL, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_hello));
        startActivity(Intent.createChooser(intent, getString(R.string.email_send)));
    }

    @OnClick(R.id.cv_share)
    public void onShareClicked() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_text));
        startActivity(sendIntent);
    }

    @OnClick(R.id.cv_report_bug)
    public void onReportBugClicked() {
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragment = BugReportFragment.newInstance();
        if (fragment != null && fragmentManager != null) {
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.inc, fragment).commit();
        }
    }

    @OnClick(R.id.cv_slack)
    public void onSlackClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse(SLACK_LINK));
        startActivity(viewIntent);
    }

    @OnClick(R.id.cv_bmc)
    public void onBuyMeACoffeeClicked() {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW, Uri.parse(BUY_ME_A_COFFEE));
        startActivity(viewIntent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
