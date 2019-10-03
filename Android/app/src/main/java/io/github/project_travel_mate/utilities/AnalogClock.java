package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;

import utils.TravelMateAnalogClock;

public class AnalogClock extends TravelMateAnalogClock {

    private void init() {
        //use this for the default Analog Clock (recommended)
        initializeSimple();

        //or use this if you want to use your own vector assets (not recommended)
        //initializeCustom(faceResourceId, hourResourceId, minuteResourceId, secondResourceId);
    }

    /**
     * @param ctx - copntext
     */
    public AnalogClock(Context ctx) {
        super(ctx);
        init();
    }

    /**
     * @param context - context
     * @param attrs - attributes
     */
    public AnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @param context - context
     * @param attrs - attributes
     * @param defStyleAttr - resource attribute
     */
    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * @param context - context
     * @param attrs - attributes
     * @param defStyleAttr - resource attribute
     * @param defStyleRes - style resources
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
