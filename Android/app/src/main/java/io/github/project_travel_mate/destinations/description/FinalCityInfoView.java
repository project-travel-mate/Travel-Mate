package io.github.project_travel_mate.destinations.description;

/**
 * Created by niranjanb on 15/05/17.
 */

interface FinalCityInfoView {
    void onPause();
    void onResume();
    void onStart();
    void onStop();
    void showProgress();
    void dismissProgress();
    void parseResult(String description,
                     String iconUrl,
                     String temp,
                     String humidity,
                     String weatherInfo,
                     String lat,
                     String lon);
}
