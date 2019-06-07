/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;

import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Interactive UI tests for the home screen. Tests included logging in and out, using the search
 * button, and using the favorites list button.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Tests launching the search function.
     */
    @Test
    public void testLaunchSearch() {
        onView(withId(R.id.angry_btn_search))
                .perform(click());
        onView(allOf(withId(R.id.button), withText("Search")))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests launching the favorites list.
     */
    @Test
    public void testLaunchFavorites() {
        //TODO
    }

    /**
     * Tests launching the login window.
     */
    @Test
    public void testLaunchLogin() {
        onView(withId(R.id.login_btn)).perform(click());
        onView(allOf(withText("Login"))).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Before
    public void logoutUser() {
        try {
            onView(withId(R.id.logout_btn)).perform(click());
        } catch(PerformException e) {
            // Nothing here.
        }
    }

    /**
     * Tests actually logging in.
     */
    @Test
    public void testLogin() {
        onView(withId(R.id.login_btn)).perform(click());
        onView(allOf(withHint(R.string.email_hint))).perform(typeText("a@m.com"));
        onView(allOf(withHint(R.string.pwd_hint))).perform(typeText("abc123"));
        onView(allOf(withText("Login"))).perform(click());

        onView(allOf(withText("Signed in as: a@m.com"))).inRoot(withDecorView(not(is(
                mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests logging out.
     */
    @Test
    public void testLogout() {
        // Login first.
        onView(withId(R.id.login_btn)).perform(click());
        onView(allOf(withHint(R.string.email_hint))).perform(typeText("a@m.com"));
        onView(allOf(withHint(R.string.pwd_hint))).perform(typeText("abc123"));
        onView(allOf(withText("Login"))).perform(click());

        // Logout.
        onView(withId(R.id.logout_btn)).perform(click());
        onView(allOf(withText("Logged out"))).inRoot(withDecorView(not(is(mActivityRule.getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }
}
