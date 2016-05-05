package new_database;

import android.provider.BaseColumns;

/**
 * Created by Swati garg on 30-06-2015.
 */
public  abstract class TableEntry_new implements BaseColumns {
    public static final String TABLE_NAME = "events_new";
    public static final String COLUMN_NAME_ID = "event_id";
    public static final String COLUMN_NAME_STIME = "stiming";
    public static final String COLUMN_NAME_ETIME = "etimings";
    public static final String COLUMN_NAME_IMAGE = "image";
    public static final String COLUMN_NAME_CITYNAME = "city_name";


}

