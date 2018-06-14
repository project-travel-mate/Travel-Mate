package objects;

/**
 * Model class for city object
 */
public class ChecklistItem {

    private final String mId;
    private final String mName;
    private final String mIsDone;


    public ChecklistItem(String id, String name, String isDone) {
        this.mId = id;
        this.mIsDone = isDone;
        this.mName = name;
    }

    public String getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmIsDone() {
        return mIsDone;
    }
}