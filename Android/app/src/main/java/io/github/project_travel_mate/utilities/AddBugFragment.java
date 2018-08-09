package io.github.project_travel_mate.utilities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class AddBugFragment extends Fragment implements AdapterView.OnItemSelectedListener, TravelmateSnackbars {

    @BindView(R.id.spinner_bug_type)
    Spinner mBugTypeSpinner;
    @BindView(R.id.edit_text_bugreport)
    EditText mDescriptionEditText;
    @BindView(R.id.button_report)
    Button mReportButton;
    private MaterialDialog mDialog;

    private String mType = null;
    private String mAuthToken = null;

    private Handler mHandler;
    private View mBugReportView;

    public AddBugFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment BugReportFragment.
     */
    public static AddBugFragment newInstance() {
        AddBugFragment fragment = new AddBugFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBugReportView = inflater.inflate(R.layout.fragment_add_bug, container, false);
        ButterKnife.bind(this, mBugReportView);

        mHandler = new Handler(Looper.getMainLooper());

        setupSpinner();
        mAuthToken = getAuthToken();

        return mBugReportView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bug_types, android.R.layout.simple_spinner_item);
        mBugTypeSpinner.setOnItemSelectedListener(this);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBugTypeSpinner.setAdapter(spinnerAdapter);
    }

    @OnClick(R.id.button_report)
    public void onReportClicked() {
        String description = mDescriptionEditText.getText().toString();

        if (!description.trim().isEmpty()) {
            sendIssue(description);
        }
    }

    /**
     *
     * @param description description of the issue/feedback
     *                    type by user
     */
    private void sendIssue(String description) {
        showProgressDialog();

        String url = API_LINK_V2 + "add-feedback";

        //Set up client
        OkHttpClient client = new OkHttpClient();
        // post request params
        RequestBody formBody = new FormBody.Builder()
                .add("text", description)
                .add("type", mType)
                .build();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mAuthToken)
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "message : " + e.getMessage());
                hideProgressDialog();
                TravelmateSnackbars.createSnackBar(mBugReportView.findViewById(R.id.fragmentBugReport),
                        R.string.failure_message_bugreport, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // will be true only if the status code is in the range of [200..300)
                    Log.d("RESPONSE : ", "success" + response.toString());
                    hideProgressDialog();
                    TravelmateSnackbars.createSnackBar(mBugReportView.findViewById(R.id.fragmentBugReport),
                            R.string.success_message_bugreport, Snackbar.LENGTH_LONG).show();
                    resetEdittextAndSpinner();
                } else {
                    hideProgressDialog();
                    TravelmateSnackbars.createSnackBar(mBugReportView.findViewById(R.id.fragmentBugReport),
                            getString(R.string.failure_message_bugreport), Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    public String getAuthToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    private void showProgressDialog() {
        mDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();
    }

    private void hideProgressDialog() {
        mHandler.post(() -> mDialog.dismiss());
    }

    private void resetEdittextAndSpinner() {
        mHandler.post(() -> {
            mDescriptionEditText.getText().clear();
            mBugTypeSpinner.setSelection(0); // reset to the first item
        });
    }
}