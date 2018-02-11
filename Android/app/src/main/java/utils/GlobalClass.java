package utils;

import android.content.Context;
import android.widget.Toast;

import tie.hackathon.travelguide.R;

/**
 * Created by Sumeet on 1/20/2018.
 */

public class GlobalClass {
    public static void showApiFailureMessage(Context context){
        Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }
}
