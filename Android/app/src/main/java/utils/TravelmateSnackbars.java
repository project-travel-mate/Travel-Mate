package utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public interface TravelmateSnackbars {

    static Snackbar createSnackBar(View view, int message, int duration) {
        return Snackbar.make(view, message, duration);
    }
    static Snackbar createSnackBar(View view, String message, int duration) {
        return Snackbar.make(view, message, duration);
    }
}