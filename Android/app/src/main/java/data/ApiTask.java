package data;

import java.util.List;

import data.models.AllCitiesModel.AllCities;
import data.models.AutocompleteModel.Autocomplete;
import data.models.CityInfoModel.CityInfoModel;
import data.models.FunFactsModel.CityFunFactsModel;
import retrofit2.Call;
import retrofit2.Callback;
import tie.hackathon.travelguide.destinations.funfacts.FunFacts;

public class ApiTask {
    private static ApiTask apiTask;

    public static ApiTask getInstance() {
        if (apiTask == null)
            apiTask = new ApiTask();
        return apiTask;
    }

    public Call<AllCities> allCitiesApi(Callback<AllCities> callback) {
        Call<AllCities> call = data.Retrofit.getRetrofitInstance().create(ApiInterface.class).callAllCitiesApi();
        call.enqueue(callback);
        return call;
    }

    public Call<CityInfoModel> cityInfoApi(String id, Callback<CityInfoModel> callback) {
        Call<CityInfoModel> call = data.Retrofit.getRetrofitInstance().create(ApiInterface.class).callCityInfoApi(id);
        call.enqueue(callback);
        return call;
    }

    public Call<CityFunFactsModel> funFactsApi(String id, Callback<CityFunFactsModel> callback) {
        Call<CityFunFactsModel> call = data.Retrofit.getRetrofitInstance().create(ApiInterface.class).callFunFactsApi(id);
        call.enqueue(callback);
        return call;
    }

    public Call<List<Autocomplete>> autocompleteApi(String searchText, Callback<List<Autocomplete>> callback) {
        Call<List<Autocomplete>> call = data.Retrofit.getRetrofitInstance().create(ApiInterface.class).callAutocompleteApi(searchText);
        call.enqueue(callback);
        return call;
    }
}
