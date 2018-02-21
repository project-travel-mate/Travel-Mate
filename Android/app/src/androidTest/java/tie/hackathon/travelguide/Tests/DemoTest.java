package tie.hackathon.travelguide.Tests;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;

/**
 * Created by valentin.boca on 2/21/2018.
 */

public class DemoTest extends EspressoTestBase {

    @Test
    public void testDemo() throws Exception {
        onView(withContentDescription("SIGNUP")).perform(click());
    }
}
