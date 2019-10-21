package io.github.project_travel_mate.utilities;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import io.github.project_travel_mate.R;


/**
 * A widget that displays the time as a 12-at-the-top 24 hour analog clock. By
 * default, it will show the current time in the current timezone.
 */
public class CustomAnalogClock extends View {

    public static boolean is24;
    public static boolean hourOnTop;
    private final ArrayList<DialOverlay> mDialOverlay = new ArrayList<DialOverlay>();
    private Calendar mCalendar;
    private Drawable mFace;
    private int mDialWidth;
    private float mSizeScale = 1f;
    private int mRadius;
    private int mDialHeight;
    private int mBottom;
    private int mTop;
    private int mLeft;
    private int mRight;
    private boolean mSizeChanged;
    private HandsOverlay mHandsOverlay;
    private boolean mAutoUpdate;

    /**
     * Constructor for CustomAnalogClock
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomAnalogClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handleAttrs(context, attrs);
    }

    /**
     * Constructor for CustomAnalogClock
     *
     * @param context
     * @param attrs
     */
    public CustomAnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttrs(context, attrs);
    }

    /**
     * Constructor for CustomAnalogClock
     *
     * @param context
     */
    public CustomAnalogClock(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructor for CustomAnalogClock
     *
     * @param context
     */
    public CustomAnalogClock(Context context, boolean defaultWatchFace) {
        super(context);
        if (defaultWatchFace)
            init(context);
    }

    /**
     * handling the attributes
     *
     * @param context
     * @param attrs
     */
    private void handleAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomAnalogClock, 0, 0);
        if (typedArray.hasValue(R.styleable.CustomAnalogClock_default_watchface)) {
            if (!typedArray.getBoolean(R.styleable.CustomAnalogClock_default_watchface, true)) {
                typedArray.recycle();
                return;
            }
        }
        init(context);
        typedArray.recycle();
    }

    /**
     * Initializing the clock
     *
     * @param context
     */
    public void init(Context context) {
        init(context, R.drawable.clock_face, R.drawable.hours_hand,
                R.drawable.minutes_hand, 0, false, false);
    }

    /**
     * Sets the scale of the view, for example the value 0.5f draws the clock with half of its mRadius
     *
     * @param scale the scale to render the view in
     */
    public void setScale(float scale) {
        if (scale <= 0)
            throw new IllegalArgumentException("Scale must be bigger than 0");
        this.mSizeScale = scale;
        mHandsOverlay.withScale(mSizeScale);
        invalidate();
    }

    /**
     * Function for the clock face on the canvas
     *
     * @param drawableRes
     */
    public void setFace(int drawableRes) {
        setFace(getContext().getDrawable(drawableRes));
    }

    /**
     * Initializes the clock face with hour hand, minute hand
     *
     * @param context
     * @param watchFace
     * @param hourHand
     * @param minuteHand
     * @param alpha
     * @param is24       checks if the time is in 24 hour format or not
     * @param hourOnTop  check if the hour hand is on top on minute hand or not
     */
    public void init(Context context, @DrawableRes int watchFace, @DrawableRes int hourHand,
                     @DrawableRes int minuteHand, int alpha, boolean is24, boolean hourOnTop) {
        CustomAnalogClock.is24 = is24;

        CustomAnalogClock.hourOnTop = hourOnTop;
        setFace(watchFace);
        Drawable hHand = context.getDrawable(hourHand);
        assert hHand != null;
        if (alpha > 0)
            hHand.setAlpha(alpha);

        Drawable mHand = context.getDrawable(minuteHand);

        mCalendar = Calendar.getInstance();

        mHandsOverlay = new HandsOverlay(hHand, mHand).withScale(mSizeScale);
    }

    /**
     * Draw the face of the clock on the canvas with the calculated height, width and radius
     *
     * @param face
     */
    public void setFace(Drawable face) {
        mFace = face;
        mSizeChanged = true;
        mDialHeight = mFace.getIntrinsicHeight();
        mDialWidth = mFace.getIntrinsicWidth();
        mRadius = Math.max(mDialHeight, mDialWidth);

        invalidate();
    }

    /**
     * Sets the currently displayed time in {@link System#currentTimeMillis()}
     * time.
     *
     * @param time the time to display on the clock
     */
    public void setTime(long time) {
        mCalendar.setTimeInMillis(time);

        invalidate();
    }

    /**
     * Sets the currently displayed time.
     *
     * @param calendar The time to display on the clock
     */
    public void setTime(Calendar calendar) {
        mCalendar = calendar;
        invalidate();
        if (mAutoUpdate) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTime(Calendar.getInstance());
                }
            }, 5000);
        }
    }

    /**
     * Auto updating time function of the clock
     */
    public void setmAutoUpdate(boolean mAutoUpdate) {
        this.mAutoUpdate = mAutoUpdate;
        setTime(Calendar.getInstance());
    }

    /**
     * Sets the timezone to use when displaying the time.
     *
     * @param timezone
     */
    public void setTimezone(TimeZone timezone) {
        mCalendar = Calendar.getInstance(timezone);
    }

    public void setHandsOverlay(HandsOverlay handsOverlay) {
        mHandsOverlay = handsOverlay;
    }

    /**
     * The function to check if the scale of analog clock is changed or not
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mSizeChanged = true;
    }

    /**
     * calculating the size of the clock
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final boolean sizeChanged = mSizeChanged;
        mSizeChanged = false;

        final int availW = mRight - mLeft;
        final int availH = mBottom - mTop;

        final int cX = availW / 2;
        final int cY = availH / 2;

        final int w = (int) (mDialWidth * mSizeScale);
        final int h = (int) (mDialHeight * mSizeScale);

        boolean scaled = false;

        if (availW < w || availH < h) {
            scaled = true;
            final float scale = Math.min((float) availW / (float) w,
                    (float) availH / (float) h);
            canvas.save();
            canvas.scale(scale, scale, cX, cY);
        }

        if (sizeChanged) {
            mFace.setBounds(cX - (w / 2), cY - (h / 2), cX + (w / 2), cY
                    + (h / 2));
        }

        mFace.draw(canvas);

        for (final DialOverlay overlay : mDialOverlay) {
            overlay.onDraw(canvas, cX, cY, w, h, mCalendar, sizeChanged);
        }

        mHandsOverlay.onDraw(canvas, cX, cY, w, h, mCalendar, sizeChanged);

        if (scaled) {
            canvas.restore();
        }
    }

    /**
     * from Analog.java
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int finalRadius = (int) (mRadius * mSizeScale);
        setMeasuredDimension(finalRadius, finalRadius);
    }

    /**
     * returning the minimum height for the clock
     *
     * @return
     */
    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) (mDialHeight * mSizeScale);
    }

    /**
     * returning the minimum width for the clock
     *
     * @return
     */
    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) (mDialWidth * mSizeScale);
    }

    /**
     * Assigning the values of layout
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRight = right;
        mLeft = left;
        mTop = top;
        mBottom = bottom;
    }

    /**
     * adding DialOverlay interface
     *
     * @param dialOverlay
     */
    public void addDialOverlay(DialOverlay dialOverlay) {
        mDialOverlay.add(dialOverlay);
    }

    /**
     * Removing DialOverlay interface
     *
     * @param dialOverlay
     */
    public void removeDialOverlay(DialOverlay dialOverlay) {
        mDialOverlay.remove(dialOverlay);
    }

    /**
     * Clearing DialOVerlay interface
     */
    public void clearDialOverlays() {
        mDialOverlay.clear();
    }

}
