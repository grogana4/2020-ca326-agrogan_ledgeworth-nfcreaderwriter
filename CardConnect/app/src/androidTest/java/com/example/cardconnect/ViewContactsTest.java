package com.example.cardconnect;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewContactsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void viewContactsTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_contacts), withText("Contacts"),
                        childAtPosition(
                                allOf(withId(R.id.activity_main),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withId(R.id.activity_contacts),
                                1)))
                .atPosition(0);
        appCompatTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.contact_info), withText("Name: luke\nPhone: 0831233986\nEmail: Hey@iol.ie\nAddress: 123 jeff lane\nWebsite: coolmathgames.com\nNotes: support me on fortnite with my creator code /smushsmish\nRead Time: 13:38\nRead Date: 04/03/2020\n"),
                        childAtPosition(
                                allOf(withId(R.id.activity_view_contact),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.btn_export_contact),
                        childAtPosition(
                                allOf(withId(R.id.activity_view_contact),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.btn_edit),
                        childAtPosition(
                                allOf(withId(R.id.activity_view_contact),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.btn_delete),
                        childAtPosition(
                                allOf(withId(R.id.activity_view_contact),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.contact_info), withText("Name: luke\nPhone: 0831233986\nEmail: Hey@iol.ie\nAddress: 123 jeff lane\nWebsite: coolmathgames.com\nNotes: support me on fortnite with my creator code /smushsmish\nRead Time: 13:38\nRead Date: 04/03/2020\n"),
                        childAtPosition(
                                allOf(withId(R.id.activity_view_contact),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Name: luke Phone: 0831233986 Email: Hey@iol.ie Address: 123 jeff lane Website: coolmathgames.com Notes: support me on fortnite with my creator code /smushsmish Read Time: 13:38 Read Date: 04/03/2020 ")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
