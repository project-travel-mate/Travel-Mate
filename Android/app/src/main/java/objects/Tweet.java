package objects;

public class Tweet {

    private final String mName;
    private final String mUrl;
    private final String mVolume;

    /**
     * Initiates Tweet object
     *
     * @param name   tweet description
     * @param url    tweet url
     * @param volume tweet volume
     */
    public Tweet(String name, String url, String volume) {
        this.mName = name;
        this.mUrl = url;
        this.mVolume = volume;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getVolume() {
        return mVolume;
    }
}
