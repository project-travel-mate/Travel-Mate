package objects;

/**
 * Created by swati on 25/1/16.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model class for city object
 */
public class City {
    private final String avatar;
    private final String nickname;
    private final String id;
    private final String la;
    private final String lo;
    private final int background;
    private final List<String> interests = new ArrayList<>();

    /**
     * Instantiates city object
     *
     * @param id         unique id for a city
     * @param avatar     city image
     * @param nickname   city name
     * @param background Background color when card is opened
     * @param lat        latitude of city
     * @param lon        longitude of city
     * @param interest   list of items when string is opened
     */
    public City(String id, String avatar, String nickname, int background, String lat, String lon, String... interest) {
        this.avatar = avatar;
        this.id = id;
        this.la = lat;
        this.lo = lon;
        this.nickname = nickname;
        this.background = background;
        interests.addAll(Arrays.asList(interest));
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getId() {
        return id;
    }

    public String getLa() {
        return la;
    }

    public String getLo() {
        return lo;
    }

    public int getBackground() {
        return background;
    }

    public List<String> getInterests() {
        return interests;
    }

}