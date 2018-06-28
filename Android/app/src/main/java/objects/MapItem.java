package objects;

/**
 * Model class for city object
 */
public class MapItem {

    private final String mName;
    private final String mNumber;
    private final String mWebsite;
    private final String mAddress;

    /**
     * Initiates MapItem object
     *
     * @param name    name of the place
     * @param number  contact details
     * @param website url of the place
     * @param address physical address of the place
     */
    public MapItem(String name, String number, String website, String address) {
        this.mName = name;
        this.mNumber = number;
        this.mWebsite = website;
        this.mAddress = address;
    }

    public String getName() {
        return mName;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public String getAddress() {
        return mAddress;
    }
}