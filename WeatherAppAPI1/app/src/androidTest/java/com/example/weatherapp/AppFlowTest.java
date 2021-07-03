package com.example.weatherapp;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import com.example.weatherapp.Activities.MainActivity;
import com.example.weatherapp.Uitls.NetworkService;
import com.jraska.livedata.TestObserver;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class AppFlowTest {
    private static final String TAG = "AppFlowTest";
    private static final String TEST_MOCK_GPS_LOCATION = "TEST_MOCK_GPS_LOCATION";
    private String stringToBetyped;
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, true);
    @Rule
    public ActivityScenarioRule<MainActivity> activityRuleMain
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        stringToBetyped = "Example";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.city_name_edit))
                .perform(typeText(stringToBetyped), closeSoftKeyboard());
        onView(withId(R.id.city_name_edit))
                .check(matches(withText(stringToBetyped)));

    }

    @Test
    public void deviceOrientationTest(){
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final int orientation = InstrumentationRegistry.getTargetContext()
                .getResources()
                .getConfiguration()
                .orientation;
        final int newOrientation = (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        activityRule.getActivity().setRequestedOrientation(newOrientation);

        getInstrumentation().waitForIdle(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
            assertEquals( activityRule.getActivity().getRequestedOrientation(),ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } catch (InterruptedException e) {
            throw new RuntimeException("Screen rotation failed", e);
        }
    }

    @Test
    public void gpsSensorTest() throws Exception {
        LocationManager locationManager = (LocationManager)androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext().getSystemService(Context.LOCATION_SERVICE);
        if (isMockLocationEnabled(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext())) {
            throw new Exception("App Weather Application as mock location app in Developers -> Select Mock Location App -> Weather App");
        }else {
            List providers = locationManager.getAllProviders();
            if (!((List) providers).contains(TEST_MOCK_GPS_LOCATION)) {
                locationManager.addTestProvider(TEST_MOCK_GPS_LOCATION, false, false, false, false, false, false, false, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
                locationManager.setTestProviderEnabled(TEST_MOCK_GPS_LOCATION, true);
                Location location = new Location(TEST_MOCK_GPS_LOCATION);
                location.setLatitude(34.1233400);
                location.setLongitude(15.6777880);
                location.setAccuracy(7);
                location.setTime(8);
                location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                locationManager.setTestProviderLocation(TEST_MOCK_GPS_LOCATION, location);
                Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
                if (locationJellyBeanFixMethod != null) {
                    locationJellyBeanFixMethod.invoke(location);
                }
            } else {
                locationManager.removeTestProvider(TEST_MOCK_GPS_LOCATION);
            }
        }
    }

    public static Boolean isMockLocationEnabled(Context context) {
        return !Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
    }
    public static boolean isMockLocation(Location location) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && location != null && location.isFromMockProvider();
    }
//    @Test
//    @LargeTest
//    public void appApiTest() {
//        final MainActivity mainActivity = activityRule.getActivity();
//
//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                NetworkService networkService = new NetworkService(ApplicationProvider.getApplicationContext());
//                networkService.callAPIForResult("Null");
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                    TestObserver.test(networkService.finalJson)
//                            .awaitValue()
//                            .assertHasValue();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                JSONObject jsonObject = networkService.finalJson.getValue();
//                assertNull(jsonObject);
//            }
//        });
//    }


}
