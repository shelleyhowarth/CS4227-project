package com.example.cs4227_project;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.hasEntry;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    private final static int LONG_WAIT = 1000;
    private final static int MED_WAIT = 500;
    private final static int SHORT_WAIT = 250;
    private String orderTotal = "";
    private String orderTime = "Date purchased: ";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    //FAILING - ADD A WAIT FOR THE RECYCLER VIEW TO BE DISPLAYED
    @Test
    public void adapterPattern() throws InterruptedException {
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        //Check if logged in
        if(loginText.equals("Log Out")) {
            //Go to orders fragment
            onView(withId(R.id.ordersBtn)).check(matches(isDisplayed())).perform(click());

            //Count items in recycler view
            RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.simpleRecyclerView);
            int originalItemCount = recyclerView.getChildCount();
            //If there are orders
            if(originalItemCount > 0) {
                onView(withId(R.id.simpleRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
                Thread.sleep(SHORT_WAIT);
                onView(withId(R.id.layout)).check(matches(isDisplayed()));
                onView(withId(R.id.heading)).check(matches(withText("Order Summary")));
                //If there are no orders
            } else {
                //builderPattern to create an order and recall adapterPattern()
                builderPattern();
                adapterPattern();
            }
        //If logged out
        } else {
            //Log in and recall adapterPattern()
            logInUser();
            adapterPattern();
        }
    }

    //PASSING
    @Test
    public void logInUser() throws InterruptedException {
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();
        if(loginText.equals("Log In")) {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).perform(typeText("testuser@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.fieldPassword)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
            onView(withId(R.id.signIn)).check(matches(isDisplayed())).perform(click());
        } else {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            logInUser();
        }
    }

    @Test
    public void logInAdmin() throws InterruptedException {
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();
        if(loginText.equals("Log In")) {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).perform(typeText("testadmin@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.fieldPassword)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
            onView(withId(R.id.signIn)).check(matches(isDisplayed())).perform(click());
        } else {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            logInUser();
        }
    }

    //PASSING BUT NEED TO CHANGE THREAD SLEEPS
    @Test
    public void builderPattern() throws InterruptedException {
        try {
            Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
            String loginText = (String) b.getText();

            if(loginText.equals("Log Out")) {
                buyItem();
                onView(withText("Ok")).perform(click());

                //Check that order has been placed
                onView(withId(R.id.ordersBtn)).check(matches(isDisplayed())).perform(click());
                Thread.sleep(LONG_WAIT);
                onView(withId(R.id.simpleRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
                Thread.sleep(LONG_WAIT);
                onView(withId(R.id.layout)).check(matches(isDisplayed()));
                onView(withId(R.id.heading)).check(matches(withText("Order Summary")));
                onView(withId(R.id.orderTotal)).check(matches(withSubstring((orderTotal))));
                onView(withId(R.id.orderTime)).check(matches(withSubstring((orderTime))));
            } else {
                logInUser();
            }

        } catch(NoMatchingViewException e) { //If not on home page
            //Click back
            onView(isRoot()).perform(pressBack());
            Thread.sleep(SHORT_WAIT);
            //Recall builderPattern
            builderPattern();
        }
    }

    //PASSING BUT NEED TO CHANGE THREAD SLEEPS
    @Test
    public void buyItem() throws InterruptedException {
        //Checks and clicks on 'Clothes'
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        //Clicks on first product in recycler view
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(LONG_WAIT);
        TextView cost = activityRule.getActivity().findViewById(R.id.productPrice);
        orderTotal = cost.getText().toString();

        //Input quantity
        onView(withId(R.id.quantity)).perform(typeText("1"), closeSoftKeyboard());

        //Add item to cart
        onView(withId(R.id.addToCart)).check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(pressBack());

        //Go to cart fragment
        onView(withId(R.id.cartBtn)).check(matches(isDisplayed())).perform(click());

        //Click checkout
        onView(withId(R.id.checkout)).check(matches(isDisplayed())).perform(click());

        //Input address and card details
        onView(withId(R.id.townInput)).check(matches(isDisplayed())).perform(click(), typeText("Annagh"), closeSoftKeyboard());
        onView(withId(R.id.cityInput)).check(matches(isDisplayed())).perform(click(), typeText("Tralee"), closeSoftKeyboard());
        onView(withId(R.id.countyInput)).check(matches(isDisplayed())).perform(click(), typeText("Co. Kerry"), closeSoftKeyboard());

        //enter test card details
        onView(withId(R.id.cardNameInput)).check(matches(isDisplayed())).perform(click(), typeText("Shelley Howarth"), closeSoftKeyboard());
        onView(withId(R.id.cardNumInput)).check(matches(isDisplayed())).perform(click(), typeText("1234567812345678"), closeSoftKeyboard());
        onView(withId(R.id.expiryDateInput)).check(matches(isDisplayed())).perform(click(), typeText("12/2021"), closeSoftKeyboard());
        onView(withId(R.id.cvvInput)).perform(click());
        onView(withId(R.id.cvvInput)).check(matches(isDisplayed())).perform(click(), typeText("123"), closeSoftKeyboard());

        //click order button
        Date timeNow = new Date();
        onView(withId(R.id.submitButton)).check(matches(isDisplayed())).perform(click());
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
        orderTime += sfd.format(timeNow);
    }

    //PASSING BUT NEED TO CHANGE THREAD SLEEPS
    @Test
    public void mementoPattern() throws InterruptedException {
        buyItem();
        //Click confirm order
        onView(withText("Undo Order")).perform(click());

        //Check that order hasn't been placed
        onView(withId(R.id.ordersBtn)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.simpleRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.layout)).check(matches(isDisplayed()));
        onView(withId(R.id.heading)).check(matches(withText("Order Summary")));
        onView(withId(R.id.orderTotal)).check(matches(withSubstring((orderTotal))));
        onView(withId(R.id.orderTime)).check(matches(not(withSubstring((orderTime)))));
    }

    //PASSING BUT NEED TO CHANGE THREAD SLEEPS
    @Test
    public void interceptorPattern() throws InterruptedException {
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        if(loginText.equals("Log In")) {
            //Checks and clicks on 'Clothes'
            Thread.sleep(LONG_WAIT);
            onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
            Thread.sleep(LONG_WAIT);

            //Clicks on first product in recycler view
            onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            Thread.sleep(LONG_WAIT);
            TextView cost = activityRule.getActivity().findViewById(R.id.productPrice);
            orderTotal = cost.getText().toString();

            //Input quantity
            onView(withId(R.id.quantity)).perform(typeText("1"), closeSoftKeyboard());

            //Add item to cart
            onView(withId(R.id.addToCart)).check(matches(isDisplayed())).perform(click());
            onView(isRoot()).perform(pressBack());

            //Go to cart fragment
            onView(withId(R.id.cartBtn)).check(matches(isDisplayed())).perform(click());

            //Click checkout
            onView(withId(R.id.checkout)).check(matches(isDisplayed())).perform(click());

            //Check if log in screen appears
            onView(withId(R.id.fragment_log_in)).check(matches(isDisplayed()));

        } else {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            interceptorPattern();
        }
    }

    @Test
    public void commandPattern() throws InterruptedException {
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        if(loginText.equals("Log In")) {
            logInAdmin();
        } else {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            logInAdmin();
        }

        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.stockBtn)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);
        //Fill in form
        onView(withId(R.id.productName)).check(matches(isDisplayed())).perform(click(), typeText("Jeans"), closeSoftKeyboard());
        onView(withId(R.id.female)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.clothes)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.size1)).check(matches(isDisplayed())).perform(click(), typeText("12"), closeSoftKeyboard());
        onView(withId(R.id.quantity1)).check(matches(isDisplayed())).perform(click(), typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.price)).check(matches(isDisplayed())).perform(click(), typeText("17.50"), closeSoftKeyboard());
        onView(withId(R.id.style)).check(matches(isDisplayed())).perform(click(), typeText("Denim Mom Jeans"), closeSoftKeyboard());
        onView(withId(R.id.brand)).check(matches(isDisplayed())).perform(click(), typeText("New Look"), closeSoftKeyboard());
        onView(withId(R.id.colour)).check(matches(isDisplayed())).perform(click(), typeText("Blue"), closeSoftKeyboard());
        onView(withId(R.id.finish)).perform(scrollTo()).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);
        onView(ViewMatchers.withId(R.id.recycler_view))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("Jeans"))
                ));
    }

    


}
