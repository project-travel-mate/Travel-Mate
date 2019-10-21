package objects;

public class User {

    private String mUsername;
    private String mFirstName;
    private String mLastName;
    private int mId;
    private String mImage;
    private String mDateJoined;
    private String mStatus;

    public User(String mUsername, String mFirstName, String mLastName,
                int mId, String mImage, String mDateJoined, String mStatus) {
        this.mUsername = mUsername;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mId = mId;
        this.mImage = mImage;
        this.mDateJoined = mDateJoined;
        this.mStatus = mStatus;
    }

    public User(String firstName, String image) {
        this.mFirstName = firstName;
        this.mImage = image;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public int getId() {
        return mId;
    }

    public String getImage() {
        return mImage;
    }

    public String getDateJoined() {
        return mDateJoined;
    }

    public String getStatus() {
        return mStatus;
    }
}
