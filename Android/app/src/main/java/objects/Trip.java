package objects;

public class Trip {

    private String id;
    private String name;
    private String image;
    private String start;
    private String end;
    private String tname;

    public Trip() {

    }

    public Trip(String id, String name, String image, String start, String end, String tname) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.start = start;
        this.end = end;
        this.tname = tname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getTname() {
        return tname;
    }


}
