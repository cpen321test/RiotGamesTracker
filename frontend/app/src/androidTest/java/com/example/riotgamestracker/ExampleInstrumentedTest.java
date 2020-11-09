package com.example.riotgamestracker;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void searchButtonDisabledInitially() {
        onView(withId(R.id.searchButton)).check(matches(not(isEnabled())));
    }

    @Test
    public void searchButtonDisabledWhenSearchBoxEmpty() {
        onView(withId(R.id.search)).perform(typeText(""));
        onView(withId(R.id.searchButton)).check(matches(not(isEnabled())));
    }

    @Test
    public void searchButtonEnabledWhenSearchBoxNotEmpty() {
        onView(withId(R.id.search)).perform(typeText("test"));
        onView(withId(R.id.searchButton)).check(matches(isEnabled()));
    }

}