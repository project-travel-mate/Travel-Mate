package objects;

/**
 * Model class for city object
 */
public class MapItem {

    private final String name;
    private final String number;
    private final String website;
    private final String address;

    public MapItem(String name, String number, String website, String address) {
        this.name       = name;
        this.number     = number;
        this.website    = website;
        this.address    = address;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }
}