package objects;

/**
 * Model class for city object
 */
public class MapItem {

    private final String mName;
    private final String mNumber;
    private final String mWebsite;
    private final String mAddress;

    public MapItem(String name, String number, String website, String address) {
        this.mName = name;
        this.mNumber = number;
        this.mWebsite = website;
        this.mAddress = address;
    }

    public String getmName() {
        return mName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public String getmWebsite() {
        return mWebsite;
    }

    public String getmAddress() {
        return mAddress;
    }
}