package utils;

import java.util.ArrayList;
import java.util.List;

import io.github.project_travel_mate.R;

/**
 * Created by swati on 11/10/15.
 */
public class Constants {
    public static final String ID_ADDED_INDB = "is_added_in_db";
    public static final String USER_NAME = "user_name";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_STATUS = "user_status";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_DATE_JOINED = "user_date_joined";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_ID = "user_id";
    public static final String OTHER_USER_ID = "other_user_id";

    public static final String EVENT_IMG = "event_name";
    public static final String EVENT_NAME = "event_img";
    public static final String IMAGE_NO = "number";

    public static final String SOURCE_CITY = "source_city";
    public static final String DESTINATION_CITY = "destination_city";

    public static final String SOURCE_CITY_ID = "source_city_id";
    public static final String DESTINATION_CITY_ID = "destination_city_id";
    public static final String DESTINATION_CITY_LAT = "destination_city_lat";
    public static final String DESTINATION_CITY_LON = "destination_city_lon";

    public static final String MUMBAI_LAT = "19.076";
    public static final String MUMBAI_LON = "72.8777";

    public static final String API_LINK_V2 = "https://project-travel-mate.herokuapp.com/api/";
    public static final String maps_key = "AIzaSyBgktirlOODUO9zWD-808D7zycmP7smp-Y";

    // TODO:: replace placeholders with actual values
    //Cloudinary information
    public static final String CLOUDINARY_CLOUD_NAME = "sample_cloud";
    public static final String CLOUDINARY_API_KEY = "sample_api_key";
    public static final String CLOUDINARY_API_SECRET = "sample_api_secret";


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

    //share profile strings
    public static final String SHARE_PROFILE_URI = "http://project-travel-mate.github.io/Travel-Mate";
    public static final String SHARE_PROFILE_USER_ID_QUERY = "user";

    //QRCode constants
    public static final int QR_CODE_WIDTH = 200;
    public static final int QR_CODE_HEIGHT = 200;

    // What's new in version code 16
    public static final String WHATS_NEW1_TITLE = "Share Contact";
    public static final String WHATS_NEW1_TEXT =
            "Share your contact more easily with friends. Checkout Share Contact in Utilities section";
    public static final String WHATS_NEW2_TITLE = "Search More places to visit";
    public static final String WHATS_NEW2_TEXT = "Search the destinations to visit more easily on home page.";
    public static final String WHATS_NEW3_TITLE = "My Profile";
    public static final String WHATS_NEW3_TEXT = "Add a status to your profile.";
    public static final String WHATS_NEW4_TITLE = "My Trips";
    public static final String WHATS_NEW4_TEXT = "Improved User interface.";

}