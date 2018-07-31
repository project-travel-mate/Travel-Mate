package objects;

import java.io.Serializable;

public class Notification implements Serializable {

    private int mId;
    private String mCreatedAt;
    private String mType;
    private String mText;
    private boolean mIsRead;
    private User mCreatedBy;
    private Trip mTrip;

    /**
     * Object to store notification
     *
     * @param mId - notification id
     * @param mType - notification tyoe
     * @param mText - notification text
     * @param mIsRead - if notfication is read
     * @param mCreatedBy - user who created this notification
     * @param mTrip - trip to which user belongs
     * @param mCreatedAt - time at which user was added to trip
     */
    public Notification(int mId, String mType, String mText, boolean mIsRead,
                        User mCreatedBy, Trip mTrip, String mCreatedAt) {
        this.mId = mId;
        this.mType = mType;
        this.mText = mText;
        this.mIsRead = mIsRead;
        this.mCreatedBy = mCreatedBy;
        this.mTrip = mTrip;
        this.mCreatedAt = mCreatedAt;
    }

    public User getCreatedBy() {
        return mCreatedBy;
    }

    public Trip getTrip() {
        return mTrip;
    }

    public void setCreatedBy(User createdBy) {
        this.mCreatedBy = createdBy;
    }

    public boolean isRead() {
        return mIsRead;
    }

    public void setRead(boolean mIsRead) {
        this.mIsRead = mIsRead;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }
}
