package objects;

public class ZoneName {

    public String shortName;
    public String abrivation;

    public ZoneName(String shortName, String abrivation) {
        this.shortName = shortName;
        this.abrivation = abrivation;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAbrivation() {
        return abrivation;
    }

    public void setAbrivation(String abrivation) {
        this.abrivation = abrivation;
    }

}
