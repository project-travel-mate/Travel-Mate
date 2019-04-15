package objects;

public class ZoneName {

    public String shortName;
    public String abbreviation;

    public ZoneName(String shortName, String abbreviation) {
        this.shortName = shortName;
        this.abbreviation = abbreviation;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ZoneName{");
        sb.append("shortName='").append(shortName).append('\'');
        sb.append(", abbreviation='").append(abbreviation).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
