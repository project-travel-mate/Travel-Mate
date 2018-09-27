package io.github.project_travel_mate.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import objects.CurrencyName;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.CurrencyConverterGlobal;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class CurrencyActivity extends AppCompatActivity {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
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

    Boolean flag_check_first_item = false;
    Boolean flag_check_second_item = false;
    int from_amount = 1;
    String first_country_short = "USD";
    String second_country_short = "INR";

    private ProgressDialog mDialog;
    public static ArrayList<CurrencyName> currences_names;

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
        mDialog.setTitle(R.string.progress_wait);

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
        Intent intent = new Intent(mContext, ConversionListviewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.to_field)
    void toSelected() {
        flag_check_second_item = true;
        flag_check_first_item = false;
        result_textview.setText(String.valueOf(0.0));
        Intent intent = new Intent(mContext, ConversionListviewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_convert)
    void onConvertclicked() {
        from_amount = Integer.parseInt(from_edittext.getText().toString());
        convertCurrency();
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
                mDialog.hide();
                mHandler.post(() -> {
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

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CurrencyActivity.class);
        return intent;
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
        animationView.playAnimation();
    }

    /**
     * Plays the no data found animation in the view
     */
    private void emptyListAnimation() {
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

    /**
     * Cancel animation after receiving response from the API
     */
    private void cancelAnimation() {
        if (animationView != null) {
            animationView.cancelAnimation();
            animationView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
