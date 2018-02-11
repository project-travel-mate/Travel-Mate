package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Constants;
import utils.LogJsonInterceptor;

public class Retrofit {
    private static Gson gson;

    public static retrofit2.Retrofit getRetrofitInstance() {
        if (gson == null)
            gson = new GsonBuilder().setLenient().create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new LogJsonInterceptor());
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder().client(httpClient.build()).baseUrl(Constants.apilink)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }

}
