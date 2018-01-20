package tie.hackathon.travelguide.destinations.description;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import data.ApiTask;
import data.models.AutocompleteModel.Autocomplete;
import data.models.CityInfoModel.CityInfoModel;
import utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.GlobalClass;

/**
 * Created by niranjanb on 15/05/17.
 */

public class FinalCityInfoPresenter {
    private ApiTask apiTask;
    private CityInfoModel cityInfoModel;
    FinalCityInfoView mFinalCityInfoView;
    OkHttpClient mOkHttpClient;
    private FinalCityInfo finalCityInfo;

    public FinalCityInfoPresenter(FinalCityInfo finalCityInfo) {
        apiTask = ApiTask.getInstance();
        this.finalCityInfo = finalCityInfo;
        mOkHttpClient = new OkHttpClient();
    }

    public void attachView(FinalCityInfoView finalCityInfoView) {
        mFinalCityInfoView = finalCityInfoView;
    }

    public void fetchCityInfo(String id) {
        mFinalCityInfoView.showProgress();

        apiTask.cityInfoApi(id, cityInfoCallback);

    }

    private retrofit2.Callback<CityInfoModel> cityInfoCallback = new retrofit2.Callback<CityInfoModel>() {
        @Override
        public void onResponse(retrofit2.Call<CityInfoModel> call, retrofit2.Response<CityInfoModel> response) {
            mFinalCityInfoView.dismissProgress();

            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    cityInfoModel = response.body();

                    mFinalCityInfoView.parseResult(cityInfoModel.getDescription(),
                            cityInfoModel.getWeather().getIcon(),
                            String.valueOf(cityInfoModel.getWeather().getTemprature()),
                            String.valueOf(cityInfoModel.getWeather().getHumidity()),
                            cityInfoModel.getWeather().getDescription(),
                            String.valueOf(cityInfoModel.getLat()), String.valueOf(cityInfoModel.getLng()));

                }
            } else {
                GlobalClass.showApiFailureMessage(finalCityInfo);
            }
        }

        @Override
        public void onFailure(retrofit2.Call<CityInfoModel> call, Throwable t) {
            mFinalCityInfoView.dismissProgress();

            GlobalClass.showApiFailureMessage(finalCityInfo);
        }
    };

}
