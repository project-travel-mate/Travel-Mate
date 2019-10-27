package io.github.project_travel_mate.utilities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.dd.processbutton.FlatButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.MainActivity;
import io.github.project_travel_mate.R;
import objects.Quote;
import objects.QuoteGroup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.DailyQuotesManager;

import static android.view.View.GONE;
import static utils.Constants.QUOTES_API_LINK;
import static utils.Constants.USER_TOKEN;

public class DailyQuotesFragment extends Fragment {

    private Handler mHandler;
    private ViewHolder mHolder;
    private File mFile;

    public DailyQuotesFragment() {
        // Required empty public constructor
    }

    public static DailyQuotesFragment newInstance() {
        return new DailyQuotesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_quotes, container, false);
        mHolder = new ViewHolder(view);

        getQuote();
        return view;
    }

    private void getQuote() {
        mHolder.animationView.setVisibility(View.VISIBLE);
        mHolder.animationView.playAnimation();

        // to fetch mCity names
        String uri = QUOTES_API_LINK + "travel" + ".json";

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder().url(uri).build();

        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {

                        String res;
                        try {
                            res = Objects.requireNonNull(response.body()).string();
                        } catch (IOException e) {
                            Log.e("Unable to read data", e.getMessage());
                            networkError();
                            return;
                        }
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        QuoteGroup quoteGroup = gson.fromJson(res, QuoteGroup.class);
                        int totalQuotes = quoteGroup.getQuotes().size();

                        Quote randomQuote = quoteGroup.getQuotes().get(new Random().nextInt(totalQuotes));
                        mHolder.quoteTv.setText(randomQuote.getQuote());
                        if (!randomQuote.getAuthor().isEmpty() && !randomQuote.getAuthor().equals("null")) {
                            mHolder.authorTv.setVisibility(View.VISIBLE);
                            mHolder.authorTv.setText(randomQuote.getAuthor());
                        } else {
                            mHolder.authorTv.setVisibility(View.GONE);
                        }

                        mHolder.animationView.setVisibility(GONE);

                    } else {
                        networkError();
                    }
                });
            }
        });
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        mHolder.animationView.setVisibility(View.VISIBLE);
        mHolder.animationView.setAnimation(R.raw.network_lost);
        mHolder.animationView.playAnimation();
    }

    /**
     * Takes screenshot of current screen
     *
     * @param view to be taken screenshot of
     * @return bitmap of the screenshot
     */
    private static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * Check if checkbox to don't show daily quote again is checked,
     * then close the fragment
     */
    void continueClicked() {
        if (getFragmentManager() != null) {
            if (mHolder.quotesCheckBox.isChecked()) {
                DailyQuotesManager.dontShowQuotes(getContext());
            }
            closeQuoteFragment();
        }
    }

    private void closeQuoteFragment() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();

            if (PreferenceManager.getDefaultSharedPreferences(getContext()).getString(USER_TOKEN, null) != null) {
                Intent intent = MainActivity.getStartIntent(getActivity());
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }
        }
    }

    /**
     * This method hides the unnecessary controls before screenshotting
     * the currently displayed quote
     */
    @SuppressLint("RestrictedApi")
    void shareClicked() {
        this.mHolder.continueButton.setVisibility(View.GONE);
        this.mHolder.quotesCheckBox.setVisibility(View.GONE);
        this.mHolder.share.setVisibility(View.GONE);

        Bitmap b = getScreenShot(this.mHolder.view);
        store(b, "myfile" + System.currentTimeMillis() + ".png");
        shareImage(mFile);

        this.mHolder.continueButton.setVisibility(View.VISIBLE);
        this.mHolder.quotesCheckBox.setVisibility(View.VISIBLE);
        this.mHolder.share.setVisibility(View.VISIBLE);
    }

    /**
     * Store bitmap mFile in MyScreenshots directory
     *
     * @param bitmap   bitmap to be saved
     * @param fileName Name of bitmap mFile
     */
    private void store(Bitmap bitmap, String fileName) {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyScreenshots";
        File dr = new File(dir);
        if (!dr.exists()) dr.mkdirs();
        mFile = new File(dr, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To Share a mFile via mFile sharer
     *
     * @param file File location to be shared
     */
    private void shareImage(File file) {
        Uri uri = FileProvider
                .getUriForFile(Objects
                        .requireNonNull(getActivity()), "io.github.project_travel_mate.shareFile", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_daily_quote));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }

    class ViewHolder {
        @BindView(R.id.root_layout)
        LinearLayout rootLayout;
        @BindView(R.id.animation_view)
        LottieAnimationView animationView;
        @BindView(R.id.quote_textView)
        TextView quoteTv;
        @BindView(R.id.author_textView)
        TextView authorTv;
        @BindView(R.id.fab)
        FloatingActionButton share;
        @BindView(R.id.continue_button)
        FlatButton continueButton;
        @BindView(R.id.dont_show_quotes_checkBox)
        CheckBox quotesCheckBox;
        View view;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.view = view;
        }

        @OnClick(R.id.fab)
        public void onFloatingActionBarClicked() {
            shareClicked();
        }

        @OnClick(R.id.continue_button)
        public void onContinueButtonClicked() {
            continueClicked();
        }

    }
}
