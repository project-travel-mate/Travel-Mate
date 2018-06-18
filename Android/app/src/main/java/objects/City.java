package objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class for city object
 */
public class City implements Serializable {

    private String mAvatar;
    private String mNickname;
    private String mDescription;
    private String mId;
    private String mLatitude;
    private String mLongitude;
    private int mBackground;
    private final List<String> mInterests = new ArrayList<>();

    /**
     * Instantiates city object
     *
     * @param id         unique id for a city
     * @param avatar     city image
     * @param nickname   city name
     * @param description   city description
     * @param background Background color when card is opened
     * @param lat        latitude of city
     * @param lon        longitude of city
     * @param interest   list of items when string is opened
     */
    public City(String id, String avatar, String nickname, String description,
                int background, String lat, String lon, String... interest) {
        this.mAvatar = avatar;
        this.mId = id;
        this.mLatitude = lat;
        this.mLongitude = lon;
        this.mNickname = nickname;
        this.mDescription = description;
        this.mBackground = background;
        mInterests.addAll(Arrays.asList(interest));
    }

    public City( String mId, String mAvatar, String mNickname) {
        this.mAvatar = mAvatar;
        this.mNickname = mNickname;
        this.mId = mId;
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

    public int getBackground() {
        return mBackground;
    }

    public List<String> getInterests() {
        return mInterests;
    }

}