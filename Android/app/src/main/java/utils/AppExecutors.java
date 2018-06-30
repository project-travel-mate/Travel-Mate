package utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//This class makes sures that all the operations in database must be performed on
// a background thread so that UI functionalities are not affected.
public class AppExecutors {

    //For Singleton Instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private Executor mDiskIO;

    private AppExecutors(Executor diskIO) {
        this.mDiskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor getDiskIO() {
        return mDiskIO;
    }

}
