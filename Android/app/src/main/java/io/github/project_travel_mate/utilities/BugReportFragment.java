package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Feedback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;
import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class BugReportFragment extends Fragment {

    @BindView(R.id.issues_list)
    ListView issuesList;
    @BindView(R.id.add_feedback)
    FloatingActionButton addButton;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.listview_title)
    TextView listViewTitle;

    private String mAuthToken = null;
    private final List<Feedback> mFeedbacks = new ArrayList<>();
    private BugReportAdapter mAdapter;
    private Activity mActivity;

    public BugReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment BugReportFragment.
     */
    public static BugReportFragment newInstance() {
        BugReportFragment fragment = new BugReportFragment();
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mBugReportView = inflater.inflate(R.layout.fragment_bug_report, container, false);
        ButterKnife.bind(this, mBugReportView);

        mAuthToken = getAuthToken();

        addButton.setOnClickListener(v -> {
            Fragment fragment;
            FragmentManager fragmentManager = getFragmentManager();
            fragment = AddBugFragment.newInstance();
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.inc, fragment).commit();
        });
        return mBugReportView;
    }

    public String getAuthToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    /**
     * fetches a list of feedbacks reported by user from
     * server
     */
    private void getAllUserFeedback() {

        Handler handler = new Handler(Looper.getMainLooper());

        String url = API_LINK_V2 + "get-all-user-feedback";

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mAuthToken)
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            final String res = Objects.requireNonNull(response.body()).string();
                            JSONArray arr;
                            arr = new JSONArray(res);
                            if (arr.length() < 1) {
                                noResult();
                                return;
                            }
                            for (int i = 0; i < arr.length(); i++) {
                                String type = arr.getJSONObject(i).getString("type");
                                String text = arr.getJSONObject(i).getString("text");
                                String dateCreated = arr.getJSONObject(i).getString("created");
                                mFeedbacks.add(new Feedback(type, text, dateCreated));
                                listViewTitle.setVisibility(View.VISIBLE);
                                issuesList.setVisibility(View.VISIBLE);
                                mAdapter = new BugReportAdapter(mActivity, mFeedbacks);
                                issuesList.setAdapter(mAdapter);
                                animationView.setVisibility(GONE);
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        handler.post(() -> noResult());
                    }
                });
            }
        });
    }

    private void noResult() {
        textView.setVisibility(View.VISIBLE);
        listViewTitle.setVisibility(GONE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.no_feedback);
        animationView.playAnimation();
    }

    private void networkError() {
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFeedbacks.clear();
        getAllUserFeedback();
    }
}