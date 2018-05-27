package objects;

/**
 * Model class for city object
 */
public class ChecklistItem {

    private final String id;
    private final String name;
    private final String isDone;


    public ChecklistItem(String id, String name, String isDone) {
        this.id = id;
        this.isDone = isDone;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIsDone() {
        return isDone;
    }
}