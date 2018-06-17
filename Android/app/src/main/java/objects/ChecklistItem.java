package objects;

/**
 * Model class for city object
 */
public class ChecklistItem {

    private final String mId;
    private final String mName;
    private final String mIsDone;

    // TODO :: Make isDone bool
    /**
     * Initiates checklist item
     * @param id        unique id of item
     * @param name      checklist task name
     * @param isDone    specify if the item is checked
     */
    public ChecklistItem(String id, String name, String isDone) {
        this.mId = id;
        this.mIsDone = isDone;
        this.mName = name;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getIsDone() {
        return mIsDone;
    }
}