package tie.hackathon.travelguide.Tests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.runner.RunWith;

import tie.hackathon.travelguide.login.LoginActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by valentin.boca on 2/21/2018.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EspressoTestBase {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    public static UiDevice device = UiDevice.getInstance(getInstrumentation());
}
