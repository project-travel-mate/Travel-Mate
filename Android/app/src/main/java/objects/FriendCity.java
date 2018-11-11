package objects;

public class FriendCity {
    private int mId;
    private String mCityName;
    private String mNickname;
    private int mFactsCount;
    private String mImage;

    public FriendCity(int id, String cityName, String nickname, int factsCount, String image) {
        this.mId = id;
        this.mCityName = cityName;
        this.mNickname = nickname;
        this.mFactsCount = factsCount;
        this.mImage = image;
    }

    public int getId() {
        return mId;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getNickname() {
        return mNickname;
    }

    public int getFactsCount() {
        return mFactsCount;
    }

    public String getImage() {
        return mImage;
    }
}
