package utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class WeatherUtils {

    /**
     * invoked to get the right weather icon vector drawable resource file identifier
     * @param context context to access application resources
     * @param iconUrl weather icon image url obtained from the API call
     * @param code integer code used to fetch the weather description
     * @return resource id of the vector file
     */
    public static int fetchDrawableFileResource(Context context, String iconUrl, int code)
            throws JSONException, IOException {
        String imageDrawable = "wi_";
        String time = iconUrl.substring(iconUrl.lastIndexOf("/") + 1);
        imageDrawable += time.contains("d") ? "day" : "night";
        String suffix = getSuffix(context, code);
        imageDrawable += "_" + suffix;

        return context.getResources().getIdentifier(imageDrawable, "drawable", "io.github.project_travel_mate");
    }

    /**
     * parses the icons.json file which contains the weather condition codes and descriptions
     * required to fetch the right weather icon to display
     *
     *
     * @param context context to access application resources
     * @param code weather condition code
     * @return weather condition description
     */
    private static String getSuffix(Context context, int code) throws JSONException, IOException {
        String json;
        String cond = "";
        try {
            InputStream is = context.getAssets().open("icons.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(String.valueOf(code))) {
                JSONObject object = jsonObject.getJSONObject(String.valueOf(code));
                cond = object.getString("icon");
            }

        } catch (JSONException ex) {
            throw new JSONException(ex.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return cond;
    }

    /**
     * called to get the days of the week needed to display the forecast
     * @param index day's index
     * @param pattern pattern of output day of week
     * @return current day of the week as a String
     */
    public static String getDayOfWeek(int index, String pattern) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, index);
        return dateFormat.format(calendar.getTime());
    }

}
