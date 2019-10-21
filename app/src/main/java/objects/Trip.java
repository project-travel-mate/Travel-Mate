package objects;

import java.io.Serializable;

public class Trip implements Serializable {

    private String mId;
    private String mName;
    private String mImage;
    private String mStart;
    private String mEnd;
    private String mTname;
    private boolean mIsPublic;

    public Trip() {
    }

    /**
     * Initiates trip object
     *
     * @param id    trip id
     * @param name  name of the city
     * @param image trip icon
     * @param start start date of trip
     * @param end   end date of trip
     * @param tname name of trip
     * @param isPublic  is trip public or private
     */
    public Trip(String id, String name, String image, String start, String end, String tname, boolean isPublic) {

        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mStart = start;
        this.mEnd = end;
        this.mTname = tname;
        this.mIsPublic = isPublic;
    }

    public Trip(String mId, String mImage) {
        this.mId = mId;
        this.mImage = mImage;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getImage() {
        return mImage;
    }

    public String getStart() {
        return mStart;
    }

    public String getEnd() {
        return mEnd;
    }

    public String getTname() {
        return mTname;
    }

    public boolean isPublic() {
        return mIsPublic;
    }
    public void setId(String id) {
        this.mId = id;
    }



}
