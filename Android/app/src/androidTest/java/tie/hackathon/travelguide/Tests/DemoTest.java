package tie.hackathon.travelguide.Tests;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by valentin.boca on 2/21/2018.
 */

public class DemoTest extends EspressoTestBase {

    @Test
    public void testDemo() throws Exception {
        onView(withContentDescription("SIGNUP")).perform(click());
    }

    @Test
    public void testDemo2() throws Exception {
        onView(allOf(withText("Travel Mate"), hasSibling(withContentDescription("Travel Guide")))).check(matches(isDisplayed()));
    }
}
