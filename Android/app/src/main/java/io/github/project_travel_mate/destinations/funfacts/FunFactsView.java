package io.github.project_travel_mate.destinations.funfacts;

import org.json.JSONArray;

/**
 * Created by niranjanb on 14/06/17.
 */

interface FunFactsView {
    void showProgressDialog();
    void hideProgressDialog();
    void setupViewPager(JSONArray factsArray);
}
