package objects;

public class CityHistoryListItem {

    private String mHeading;
    private String mText;

    public CityHistoryListItem(String heading, String text) {
        mHeading = heading;
        mText = text;
    }

    public String getHeading() {
        return mHeading;
    }

    public String getText() {
        return mText;
    }
}
