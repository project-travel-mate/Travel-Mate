package objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Model class for city object
 */
@Entity(tableName = "events_new")
public class ChecklistItem {

    //so that primary key is generated automatically
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private  int mId = 0;

    @ColumnInfo(name = "name")
    private final String mName;

    @ColumnInfo(name = "isDone")
    private final String mIsDone;

    // TODO :: Make isDone bool

    /**
     * Initiates checklist item
     * @param name   checklist task name
     * @param isDone specify if the item is checked
     */

    public ChecklistItem(String name, String isDone) {
        this.mName = name;
        this.mIsDone = isDone;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getIsDone() {
        return mIsDone;
    }

    public void setId(int id) {
        this.mId = id;
    }
}