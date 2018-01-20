package tie.hackathon.travelguide.destinations.funfacts;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import data.ApiTask;
import data.models.AutocompleteModel.Autocomplete;
import data.models.FunFactsModel.CityFunFactsModel;
import tie.hackathon.travelguide.R;
import utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.GlobalClass;

/**
 * Created by niranjanb on 14/06/17.
 */

public class FunFactsPresenter {
    private ApiTask apiTask;
    FunFactsView mFunFactsView;
    private FunFacts funFacts;
    private CityFunFactsModel funFactsModel;

    public FunFactsPresenter(FunFactsView funFactsView, FunFacts funFacts) {
        apiTask = ApiTask.getInstance();
        mFunFactsView = funFactsView;
        this.funFacts = funFacts;
    }

    public void initPresenter(String id) {
        getCityFacts(id);
    }

    // Fetch fun facts about city
    private void getCityFacts(String id) {
        mFunFactsView.showProgressDialog();

        apiTask.funFactsApi(id, funFactsCallback);
    }


    private retrofit2.Callback<CityFunFactsModel> funFactsCallback = new retrofit2.Callback<CityFunFactsModel>() {
        @Override
        public void onResponse(retrofit2.Call<CityFunFactsModel> call, retrofit2.Response<CityFunFactsModel> response) {
            mFunFactsView.hideProgressDialog();
            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    funFactsModel = response.body();

                    mFunFactsView.setupViewPager(funFactsModel);
                }
            } else {
                GlobalClass.showApiFailureMessage(funFacts);
            }
        }

        @Override
        public void onFailure(retrofit2.Call<CityFunFactsModel> call, Throwable t) {
            mFunFactsView.hideProgressDialog();
            GlobalClass.showApiFailureMessage(funFacts);
        }
    };
}
