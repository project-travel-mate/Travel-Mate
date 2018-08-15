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
    private int mBackgroundColor;


    public City(String mId, String mAvatar, String mNickname, int funFactsCount, int color, String... interest) {
        this.mAvatar = mAvatar;
        this.mNickname = mNickname;
        this.mId = mId;
        this.mFunFactsCount = funFactsCount;
        this.mBackgroundColor = color;
        mInterests.addAll(Arrays.asList(interest));
    }

    public City(String nickname, String id) {
        this.mNickname = nickname;
        this.mId = id;
    }

    public City(String id, String nickname, String avatar) {
        this.mId = id;
        this.mNickname = nickname;
        this.mAvatar = avatar;
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

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }
}