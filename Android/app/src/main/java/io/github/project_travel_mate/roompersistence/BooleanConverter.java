package io.github.project_travel_mate.roompersistence;

import androidx.room.TypeConverter;

// A TypeConverter class that will automatically convert the boolean type
// to String(s) in order to store in SQLite Db, and vice-versa.
public class BooleanConverter {
    @TypeConverter
    public static boolean boolFromString(String value) {
        return value.equals("1");
    }

    @TypeConverter
    public static String boolToString(boolean value) {
        return value ? "1" : "0";
    }
}
