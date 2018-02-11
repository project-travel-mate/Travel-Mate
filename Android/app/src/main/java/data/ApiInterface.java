package data;

import android.provider.SyncStateContract;

import java.util.List;

import data.models.AllCitiesModel.AllCities;
import data.models.AutocompleteModel.Autocomplete;
import data.models.CityInfoModel.CityInfoModel;
import data.models.FunFactsModel.CityFunFactsModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import utils.Constants;

/**
 * Created by Sumeet on 12/17/2017.
 */

public interface ApiInterface {

    @GET(Constants.ALL_CITY)
    Call<AllCities> callAllCitiesApi();

    @GET(Constants.CITY_INFO)
    Call<CityInfoModel> callCityInfoApi(@Query(Constants.CITY_INFO_ID) String id);

    @GET(Constants.FUN_FACTS)
    Call<CityFunFactsModel> callFunFactsApi(@Query(Constants.FUN_FACTS_ID) String id);

    @GET(Constants.AUTOCOMPLETE)
    Call<List<Autocomplete>> callAutocompleteApi(@Query(Constants.AUTOCOMPLETE_SEARCH) String search);
}
