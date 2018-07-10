package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final long ONE_SECOND_MILLS = 1000L;
    private static final long ONE_MINUTE_MILLS = ONE_SECOND_MILLS * 60;
    private static final long ONE_HOUR_MILLS = ONE_MINUTE_MILLS * 60;
    private static final long ONE_DAY_MILLS = ONE_HOUR_MILLS * 24;
    private static final long ONE_MONTH_MILLS = ONE_HOUR_MILLS * 31;

    private static int timelineOffset = TimeZone.getDefault().getRawOffset();

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    /**
     * Convert rfc3339 format received from the server to time in millis
     * @param dateInString - input date in string
     * @return date in milliseconds
     */
    public static Long rfc3339ToMills(String dateInString) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSSSSS", Locale.getDefault());
        try {
            cal.setTime(simpleDateFormat.parse(dateInString
                    .replace("Z", "")
                    .replace("T", "-")
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getTimeInMillis() + timelineOffset;
    }

    /**
     * Convert time in millis to date
     * @param timeMills - input time in milliseconds
     * @return input date in date format
     */
    public static String getDate(Long timeMills) {
        long duration = System.currentTimeMillis() - timeMills;
        return dateFormatter.format(new Date(timeMills));
    }
}
