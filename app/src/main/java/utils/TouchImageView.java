package utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.OverScroller;
import android.widget.Scroller;

import java.util.Objects;

public class TouchImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String DEBUG = "DEBUG";

    //
    // SuperMin and SuperMax multipliers. Determine how much the image can be
    // zoomed below or above the zoom boundaries, before animating back to the
    // min/max zoom boundary.
    //
    private static final float SUPER_MIN_MULTIPLIER = .75f;
    private static final float SUPER_MAX_MULTIPLIER = 1.25f;

    //
    // Scale of image ranges from mMinScale to mMaxScale, where mMinScale == 1
    // when the image is stretched to fit view.
    //
    private float mNormalizedScale;

    //
    // Matrix applied to image. MSCALE_X and MSCALE_Y should always be equal.
    // MTRANS_X and MTRANS_Y are the other values used. mPrevMatrix is the mMatrix
    // saved prior to the screen rotating.
    //
    private Matrix mMatrix, mPrevMatrix;
    private State mState;
    private float mMinScale;
    private float mMaxScale;
    private float mSuperMinScale;
    private float mSuperMaxScale;
    private float[] mFloat;
    private Context mContext;
    private Fling mFling;
    private ScaleType mScaleType;
    private boolean mImageRenderedAtLeastOnce;
    private boolean mOnDrawReady;
    private ZoomVariables mDelayedZoomVariables;
    //
    // Size of view and previous view size (ie before rotation)
    //
    private int mViewWidth, mViewHeight, mPrevViewWidth, mPrevViewHeight;
    //
    // Size of image when it is stretched to fit view. Before and After rotation.
    //
    private float mMatchViewWidth, mMatchViewHeight, mPrevMatchViewWidth, mPrevMatchViewHeight;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private GestureDetector.OnDoubleTapListener mDoubleTapListener = null;
    private OnTouchListener mUserTouchListener = null;
    private OnTouchImageViewListener mTouchImageViewListener = null;
    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        sharedConstructing(context);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.mContext = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
        mMatrix = new Matrix();
        mPrevMatrix = new Matrix();
        mFloat = new float[9];
        mNormalizedScale = 1;
        if (mScaleType == null) {
            mScaleType = ScaleType.FIT_CENTER;
        }
        mMinScale = 1;
        mMaxScale = 3;
        mSuperMinScale = SUPER_MIN_MULTIPLIER * mMinScale;
        mSuperMaxScale = SUPER_MAX_MULTIPLIER * mMaxScale;
        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);
        setState(State.NONE);
        mOnDrawReady = false;
        super.setOnTouchListener(new PrivateOnTouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mUserTouchListener = l;
    }

    public void setOnTouchImageViewListener(OnTouchImageViewListener l) {
        mTouchImageViewListener = l;
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener l) {
        mDoubleTapListener = l;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        savePreviousImageValues();
        fitImageToView();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        savePreviousImageValues();
        fitImageToView();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        savePreviousImageValues();
        fitImageToView();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        savePreviousImageValues();
        fitImageToView();
    }

    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setScaleType(ScaleType type) {
        if (type == ScaleType.FIT_START || type == ScaleType.FIT_END) {
            throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
        }
        if (type == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX);

        } else {
            mScaleType = type;
            if (mOnDrawReady) {
                //
                // If the image is already rendered, scaleType has been called programmatically
                // and the TouchImageView should be updated with the new scaleType.
                //
                setZoom(this);
            }
        }
    }

    /**
     * Returns false if image is in initial, unzoomed state. False, otherwise.
     *
     * @return true if image is zoomed
     */
    private boolean isZoomed() {
        return mNormalizedScale != 1;
    }

    /**
     * Return a Rect representing the zoomed image.
     *
     * @return rect representing zoomed image
     */
    public RectF getZoomedRect() {
        if (mScaleType == ScaleType.FIT_XY) {
            throw new UnsupportedOperationException("getZoomedRect() not supported with FIT_XY");
        }
        PointF topLeft = transformCoordTouchToBitmap(0, 0, true);
        PointF bottomRight = transformCoordTouchToBitmap(mViewWidth, mViewHeight, true);

        float w = getDrawable().getIntrinsicWidth();
        float h = getDrawable().getIntrinsicHeight();
        return new RectF(topLeft.x / w, topLeft.y / h, bottomRight.x / w, bottomRight.y / h);
    }

    /**
     * Save the current mMatrix and view dimensions
     * in the mPrevMatrix and prevView variables.
     */
    private void savePreviousImageValues() {
        if (mMatrix != null && mViewHeight != 0 && mViewWidth != 0) {
            mMatrix.getValues(mFloat);
            mPrevMatrix.setValues(mFloat);
            mPrevMatchViewHeight = mMatchViewHeight;
            mPrevMatchViewWidth = mMatchViewWidth;
            mPrevViewHeight = mViewHeight;
            mPrevViewWidth = mViewWidth;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("saveScale", mNormalizedScale);
        bundle.putFloat("mMatchViewHeight", mMatchViewHeight);
        bundle.putFloat("mMatchViewWidth", mMatchViewWidth);
        bundle.putInt("mViewWidth", mViewWidth);
        bundle.putInt("mViewHeight", mViewHeight);
        mMatrix.getValues(mFloat);
        bundle.putFloatArray("mMatrix", mFloat);
        bundle.putBoolean("imageRendered", mImageRenderedAtLeastOnce);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mNormalizedScale = bundle.getFloat("saveScale");
            mFloat = bundle.getFloatArray("mMatrix");
            mPrevMatrix.setValues(mFloat);
            mPrevMatchViewHeight = bundle.getFloat("mMatchViewHeight");
            mPrevMatchViewWidth = bundle.getFloat("mMatchViewWidth");
            mPrevViewHeight = bundle.getInt("mViewHeight");
            mPrevViewWidth = bundle.getInt("mViewWidth");
            mImageRenderedAtLeastOnce = bundle.getBoolean("imageRendered");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mOnDrawReady = true;
        mImageRenderedAtLeastOnce = true;
        if (mDelayedZoomVariables != null) {
            setZoom(mDelayedZoomVariables.scale,
                    mDelayedZoomVariables.focusX,
                    mDelayedZoomVariables.focusY,
                    mDelayedZoomVariables.scaleType);
            mDelayedZoomVariables = null;
        }
        super.onDraw(canvas);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        savePreviousImageValues();
    }

    /**
     * Get the max zoom multiplier.
     *
     * @return max zoom multiplier.
     */
    public float getMaxZoom() {
        return mMaxScale;
    }

    /**
     * Set the max zoom multiplier. Default value: 3.
     *
     * @param max max zoom multiplier.
     */
    public void setMaxZoom(float max) {
        mMaxScale = max;
        mSuperMaxScale = SUPER_MAX_MULTIPLIER * mMaxScale;
    }

    /**
     * Get the min zoom multiplier.
     *
     * @return min zoom multiplier.
     */
    public float getMinZoom() {
        return mMinScale;
    }

    /**
     * Set the min zoom multiplier. Default value: 1.
     *
     * @param min min zoom multiplier.
     */
    public void setMinZoom(float min) {
        mMinScale = min;
        mSuperMinScale = SUPER_MIN_MULTIPLIER * mMinScale;
    }

    /**
     * Get the current zoom. This is the zoom relative to the initial
     * scale, not the original resource.
     *
     * @return current zoom multiplier.
     */
    private float getCurrentZoom() {
        return mNormalizedScale;
    }

    /**
     * Reset zoom and translation to initial state.
     */
    private void resetZoom() {
        mNormalizedScale = 1;
        fitImageToView();
    }

    /**
     * Set zoom to the specified scale. Image will be centered by default.
     *
     * @param scale : scale to which zoom
     */
    public void setZoom(float scale) {
        setZoom(scale, 0.5f, 0.5f);
    }

    /**
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     *
     * @param scale  :scael to which zoom
     * @param focusX :focus point of x
     * @param focusY :focus point of y
     */
    private void setZoom(float scale, float focusX, float focusY) {
        setZoom(scale, focusX, focusY, mScaleType);
    }

    /**
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     *
     * @param scale     :scale to zoom
     * @param focusX    :focus point of x
     * @param focusY    :focus point of y
     * @param scaleType :scaling type
     */
    private void setZoom(float scale, float focusX, float focusY, ScaleType scaleType) {
        //
        // setZoom can be called before the image is on the screen, but at this point,
        // image and view sizes have not yet been calculated in onMeasure. Thus, we should
        // delay calling setZoom until the view has been measured.
        //
        if (!mOnDrawReady) {
            mDelayedZoomVariables = new ZoomVariables(scale, focusX, focusY, scaleType);
            return;
        }

        if (scaleType != mScaleType) {
            setScaleType(scaleType);
        }
        resetZoom();
        scaleImage(scale, mViewWidth / 2, mViewHeight / 2, true);
        mMatrix.getValues(mFloat);
        mFloat[Matrix.MTRANS_X] = -((focusX * getImageWidth()) - (mViewWidth * 0.5f));
        mFloat[Matrix.MTRANS_Y] = -((focusY * getImageHeight()) - (mViewHeight * 0.5f));
        mMatrix.setValues(mFloat);
        fixTrans();
        setImageMatrix(mMatrix);
    }

    /**
     * Set zoom parameters equal to another TouchImageView. Including scale, position,
     * and ScaleType.
     *
     * @param img : imageView
     */
    private void setZoom(TouchImageView img) {
        PointF center = img.getScrollPosition();
        setZoom(img.getCurrentZoom(), Objects.requireNonNull(center).x, center.y, img.getScaleType());
    }

    /**
     * Return the point at the center of the zoomed image. The PointF coordinates range
     * in value between 0 and 1 and the focus point is denoted as a fraction from the left
     * and top of the view. For example, the top left corner of the image would be (0, 0).
     * And the bottom right corner would be (1, 1).
     *
     * @return PointF representing the scroll position of the zoomed image.
     */
    private PointF getScrollPosition() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        PointF point = transformCoordTouchToBitmap(mViewWidth / 2, mViewHeight / 2, true);
        point.x /= drawableWidth;
        point.y /= drawableHeight;
        return point;
    }

    /**
     * Set the focus point of the zoomed image. The focus points are denoted as a fraction from the
     * left and top of the view. The focus points can range in value between 0 and 1.
     *
     * @param focusX :focus point of x
     * @param focusY :focus point of y
     */
    public void setScrollPosition(float focusX, float focusY) {
        setZoom(mNormalizedScale, focusX, focusY);
    }

    /**
     * Performs boundary checking and fixes the image mMatrix if it
     * is out of bounds.
     */
    private void fixTrans() {
        mMatrix.getValues(mFloat);
        float transX = mFloat[Matrix.MTRANS_X];
        float transY = mFloat[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, mViewWidth, getImageWidth());
        float fixTransY = getFixTrans(transY, mViewHeight, getImageHeight());

        if (fixTransX != 0 || fixTransY != 0) {
            mMatrix.postTranslate(fixTransX, fixTransY);
        }
    }

    /**
     * When transitioning from zooming from focus to zoom from center (or vice versa)
     * the image can become unaligned within the view. This is apparent when zooming
     * quickly. When the content size is less than the view size, the content will often
     * be centered incorrectly within the view. fixScaleTrans first calls fixTrans() and
     * then makes sure the image is centered correctly within the view.
     */
    private void fixScaleTrans() {
        fixTrans();
        mMatrix.getValues(mFloat);
        if (getImageWidth() < mViewWidth) {
            mFloat[Matrix.MTRANS_X] = (mViewWidth - getImageWidth()) / 2;
        }

        if (getImageHeight() < mViewHeight) {
            mFloat[Matrix.MTRANS_Y] = (mViewHeight - getImageHeight()) / 2;
        }
        mMatrix.setValues(mFloat);
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;

        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    private float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    private float getImageWidth() {
        return mMatchViewWidth * mNormalizedScale;
    }

    private float getImageHeight() {
        return mMatchViewHeight * mNormalizedScale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mViewWidth = setViewSize(widthMode, widthSize, drawableWidth);
        mViewHeight = setViewSize(heightMode, heightSize, drawableHeight);

        //
        // Set view dimensions
        //
        setMeasuredDimension(mViewWidth, mViewHeight);

        //
        // Fit content within view
        //
        fitImageToView();
    }

    /**
     * If the mNormalizedScale is equal to 1, then the image is made to fit the screen. Otherwise,
     * it is made to fit the screen according to the dimensions of the previous image mMatrix. This
     * allows the image to maintain its zoom after rotation.
     */
    private void fitImageToView() {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            return;
        }
        if (mMatrix == null || mPrevMatrix == null) {
            return;
        }

        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        //
        // Scale image for view
        //
        float scaleX = (float) mViewWidth / drawableWidth;
        float scaleY = (float) mViewHeight / drawableHeight;

        switch (mScaleType) {
            case CENTER:
                scaleX = scaleY = 1;
                break;

            case CENTER_CROP:
                scaleX = scaleY = Math.max(scaleX, scaleY);
                break;

            case CENTER_INSIDE:
                scaleX = scaleY = Math.min(1, Math.min(scaleX, scaleY));

            case FIT_CENTER:
                scaleX = scaleY = Math.min(scaleX, scaleY);
                break;

            case FIT_XY:
                break;

            default:
                //
                // FIT_START and FIT_END not supported
                //
                throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");

        }

        //
        // Center the image
        //
        float redundantXSpace = mViewWidth - (scaleX * drawableWidth);
        float redundantYSpace = mViewHeight - (scaleY * drawableHeight);
        mMatchViewWidth = mViewWidth - redundantXSpace;
        mMatchViewHeight = mViewHeight - redundantYSpace;
        if (!isZoomed() && !mImageRenderedAtLeastOnce) {
            //
            // Stretch and center image to fit view
            //
            mMatrix.setScale(scaleX, scaleY);
            mMatrix.postTranslate(redundantXSpace / 2, redundantYSpace / 2);
            mNormalizedScale = 1;

        } else {
            //
            // These values should never be 0 or we will set mViewWidth and mViewHeight
            // to NaN in translateMatrixAfterRotate. To avoid this, call savePreviousImageValues
            // to set them equal to the current values.
            //
            if (mPrevMatchViewWidth == 0 || mPrevMatchViewHeight == 0) {
                savePreviousImageValues();
            }

            mPrevMatrix.getValues(mFloat);

            //
            // Rescale Matrix after rotation
            //
            mFloat[Matrix.MSCALE_X] = mMatchViewWidth / drawableWidth * mNormalizedScale;
            mFloat[Matrix.MSCALE_Y] = mMatchViewHeight / drawableHeight * mNormalizedScale;

            //
            // TransX and TransY from previous mMatrix
            //
            float transX = mFloat[Matrix.MTRANS_X];
            float transY = mFloat[Matrix.MTRANS_Y];

            //
            // Width
            //
            float prevActualWidth = mPrevMatchViewWidth * mNormalizedScale;
            float actualWidth = getImageWidth();
            translateMatrixAfterRotate(Matrix.MTRANS_X,
                    transX,
                    prevActualWidth,
                    actualWidth,
                    mPrevViewWidth,
                    mViewWidth,
                    drawableWidth);

            //
            // Height
            //
            float prevActualHeight = mPrevMatchViewHeight * mNormalizedScale;
            float actualHeight = getImageHeight();
            translateMatrixAfterRotate(Matrix.MTRANS_Y,
                    transY,
                    prevActualHeight,
                    actualHeight,
                    mPrevViewHeight,
                    mViewHeight,
                    drawableHeight);

            //
            // Set the mMatrix to the adjusted scale and translate values.
            //
            mMatrix.setValues(mFloat);
        }
        fixTrans();
        setImageMatrix(mMatrix);
    }

    /**
     * Set view dimensions based on layout params
     *
     * @param mode          :zoom mode
     * @param size          :final size
     * @param drawableWidth :width
     * @return :int final view size
     */
    private int setViewSize(int mode, int size, int drawableWidth) {
        int viewSize;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                viewSize = size;
                break;

            case MeasureSpec.AT_MOST:
                viewSize = Math.min(drawableWidth, size);
                break;

            case MeasureSpec.UNSPECIFIED:
                viewSize = drawableWidth;
                break;

            default:
                viewSize = size;
                break;
        }
        return viewSize;
    }

    /**
     * After rotating, the mMatrix needs to be translated. This function finds the area of image
     * which was previously centered and adjusts translations so that is again the center, post-rotation.
     *
     * @param axis          Matrix.MTRANS_X or Matrix.MTRANS_Y
     * @param trans         the value of trans in that axis before the rotation
     * @param prevImageSize the width/height of the image before the rotation
     * @param imageSize     width/height of the image after rotation
     * @param prevViewSize  width/height of view before rotation
     * @param viewSize      width/height of view after rotation
     * @param drawableSize  width/height of drawable
     */
    private void translateMatrixAfterRotate(int axis,
                                            float trans,
                                            float prevImageSize,
                                            float imageSize,
                                            int prevViewSize,
                                            int viewSize,
                                            int drawableSize) {
        if (imageSize < viewSize) {
            //
            // The width/height of image is less than the view's width/height. Center it.
            //
            mFloat[axis] = (viewSize - (drawableSize * mFloat[Matrix.MSCALE_X])) * 0.5f;

        } else if (trans > 0) {
            //
            // The image is larger than the view, but was not before rotation. Center it.
            //
            mFloat[axis] = -((imageSize - viewSize) * 0.5f);

        } else {
            //
            // Find the area of the image which was previously centered in the view. Determine its distance
            // from the left/top side of the view as a fraction of the entire image's width/height. Use that percentage
            // to calculate the trans in the new view width/height.
            //
            float percentage = (Math.abs(trans) + (0.5f * prevViewSize)) / prevImageSize;
            mFloat[axis] = -((percentage * imageSize) - (viewSize * 0.5f));
        }
    }

    private void setState(State state) {
        this.mState = state;
    }

    public boolean canScrollHorizontallyFroyo(int direction) {
        return canScrollHorizontally(direction);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        mMatrix.getValues(mFloat);
        float x = mFloat[Matrix.MTRANS_X];

        return !(getImageWidth() < mViewWidth) &&
                (!(x >= -1) || direction >= 0) &&
                (!(Math.abs(x) + mViewWidth + 1 >= getImageWidth()) || direction <= 0);

    }

    private void scaleImage(double deltaScale, float focusX, float focusY, boolean stretchImageToSuper) {

        float lowerScale, upperScale;
        if (stretchImageToSuper) {
            lowerScale = mSuperMinScale;
            upperScale = mSuperMaxScale;

        } else {
            lowerScale = mMinScale;
            upperScale = mMaxScale;
        }

        float origScale = mNormalizedScale;
        mNormalizedScale *= deltaScale;
        if (mNormalizedScale > upperScale) {
            mNormalizedScale = upperScale;
            deltaScale = upperScale / origScale;
        } else if (mNormalizedScale < lowerScale) {
            mNormalizedScale = lowerScale;
            deltaScale = lowerScale / origScale;
        }

        mMatrix.postScale((float) deltaScale, (float) deltaScale, focusX, focusY);
        fixScaleTrans();
    }

    /**
     * This function will transform the coordinates in the touch event to the coordinate
     * system of the drawable that the imageview contain
     *
     * @param x            x-coordinate of touch event
     * @param y            y-coordinate of touch event
     * @param clipToBitmap Touch event may occur within view, but outside image content. True, to clip return value
     *                     to the bounds of the bitmap size.
     * @return Coordinates of the point touched, in the coordinate system of the original drawable.
     */
    private PointF transformCoordTouchToBitmap(float x, float y, boolean clipToBitmap) {
        mMatrix.getValues(mFloat);
        float origW = getDrawable().getIntrinsicWidth();
        float origH = getDrawable().getIntrinsicHeight();
        float transX = mFloat[Matrix.MTRANS_X];
        float transY = mFloat[Matrix.MTRANS_Y];
        float finalX = ((x - transX) * origW) / getImageWidth();
        float finalY = ((y - transY) * origH) / getImageHeight();

        if (clipToBitmap) {
            finalX = Math.min(Math.max(finalX, 0), origW);
            finalY = Math.min(Math.max(finalY, 0), origH);
        }

        return new PointF(finalX, finalY);
    }

    /**
     * Inverse of transformCoordTouchToBitmap. This function will transform the coordinates in the
     * drawable's coordinate system to the view's coordinate system.
     *
     * @param bx x-coordinate in original bitmap coordinate system
     * @param by y-coordinate in original bitmap coordinate system
     * @return Coordinates of the point in the view's coordinate system.
     */
    private PointF transformCoordBitmapToTouch(float bx, float by) {
        mMatrix.getValues(mFloat);
        float origW = getDrawable().getIntrinsicWidth();
        float origH = getDrawable().getIntrinsicHeight();
        float px = bx / origW;
        float py = by / origH;
        float finalX = mFloat[Matrix.MTRANS_X] + getImageWidth() * px;
        float finalY = mFloat[Matrix.MTRANS_Y] + getImageHeight() * py;
        return new PointF(finalX, finalY);
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    private void compatPostOnAnimation(Runnable runnable) {
        postOnAnimation(runnable);

    }

    private void printMatrixInfo() {
        float[] n = new float[9];
        mMatrix.getValues(n);
        Log.d(DEBUG, "Scale: " + n[Matrix.MSCALE_X] +
                " TransX: " + n[Matrix.MTRANS_X] +
                " TransY: " + n[Matrix.MTRANS_Y]);
    }

    private enum State { NONE, DRAG, ZOOM, FLING, ANIMATE_ZOOM }

    interface OnTouchImageViewListener {
        void onMove();
    }

    /**
     * Gesture Listener detects a single click or long click and passes that on
     * to the view's listener.
     *
     * @author Ortiz
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mDoubleTapListener != null) {
                return mDoubleTapListener.onSingleTapConfirmed(e);
            }
            return performClick();
        }

        @Override
        public void onLongPress(MotionEvent e) {
            performLongClick();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mFling != null) {
                //
                // If a previous mFling is still active, it should be cancelled so that two flings
                // are not run simultaenously.
                //
                mFling.cancelFling();
            }
            mFling = new Fling((int) velocityX, (int) velocityY);
            compatPostOnAnimation(mFling);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            boolean consumed = false;
            if (mDoubleTapListener != null) {
                consumed = mDoubleTapListener.onDoubleTap(e);
            }
            if (mState == State.NONE) {
                float targetZoom = (mNormalizedScale == mMinScale) ? mMaxScale : mMinScale;
                DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, e.getX(), e.getY(), false);
                compatPostOnAnimation(doubleTap);
                consumed = true;
            }
            return consumed;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return mDoubleTapListener != null && mDoubleTapListener.onDoubleTapEvent(e);
        }
    }

    /**
     * Responsible for all touch events. Handles the heavy lifting of drag and also sends
     * touch events to Scale Detector and Gesture Detector.
     *
     * @author Ortiz
     */
    private class PrivateOnTouchListener implements OnTouchListener {

        //
        // Remember last point position for dragging
        //
        private final PointF mLast = new PointF();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mScaleDetector.onTouchEvent(event);
            mGestureDetector.onTouchEvent(event);
            PointF curr = new PointF(event.getX(), event.getY());

            if (mState == State.NONE || mState == State.DRAG || mState == State.FLING) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLast.set(curr);
                        if (mFling != null)
                            mFling.cancelFling();
                        setState(State.DRAG);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mState == State.DRAG) {
                            float deltaX = curr.x - mLast.x;
                            float deltaY = curr.y - mLast.y;
                            float fixTransX = getFixDragTrans(deltaX, mViewWidth, getImageWidth());
                            float fixTransY = getFixDragTrans(deltaY, mViewHeight, getImageHeight());
                            mMatrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            mLast.set(curr.x, curr.y);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        setState(State.NONE);
                        break;
                }
            }

            setImageMatrix(mMatrix);

            //
            // User-defined OnTouchListener
            //
            if (mUserTouchListener != null) {
                mUserTouchListener.onTouch(v, event);
            }

            //
            // OnTouchImageViewListener is set: TouchImageView dragged by user.
            //
            if (mTouchImageViewListener != null) {
                mTouchImageViewListener.onMove();
            }

            //
            // indicate event was handled
            //
            return true;
        }
    }

    /**
     * ScaleListener detects user two finger scaling and scales image.
     *
     * @author Ortiz
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            setState(State.ZOOM);
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleImage(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY(), true);

            //
            // OnTouchImageViewListener is set: TouchImageView pinch zoomed by user.
            //
            if (mTouchImageViewListener != null) {
                mTouchImageViewListener.onMove();
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            setState(State.NONE);
            boolean animateToZoomBoundary = false;
            float targetZoom = mNormalizedScale;
            if (mNormalizedScale > mMaxScale) {
                targetZoom = mMaxScale;
                animateToZoomBoundary = true;

            } else if (mNormalizedScale < mMinScale) {
                targetZoom = mMinScale;
                animateToZoomBoundary = true;
            }

            if (animateToZoomBoundary) {
                DoubleTapZoom doubleTap = new DoubleTapZoom(targetZoom, mViewWidth / 2, mViewHeight / 2, true);
                compatPostOnAnimation(doubleTap);
            }
        }
    }

    /**
     * DoubleTapZoom calls a series of runnables which apply
     * an animated zoom in/out graphic to the image.
     *
     * @author Ortiz
     */
    private class DoubleTapZoom implements Runnable {

        private static final float ZOOM_TIME = 500;
        private final long mStartTime;
        private final float mStartZoom;
        private final float mTargetZoom;
        private final float mBitmapX;
        private final float mBitmapY;
        private final boolean mStretchImageToSuper;
        private final AccelerateDecelerateInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
        private final PointF mStartTouch;
        private final PointF mEndTouch;

        DoubleTapZoom(float targetZoom, float focusX, float focusY, boolean stretchImageToSuper) {
            setState(State.ANIMATE_ZOOM);
            mStartTime = System.currentTimeMillis();
            this.mStartZoom = mNormalizedScale;
            this.mTargetZoom = targetZoom;
            this.mStretchImageToSuper = stretchImageToSuper;
            PointF bitmapPoint = transformCoordTouchToBitmap(focusX, focusY, false);
            this.mBitmapX = bitmapPoint.x;
            this.mBitmapY = bitmapPoint.y;

            //
            // Used for translating image during scaling
            //
            mStartTouch = transformCoordBitmapToTouch(mBitmapX, mBitmapY);
            mEndTouch = new PointF(mViewWidth / 2, mViewHeight / 2);
        }

        @Override
        public void run() {
            float t = interpolate();
            double deltaScale = calculateDeltaScale(t);
            scaleImage(deltaScale, mBitmapX, mBitmapY, mStretchImageToSuper);
            translateImageToCenterTouchPosition(t);
            fixScaleTrans();
            setImageMatrix(mMatrix);

            //
            // OnTouchImageViewListener is set: double tap runnable updates listener
            // with every frame.
            //
            if (mTouchImageViewListener != null) {
                mTouchImageViewListener.onMove();
            }

            if (t < 1f) {
                //
                // We haven't finished zooming
                //
                compatPostOnAnimation(this);

            } else {
                //
                // Finished zooming
                //
                setState(State.NONE);
            }
        }

        /**
         * Interpolate between where the image should start and end in order to translate
         * the image so that the point that is touched is what ends up centered at the end
         * of the zoom.
         *
         * @param t :touch point of image
         */
        private void translateImageToCenterTouchPosition(float t) {
            float targetX = mStartTouch.x + t * (mEndTouch.x - mStartTouch.x);
            float targetY = mStartTouch.y + t * (mEndTouch.y - mStartTouch.y);
            PointF curr = transformCoordBitmapToTouch(mBitmapX, mBitmapY);
            mMatrix.postTranslate(targetX - curr.x, targetY - curr.y);
        }

        /**
         * Use interpolator to get t
         *
         * @return interpolator point of image
         */
        private float interpolate() {
            long currTime = System.currentTimeMillis();
            float elapsed = (currTime - mStartTime) / ZOOM_TIME;
            elapsed = Math.min(1f, elapsed);
            return mInterpolator.getInterpolation(elapsed);
        }

        /**
         * Interpolate the current targeted zoom and get the delta
         * from the current zoom.
         *
         * @param t :delat size
         * @return :final scale
         */
        private double calculateDeltaScale(float t) {
            double zoom = mStartZoom + t * (mTargetZoom - mStartZoom);
            return zoom / mNormalizedScale;
        }
    }

    /**
     * Fling launches sequential runnables which apply
     * the mFling graphic to the image. The values for the translation
     * are interpolated by the Scroller.
     *
     * @author Ortiz
     */
    private class Fling implements Runnable {

        CompatScroller scroller;
        int currX, currY;

        Fling(int velocityX, int velocityY) {
            setState(State.FLING);
            scroller = new CompatScroller(mContext);
            mMatrix.getValues(mFloat);

            int startX = (int) mFloat[Matrix.MTRANS_X];
            int startY = (int) mFloat[Matrix.MTRANS_Y];
            int minX, maxX, minY, maxY;

            if (getImageWidth() > mViewWidth) {
                minX = mViewWidth - (int) getImageWidth();
                maxX = 0;

            } else {
                minX = maxX = startX;
            }

            if (getImageHeight() > mViewHeight) {
                minY = mViewHeight - (int) getImageHeight();
                maxY = 0;

            } else {
                minY = maxY = startY;
            }

            scroller.fling(startX, startY, velocityX, velocityY, minX,
                    maxX, minY, maxY);
            currX = startX;
            currY = startY;
        }

        void cancelFling() {
            if (scroller != null) {
                setState(State.NONE);
                scroller.forceFinished();
            }
        }

        @Override
        public void run() {

            //
            // OnTouchImageViewListener is set: TouchImageView listener has been flung by user.
            // Listener runnable updated with each frame of mFling animation.
            //
            if (mTouchImageViewListener != null) {
                mTouchImageViewListener.onMove();
            }

            if (scroller.isFinished()) {
                scroller = null;
                return;
            }

            if (scroller.computeScrollOffset()) {
                int newX = scroller.getCurrX();
                int newY = scroller.getCurrY();
                int transX = newX - currX;
                int transY = newY - currY;
                currX = newX;
                currY = newY;
                mMatrix.postTranslate(transX, transY);
                fixTrans();
                setImageMatrix(mMatrix);
                compatPostOnAnimation(this);
            }
        }
    }

    @TargetApi(VERSION_CODES.GINGERBREAD)
    private class CompatScroller {
        final OverScroller overScroller;
        final boolean isPreGingerbread;
        Scroller scroller;

        CompatScroller(Context context) {
            isPreGingerbread = false;
            overScroller = new OverScroller(context);
        }

        void fling(int startX, int startY, int velocityX, int velocityY,
                   int minX, int maxX, int minY, int maxY) {
            if (isPreGingerbread) {
                scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            } else {
                overScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            }
        }

        void forceFinished() {
            if (isPreGingerbread) {
                scroller.forceFinished(true);
            } else {
                overScroller.forceFinished(true);
            }
        }

        boolean isFinished() {
            if (isPreGingerbread) {
                return scroller.isFinished();
            } else {
                return overScroller.isFinished();
            }
        }

        boolean computeScrollOffset() {
            if (isPreGingerbread) {
                return scroller.computeScrollOffset();
            } else {
                overScroller.computeScrollOffset();
                return overScroller.computeScrollOffset();
            }
        }

        int getCurrX() {
            if (isPreGingerbread) {
                return scroller.getCurrX();
            } else {
                return overScroller.getCurrX();
            }
        }

        int getCurrY() {
            if (isPreGingerbread) {
                return scroller.getCurrY();
            } else {
                return overScroller.getCurrY();
            }
        }
    }

    private class ZoomVariables {
        final float scale;
        final float focusX;
        final float focusY;
        final ScaleType scaleType;

        ZoomVariables(float scale, float focusX, float focusY, ScaleType scaleType) {
            this.scale = scale;
            this.focusX = focusX;
            this.focusY = focusY;
            this.scaleType = scaleType;
        }
    }
}