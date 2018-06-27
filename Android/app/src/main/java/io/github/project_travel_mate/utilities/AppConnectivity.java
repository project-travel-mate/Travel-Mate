package io.github.project_travel_mate.utilities;

import android.content.Context;

import java.io.IOException;

public class AppConnectivity {

    public AppConnectivity(Context context) {
        Context context1 = context;
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
