package io.github.project_travel_mate.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.github.project_travel_mate.R;
import objects.ZoneName;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.CurrencyConverterGlobal;
import utils.DateUtils;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class CurrencyActivity extends AppCompatActivity {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.actual_layout)
    View actual_layout;
    @BindView(R.id.first_country_image)
    ImageView from_image;
    @BindView(R.id.second_country_flag)
    ImageView to_image;
    @BindView(R.id.first_country_edittext)
    EditText from_edittext;
    @BindView(R.id.text_result)
    TextView result_textview;
    @BindView(R.id.first_country_name)
    TextView from_country_name;
    @BindView(R.id.second_country_name)
    TextView to_country_name;
    @BindView(R.id.graph)
    LineChart graph;
    @BindView(R.id.chart_duration_spinner)
    Spinner chart_duration_spinner;


    Boolean flag_check_first_item = false;
    Boolean flag_check_second_item = false;
    Boolean flag_convert_pressed = false;


    int from_amount = 1;
    String first_country_short = "USD";
    String second_country_short = "INR";
    String GRAPH_LABEL_NAME = "Last 7 days currency rate trends";

    private ProgressDialog mDialog;
    public static ArrayList<ZoneName> currences_names;

    public static String sDefSystemLanguage;
    private Context mContext;
    private Handler mHandler;
    private String mToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_currency_converter);
        ButterKnife.bind(this);
        setTitle(R.string.text_currency);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getResources().getString(R.string.progress_wait));
        mDialog.setTitle(R.string.app_name);
        mDialog.setCancelable(false);

        sDefSystemLanguage = Locale.getDefault().getLanguage();

        currences_names = new ArrayList<>();
        mHandler = new Handler(Looper.getMainLooper());
        mContext = this;

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.from_field)
    void fromSelected() {
        flag_check_first_item = true;
        flag_check_second_item = false;
        result_textview.setText(String.valueOf(0.0));
        Intent intent = new Intent(mContext, CurrencyListViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.to_field)
    void toSelected() {
        flag_check_second_item = true;
        flag_check_first_item = false;
        result_textview.setText(String.valueOf(0.0));
        Intent intent = new Intent(mContext, CurrencyListViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_convert)
    void onConvertclicked() {

        from_amount = Integer.parseInt(from_edittext.getText().toString());
        if (new Connection().isOnline()) {
            convertCurrency();
            int chartDays = getChartTimeInterval();
            currencyRate(chartDays);
            flag_convert_pressed = true;
            utils.Utils.hideKeyboard(this);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.check_internet);
            builder.setPositiveButton(R.string.positive_button, (dialog, which) -> dialog.dismiss());
            builder.show();
        }
    }

    @OnItemSelected(R.id.chart_duration_spinner)
    void onChartDurationSpinnerClicked() {
        // Flag is implemented here so that default behaviour of spinner's
        // on item selected doesn't trigger when activity is first launched
        if (flag_convert_pressed) {
            onConvertclicked();
            GRAPH_LABEL_NAME = "Last " + chart_duration_spinner.getSelectedItem() + " currency rate trends";
        }
    }


    // Method to obtain value(last x number of days) to be passed to API
    int getChartTimeInterval() {
        switch (chart_duration_spinner.getSelectedItemPosition()) {
            default:
                return 6;
            case 0:
                return 6;
            case 1:
                return 30;
            case 2:
                return 60;
            case 3:
                return 180;
            case 4:
                return 364;

        }
    }


    void setupGraph(JSONArray currencyRateTrends) {
        if (currencyRateTrends == null || currencyRateTrends.length() == 0) {
            graph.setVisibility(View.GONE);
            chart_duration_spinner.setVisibility(View.INVISIBLE);
        } else {
            graph.setVisibility(View.VISIBLE);
            chart_duration_spinner.setVisibility(View.VISIBLE);
            graph.getXAxis().setEnabled(false);
            graph.getAxisLeft().setEnabled(false);
            graph.getAxisRight().setEnabled(false);
            graph.getDescription().setText("");
            setGraphData(currencyRateTrends);
            graph.animateX(1000);
            Legend l = graph.getLegend();
            l.setForm(Legend.LegendForm.LINE);
        }
    }

    void setGraphData(JSONArray currencyRateTrends) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < currencyRateTrends.length(); i++) {
            try {
                values.add(new Entry(i, (float) currencyRateTrends.getDouble(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LineDataSet lineDataSet = new LineDataSet(values, GRAPH_LABEL_NAME);
        lineDataSet.setDrawIcons(false);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setCircleRadius(1f);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormSize(10.f);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_green);
            lineDataSet.setFillDrawable(drawable);
        } else {
            lineDataSet.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        graph.setData(data);

    }

    /**
     * Convert currency
     * and set result to result_textview
     */
    private void convertCurrency() {
        // to fetch weather forecast by city name
        String uri = API_LINK_V2 + "get-currency-conversion-rate/" + first_country_short + "/" + second_country_short;
        Log.v("EXECUTING", uri);

        mDialog.show();
        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();

        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> {
                    mDialog.hide();
                    Log.e("Request Failed", "Message : " + e.getMessage());
                    cancelAnimation();
                    networkError();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonResponse = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    mDialog.hide();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject result = new JSONObject(jsonResponse);
                            if (result.getString("result") != null) {
                                Double conversion = Double.parseDouble(result.getString("result"));
                                result_textview.setText(Double.toString(conversion * from_amount));
                            }
                            //cancel the loading animation once the data is fetched
                            cancelAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //display no data error animation
                            emptyListAnimation();
                        }
                    } else {
                        //display animation for no data returned from the API call
                        emptyListAnimation();
                    }
                });
            }
        });
    }

    /**
     * currency rate conversion
     * and set result to graph
     */
    private void currencyRate(int totalDays) {

        String uri = API_LINK_V2 + "get-all-currency-rate/"
                + DateUtils.getDate(totalDays) + "/" + DateUtils.getDate(0) + "/"
                + first_country_short.toLowerCase() + "/" + second_country_short.toLowerCase();

        mDialog.show();
        //Set up client

        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();

        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mDialog.hide();
                mHandler.post(() -> {
                    cancelAnimation();
                    networkError();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonResponse = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    mDialog.hide();
                    if (response.isSuccessful()) {
                        try {
                            JSONArray result = new JSONArray(jsonResponse);
                            setupGraph(result);
                            //cancel the loading animation once the data is fetched
                            cancelAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //display no data error animation
                            emptyListAnimation();
                        }
                    } else {
                        //display animation for no data returned from the API call
                        emptyListAnimation();
                    }
                });
            }
        });


    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, CurrencyActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag_check_first_item) {
            if (CurrencyConverterGlobal.global_image_id != 0 &&
                    !CurrencyConverterGlobal.global_country_name.isEmpty()) {
                from_image.setImageResource(CurrencyConverterGlobal.global_image_id);
                from_country_name.setText(CurrencyConverterGlobal.global_country_name);
                first_country_short = CurrencyConverterGlobal.country_id;
                Log.d("Key", first_country_short);
            }
        }

        if (flag_check_second_item) {
            if (CurrencyConverterGlobal.global_image_id != 0 &&
                    !CurrencyConverterGlobal.global_country_name.isEmpty()) {
                to_image.setImageResource(CurrencyConverterGlobal.global_image_id);
                to_country_name.setText(CurrencyConverterGlobal.global_country_name);
                second_country_short = CurrencyConverterGlobal.country_id;
            }
        }
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        actual_layout.setVisibility(View.GONE);
        animationView.playAnimation();
    }

    /**
     * Plays the no data found animation in the view
     */
    private void emptyListAnimation() {
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.empty_list);
        actual_layout.setVisibility(View.GONE);
        animationView.playAnimation();
    }

    /**
     * Cancel animation after receiving response from the API
     */
    private void cancelAnimation() {
        if (animationView != null) {
            animationView.cancelAnimation();
            animationView.setVisibility(View.GONE);
            actual_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //    TODO : Move to different class
    class Connection {
        boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
            return info != null && info.isConnectedOrConnecting();
        }
    }

    /**
     * Error animation will be cancelled if user presses back while the animation is showing
     */
    @Override
    public void onBackPressed() {
        if (animationView != null) {
            if (animationView.getVisibility() == View.VISIBLE) {
                cancelAnimation();
                return;
            }
        }
        super.onBackPressed();
    }
}
