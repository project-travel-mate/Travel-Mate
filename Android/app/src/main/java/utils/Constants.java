package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swati on 11/10/15.
 */
public class Constants {
    public static final String ID_ADDED_INDB = "is_added_in_db";
    public static final String USER_NAME = "user_name";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_TOKEN = "user_token";
    public static final String FIRST_NAME = "first_name";

    public static final String EVENT_IMG = "event_name";
    public static final String EVENT_NAME = "event_img";
    public static final String IMAGE_NO = "number";

    public static final String SOURCE_CITY = "source_city";
    public static final String DESTINATION_CITY = "destination_city";

    public static final String SOURCE_CITY_ID = "source_city_id";
    public static final String DESTINATION_CITY_ID = "destination_city_id";
    public static final String SOURCE_CITY_LAT = "source_city_lat";
    public static final String DESTINATION_CITY_LAT = "destination_city_lat";
    public static final String SOURCE_CITY_LON = "source_city_lon";
    public static final String DESTINATION_CITY_LON = "destination_city_lon";

    public static final String DELHI_LAT = "28.6466773";
    public static final String DELHI_LON = "76.813073";
    public static final String MUMBAI_LAT = "19.076";
    public static final String MUMBAI_LON = "72.8777";

    public static final String API_LINK = "http://prabhakargupta.com/travel-mate/";
    public static final String API_LINK_V2 = "https://project-travel-mate.herokuapp.com/api/";
    public static final String maps_key = "AIzaSyBgktirlOODUO9zWD-808D7zycmP7smp-Y";

    public static final List<String> BASE_TASKS = new ArrayList<String>() {
        {
            add("Bags");
            add("Keys");
            add("Charger");
            add("Earphones");
            add("Clothes");
            add("Food");
            add("Tickets");
        }
    };

    // For passing within intents / fragments
    public static final String EXTRA_MESSAGE_TYPE = "type_";
    public static final String EXTRA_MESSAGE_CITY_OBJECT = "cityobject_";
    public static final String EXTRA_MESSAGE_FUNFACT_OBJECT = "funfactobject_";
    public static final String EXTRA_MESSAGE_TRIP_OBJECT = "tripobject_";


    // HTTP STATUS CODES
    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_CREATED = 201;
    public static final int STATUS_CODE_UNAUTHORIZED = 403;

    // Here API
    public static final String HERE_API_LINK = "https://places.api.here.com/places/v1/discover/explore";
    public static final String HERE_API_APP_ID = "7xQMJiIPsG3ptIohUobu";
    public static final String HERE_API_APP_CODE = "iPNVovxnQdkSvsRPjPct3w";
    public static final List<String> HERE_API_MODES = new ArrayList<String>() {
        {
            add("eat-drink");
            add("going-out,leisure-outdoor");
            add("sights-museums");
            add("transport");
            add("shopping");
            add("petrol-station");
            add("atm-bank-exchange");
            add("hospital-health-care-facility");
        }
    };

}