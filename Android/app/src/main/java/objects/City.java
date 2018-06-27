package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class for city object
 */
public class City implements Serializable {

    private final List<String> mInterests = new ArrayList<>();
    private String mAvatar;
    private String mNickname;
    private String mDescription;
    private String mId;
    private String mLatitude;
    private String mLongitude;
    private int mFunFactsCount;

    /**
     * Instantiates city object
     *
     * @param id          unique id for a city
     * @param avatar      city image
     * @param nickname    city name
     * @param description city description
     * @param lat         latitude of city
     * @param lon         longitude of city
     * @param funFactsCount count of fun facts for the city
     * @param interest    list of items when string is opened
     */
    public City(String id, String avatar, String nickname, String description,
                String lat, String lon,  int funFactsCount, String... interest) {
        this.mAvatar = avatar;
        this.mId = id;
        this.mLatitude = lat;
        this.mLongitude = lon;
        this.mNickname = nickname;
        this.mDescription = description;
        this.mFunFactsCount = funFactsCount;
        mInterests.addAll(Arrays.asList(interest));
    }

    public City(String mId, String mAvatar, String mNickname, int funFactsCount, String... interest) {
        this.mAvatar = mAvatar;
        this.mNickname = mNickname;
        this.mId = mId;
        this.mFunFactsCount = funFactsCount;
        mInterests.addAll(Arrays.asList(interest));
    }

    public String getAvatar() {
        return mAvatar;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getId() {
        return mId;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public List<String> getInterests() {
        return mInterests;
    }

    public int getFunFactsCount() {
        return mFunFactsCount;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }
}