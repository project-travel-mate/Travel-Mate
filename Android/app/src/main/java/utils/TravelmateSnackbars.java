package utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public abstract interface TravelmateSnackbars {

    static public Snackbar createSnackBar(View view, int message, int duration) {
        return Snackbar.make(view, message, duration);
    }
}