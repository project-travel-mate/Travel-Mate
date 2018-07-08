package objects;

public class Weather {

    private int mImageId;
    private int mMinTemp;
    private int mMaxTemp;
    private String mDayOfWeek;
    private String mDate;

    public Weather(int imageId, int maxTemp, int minTemp, String dayOfWeek, String date) {
        this.mImageId = imageId;
        this.mMaxTemp = maxTemp;
        this.mMinTemp = minTemp;
        this.mDayOfWeek = dayOfWeek;
        this.mDate = date;
    }

    public int getmMinTemp() {
        return mMinTemp;
    }

    public int getmMaxTemp() {
        return mMaxTemp;
    }

    public int getmImageId() {
        return mImageId;
    }

    public String getmDayOfWeek() {
        return mDayOfWeek;
    }

    public String getmDate() {
        return mDate;
    }

}
