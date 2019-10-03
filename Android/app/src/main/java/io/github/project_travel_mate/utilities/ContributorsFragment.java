package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import adapters.ContributorsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import objects.Contributor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.ExpandableHeightGridView;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

/**
 * @author amoraitis
 * A simple {@link Fragment} subclass.
 * Use the {@link ContributorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContributorsFragment extends Fragment {

    @BindView(R.id.android_contributors_gv)
    ExpandableHeightGridView android_contributors_gv;
    @BindView(R.id.server_contributors_gv)
    ExpandableHeightGridView server_contributors_gv;
    private String mToken;
    private Activity mActivity;
    private View mContributorsView;
    private ContributorsAdapter mAndroidContributorsAdapter;
    private ArrayList<Contributor> mAndroidContributors = new ArrayList<>();
    private ArrayList<Contributor> mServerContributors = new ArrayList<>();
    private ContributorsAdapter mServerContributorsAdapter;
    private Handler mHandler;

    public ContributorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContributorsFragment.
     */
    public static ContributorsFragment newInstance() {
        ContributorsFragment fragment = new ContributorsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContributorsView = inflater.inflate(R.layout.fragment_contributors, container, false);
        ButterKnife.bind(this, mContributorsView);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());
        mAndroidContributorsAdapter = new ContributorsAdapter(this.getContext(), mAndroidContributors);
        android_contributors_gv.setAdapter(mAndroidContributorsAdapter);

        mServerContributorsAdapter = new ContributorsAdapter(this.getContext(), mServerContributors);
        server_contributors_gv.setAdapter(mServerContributorsAdapter);

        setContributors("Travel-Mate");
        setContributors("server");
        android_contributors_gv.setOnItemClickListener((adapterView, view, i, l) -> startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(mAndroidContributors.get(i).getUrl()))
        ));
        server_contributors_gv.setOnItemClickListener((adapterView, view, i, l) -> startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(mServerContributors.get(i).getUrl()))
        ));
        return mContributorsView;
    }

    private void setContributors(String repo) {
        String uri = API_LINK_V2 + "get-contributors/" + repo;
        Log.v("EXECUTING", uri);

        //Set up client for android contributors
        OkHttpClient client = new OkHttpClient();
        //Request
        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Callback
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                mHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            final String responseString = response.body().string();
                            Log.v("RESPONSE", responseString);

                            Type listType = new TypeToken<ArrayList<Contributor>>() {
                            }.getType();
                            ArrayList<Contributor> contributorsList = new Gson().fromJson(responseString,
                                    listType);
                            updateDataAndUI(response.request().url().toString(), contributorsList);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    private void updateDataAndUI(String url, ArrayList<Contributor> contributorsList) {
        if (url.contains("server")) {
            mServerContributors = contributorsList;
            mServerContributorsAdapter.update(mServerContributors);
            server_contributors_gv.setExpanded(true);

        } else if (url.contains("Travel-Mate")) {
            mAndroidContributors = contributorsList;
            mAndroidContributorsAdapter.update(mAndroidContributors);
            android_contributors_gv.setExpanded(true);

        }
    }

    @OnClick(R.id.contributors_footer)
    void github_project_cardview_clicked() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/project-travel-mate")));
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }
}
