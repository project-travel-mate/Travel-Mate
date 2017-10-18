package tie.hackathon.travelguide.Travel;

import tie.hackathon.travelguide.R;

/**
 * Created by zoolander on 10/18/17.
 */

public class TravelPresenter {

    private TravelView view;

    public void bind(TravelView view) {
        this.view = view;
    }

    public void unbind() {
        view = null;
    }

    public void onMenuButtonClick(int id) {
        switch (id) {
            case R.id.vehicle:
                view.startVehicleActivity();
                break;

            case R.id.shopping:
                view.startShoppingCurrentCityActivity();
                break;

            case R.id.accomo:
                view.startHotelsActivity();
                break;

            case R.id.realtime:
                view.startMapRealTimeActivity();
                break;

            case R.id.mytrips:
                view.startMyTripsActivity();
                break;
        }
    }


}
