package tie.hackathon.travelguide.destinations.funfacts;

import org.json.JSONArray;

/**
 * Created by niranjanb on 14/06/17.
 */

public interface FunFactsView {
    void showProgressDialog();
    void hideProgressDialog();
    void setupViewPager(JSONArray factsArray);
}
