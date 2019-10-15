package io.github.project_travel_mate.utilities;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.Calendar;


public class HandsOverlay implements DialOverlay {

    private final Drawable mHour;
    private final Drawable mMinute;
    private final boolean mUseLargeFace;
    private float mHourRot;
    private float mMinRot;
    private boolean mShowSeconds;
    private float mScale;

    /**
     * Constructor for HandsOverlay.java
     *
     * @param context
     * @param useLargeFace checks if the scale of the clock face is default or not
     */
    public HandsOverlay(Context context, boolean useLargeFace) {
        final Resources r = context.getResources();

        mUseLargeFace = useLargeFace;

        mHour = null;
        mMinute = null;
    }

    /**
     * Constructor for HandsOverlay.java
     * Sets the drawable of minute hand and hour hand.
     * Default value for mUseLargeFace is false.
     *
     * @param hourHand
     * @param minuteHand
     */
    public HandsOverlay(Drawable hourHand, Drawable minuteHand) {
        mUseLargeFace = false;

        mHour = hourHand;
        mMinute = minuteHand;
    }

    /**
     * Constructor of HandsOverlay.java
     * The following constructor is called when the scale of the clock is changed.
     * The scales of the minute hand and hour hand depends on the setScale function from CustomAnalogClock.java
     *
     * @param scale
     * @return
     */
    public HandsOverlay withScale(float scale) {
        this.mScale = scale;
        return this;
    }

    /**
     * Calculates the angle of hour hand for both 24 hour format and 12 hour format
     *
     * @param h
     * @param m
     * @return
     */
    public static float getHourHandAngle(int h, int m) {
        return CustomAnalogClock.is24 ? ((12 + h) / 24.0f * 360) % 360 + (m / 60.0f) * 360 / 24.0f :
                ((12 + h) / 12.0f * 360) % 360 + (m / 60.0f) * 360 / 12.0f;
    }
    /**
     * Override onDraw method to draw the overlay.
     *
     * @param canvas   the canvas onto which you must draw
     * @param cX       the x coordinate of the center
     * @param cY       the y coordinate of the center
     * @param w        the width of the canvas
     * @param h        the height of the canvas
     * @param calendar the desired date/time
     */
    @Override
    public void onDraw(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar,
                       boolean sizeChanged) {

        updateHands(calendar);

        canvas.save();
        if (!CustomAnalogClock.hourOnTop)
            drawHours(canvas, cX, cY, w, h, calendar, sizeChanged);
        else
            drawMinutes(canvas, cX, cY, w, h, calendar, sizeChanged);
        canvas.restore();

        canvas.save();
        if (!CustomAnalogClock.hourOnTop)
            drawMinutes(canvas, cX, cY, w, h, calendar, sizeChanged);
        else
            drawHours(canvas, cX, cY, w, h, calendar, sizeChanged);
        canvas.restore();
    }

    /**
     * Sets minute hand on the clock face
     *
     * @param canvas      the canvas onto which you must draw
     * @param cX          the x coordinate of the center
     * @param cY          the y coordinate of the center
     * @param w           the width of the canvas
     * @param h           the height of the canvas
     * @param calendar    the desired date/time
     * @param sizeChanged boolean to check if the clock size has changed or not
     */
    private void drawMinutes(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar,
                             boolean sizeChanged) {
        canvas.rotate(mMinRot, cX, cY);

        if (sizeChanged) {
            w = (int) (mMinute.getIntrinsicWidth() * mScale);
            h = (int) (mMinute.getIntrinsicHeight() * mScale);
            mMinute.setBounds(cX - (w / 1), cY - (h / 1), cX + (w / 2), cY + (h / 2));
        }
        mMinute.draw(canvas);
    }

    /**
     * Sets minute hand on the clock face
     *
     * @param canvas      the canvas onto which you must draw
     * @param cX          the x coordinate of the center
     * @param cY          the y coordinate of the center
     * @param w           the width of the canvas
     * @param h           the height of the canvas
     * @param calendar    the desired date/time
     * @param sizeChanged boolean to check if the clock size has changed or not
     */
    private void drawHours(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar,
                           boolean sizeChanged) {
        canvas.rotate(mHourRot, cX, cY);

        if (sizeChanged) {
            w = (int) (mHour.getIntrinsicWidth() * mScale);
            h = (int) (mHour.getIntrinsicHeight() * mScale);
            mHour.setBounds(cX - (w / 2), cY - (h / 1), cX + (w / 2), cY + (h / 2));
        }
        mHour.draw(canvas);
    }

    /**
     * Triggers the analog clock to show seconds
     *
     * @param showSeconds
     */
    public void setShowSeconds(boolean showSeconds) {
        mShowSeconds = showSeconds;
    }

    /**
     * This function calculates the rotation of hour hand and minute hand
     *
     * @param calendar
     */
    private void updateHands(Calendar calendar) {

        final int h = calendar.get(Calendar.HOUR_OF_DAY);
        final int m = calendar.get(Calendar.MINUTE);
        final int s = calendar.get(Calendar.SECOND);

        mHourRot = getHourHandAngle(h, m);
        mMinRot = (m / 60.0f) * 360 + (mShowSeconds ? ((s / 60.0f) * 360 / 60.0f) : 0);
    }

}
