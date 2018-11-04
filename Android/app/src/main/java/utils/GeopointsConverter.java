package utils;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;



/**
 * Created by Ajay Deepak on 30-10-2018.
 */
public class GeopointsConverter {


    @TypeConverter
    public static GeoPoint stringToGeoPoint(String data) {
        return new Gson().fromJson(data, GeoPoint.class);
    }

    @TypeConverter
    public static String geoPointToString(GeoPoint geoPoint) {
        Gson gson = new Gson();
        String value = gson.toJson(geoPoint);
        //return new Gson().toJson(geoPoints);
        return value;
    }
}
