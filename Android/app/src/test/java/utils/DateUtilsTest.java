package utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void rfc3339ToMills() {
        assertEquals(1572374198000L , DateUtils.rfc3339ToMills("2019-10-29T18:36:38.000GMT").longValue());
        long epoch = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        assertTrue(((epoch - DateUtils.rfc3339ToMills("abc").longValue()) < 1000));
        epoch = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        assertTrue(((epoch - DateUtils.rfc3339ToMills("10-23-2001").longValue()) < 1000));
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
