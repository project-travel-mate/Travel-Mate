package objects;

/**
 * Created by swati on 25/1/16.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yalantis
 */
public class Friend {
    private String avatar;
    private String nickname, id, la, lo;
    private int background;
    private List<String> interests = new ArrayList<>();

    public Friend(String id, String avatar, String nickname, int background, String lat, String lon, String... interest) {
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