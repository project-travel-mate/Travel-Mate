package objects;

public class Trip {

    private String mId;
    private String mName;
    private String mImage;
    private String mStart;
    private String mEnd;
    private String mTname;

    public Trip() {

    }

    public Trip(String id, String name, String image, String start, String end, String tname) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mStart = start;
        this.mEnd = end;
        this.mTname = tname;
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

    public String getmTname() {
        return mTname;
    }


}
