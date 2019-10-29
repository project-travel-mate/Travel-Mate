package utils;


import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void rfc3339ToMills() {
        assertEquals(1572374198000L , DateUtils.rfc3339ToMills("2019-10-29T18:36:38.000GMT").longValue());
    }
    @Test
    public void getDate_with_mils() {
        assertEquals("29 Oct 2019" , DateUtils.getDate(1572374198000L));
    }

    @Test
    public void getDate_with_prevDays() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(LocalDate.now().minusDays(4).format(formatter) , DateUtils.getDate(4));
        assertEquals(LocalDate.now().format(formatter) , DateUtils.getDate(0));
        assertEquals(LocalDate.now().format(formatter) , DateUtils.getDate(-1));
    }
}
