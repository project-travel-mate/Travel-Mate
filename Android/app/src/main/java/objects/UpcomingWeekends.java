package objects;

import java.io.Serializable;

public class UpcomingWeekends implements Serializable {

    private String mMonth;
    private int mDate;
    private String mDay;
    private String mName;
    private String mType;

    public UpcomingWeekends() {
    }

    /**
     * Object to hold info about upcoming events
     *
     * @param mMonth - month of the event
     * @param mDate - day of the month in int of the event
     * @param mDay - day of the week in string of the event (eg. Friday)
     * @param mName - name of the event
     * @param mType - type of the event
     */
    public UpcomingWeekends(String mMonth, int mDate, String mDay, String mName, String mType) {
        this.mMonth = mMonth;
        this.mDate = mDate;
        this.mDay = mDay;
        this.mName = mName;
        this.mType = mType;
    }

    public String getmMonth() {
        return mMonth;
    }

    public int getmDate() {
        return mDate;
    }

    public String getmDay() {
        return mDay;
    }

    public String getmName() {
        return mName;
    }

    public String getmType() {
        return mType;
    }

}
