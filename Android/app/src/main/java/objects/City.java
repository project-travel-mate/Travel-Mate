package objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class for city object
 */
public class City {
    private final String mAvatar;
    private final String mNickname;
    private final String mId;
    private final String mLatitude;
    private final String mLongitude;
    private final int mBackground;
    private final List<String> mInterests = new ArrayList<>();

    /**
     * Instantiates city object
     *
     * @param id         unique id for a city
     * @param avatar     city image
     * @param nickname   city name
     * @param background Background color when card is opened
     * @param lat        latitude of city
     * @param lon        longitude of city
     * @param interest   list of items when string is opened
     */
    public City(String id, String avatar, String nickname, int background, String lat, String lon, String... interest) {
        this.mAvatar = avatar;
        this.mId = id;
        this.mLatitude = lat;
        this.mLongitude = lon;
        this.mNickname = nickname;
        this.mBackground = background;
        mInterests.addAll(Arrays.asList(interest));
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public String getmNickname() {
        return mNickname;
    }

    public String getId() {
        return mId;
    }

    public String getmLatitude() {
        return mLatitude;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public int getmBackground() {
        return mBackground;
    }

    public List<String> getmInterests() {
        return mInterests;
    }

}