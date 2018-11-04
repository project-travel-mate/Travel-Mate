package io.github.project_travel_mate.destinations.offlinedata;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import utils.DateConverter;
import utils.GeopointsConverter;


/**
 * Created by Ajay Deepak on 29-10-2018.
 */

@Entity (tableName = "offline_maps")
@TypeConverters({DateConverter.class, GeopointsConverter.class})
public class OfflineData implements Serializable {

    private Context mContext;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private  int mId = 0;

    @ColumnInfo(name = "geopoint")

    private GeoPoint mGeoPoint;

    @ColumnInfo(name = "zoomin")
    private int mZoomIn;

    @ColumnInfo(name = "zoomout")
    private int mZoomOut;

    @ColumnInfo(name = "created_date")

    private Date mCreatedDate;

    public OfflineData(Context context, GeoPoint geoPoint, int zoomIn, int zoomOut) {
        this.mContext = context;
        this.mGeoPoint = geoPoint;
        this.mZoomIn = zoomIn;
        this.mZoomOut = zoomOut;
    }

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(Date mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public GeoPoint getGeoPoints() {
        return mGeoPoint;
    }

    public void setGeoPoints(GeoPoint geoPoints) {
        this.mGeoPoint = geoPoints;
    }

    public int getZoomIn() {
        return mZoomIn;
    }

    public void setZoomIn(int zoomIn) {
        this.mZoomIn = zoomIn;
    }

    public int getZoomOut() {
        return mZoomOut;
    }

    public void setZoomOut(int zoomOut) {
        this.mZoomOut = zoomOut;
    }

}
