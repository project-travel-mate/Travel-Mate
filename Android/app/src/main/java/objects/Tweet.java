package objects;

public class Tweet {

    private String name;
    private String url;
    private String volume;

    public Tweet(String name, String url, String volume) {
        this.name = name;
        this.url = url;
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getVolume() {
        return volume;
    }
}
