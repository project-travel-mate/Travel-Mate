package utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Santosh on 31-08-2018.
 */
public class Compass implements SensorEventListener {
    private static final String TAG = "Compass";

    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }

    private CompassListener mListener;

    private SensorManager mSensorManager;
    private Sensor mGsensor;
    private Sensor mSensor;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] mR = new float[9];
    private float[] mI = new float[9];

    private float mAzimuth;
    private float mAzimuthFix;

    public Compass(Context context) {
        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mGsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    /**
     * start compass
     */
    public void start() {
        mSensorManager.registerListener(this, mGsensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * stop compass
     */
    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public void setAzimuthFix(float fix) {
        mAzimuthFix = fix;
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0);
    }

    public void setListener(CompassListener l) {
        mListener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];



                // mGravity = event.values;

                // Log.e(TAG, Float.toString(mGravity[0]));
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
                // Log.e(TAG, Float.toString(event.values[0]));

            }

            boolean success = SensorManager.getRotationMatrix(mR, mI, mGravity,
                    mGeomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(mR, orientation);
                // Log.d(TAG, "azimuth (rad): " + azimuth);
                mAzimuth = (float) Math.toDegrees(orientation[0]); // orientation
                mAzimuth = (mAzimuth + mAzimuthFix + 360) % 360;
                // Log.d(TAG, "azimuth (deg): " + azimuth);
                if (mListener != null) {
                    mListener.onNewAzimuth(mAzimuth);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
