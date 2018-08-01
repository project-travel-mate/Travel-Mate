package objects;

public class TweetDescription {

    private String mUsername;
    private String mUserScreenName;
    private String mFavCount;
    private String mRetweetsCount;
    private String mCreatedAt;
    private String mTweetText;
    private String mUserAvatar;

    public TweetDescription(String username, String userScreenName, String createdAt,
                            String avatar, String favCount, String retweetsCount, String text) {
        mUsername = username;
        mUserScreenName = userScreenName;
        mCreatedAt = createdAt;
        mFavCount = favCount;
        mRetweetsCount = retweetsCount;
        mUserAvatar = avatar;
        mTweetText = text;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getFavCount() {
        return mFavCount;
    }

    public String getRetweetsCount() {
        return mRetweetsCount;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getTweetText() {
        return mTweetText;
    }

    public String getAvatar() {
        return mUserAvatar;
    }

    public String getUserScreenName() {
        return mUserScreenName;
    }
}
