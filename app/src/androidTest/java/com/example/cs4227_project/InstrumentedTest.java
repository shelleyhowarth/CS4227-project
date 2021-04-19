package com.example.cs4227_project;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

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
            logIn();
            adapterPattern();
        }
    }

    //PASSING
    @Test
    public void logIn() throws InterruptedException {
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
            logIn();
        }
    }

    //PASSING
    @Test
    public void builderPattern() throws InterruptedException {
        try {
            //Checks and clicks on 'Clothes'
            onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
            Thread.sleep(LONG_WAIT);

            //Clicks on first product in recycler view
            onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
            TextView item = activityRule.getActivity().findViewById(R.id.productName);
            TextView cost = activityRule.getActivity().findViewById(R.id.productPrice);
            String itemName = item.getText().toString();
            String orderTotal = item.getText().toString();

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
            onView(withId(R.id.submitButton)).check(matches(isDisplayed())).perform(click());
            onView(withText("Ok")).perform(click());

            //Check that order has been placed
            onView(withId(R.id.ordersBtn)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.simpleRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.layout)).check(matches(isDisplayed()));
            onView(withId(R.id.heading)).check(matches(withText("Order Summary")));
            onView(withId(R.id.orderItems)).check(matches(withText(itemName)));
            onView(withId(R.id.orderTotal)).check(matches(withText(orderTotal)));

        } catch(NoMatchingViewException e) { //If not on home page
            //Click back
            onView(isRoot()).perform(pressBack());
            Thread.sleep(SHORT_WAIT);
            //Recall builderPattern
            builderPattern();
        }
    }

    @Test
    public void mementoPattern() throws InterruptedException {
        //Checks and clicks on 'Clothes'
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);
        //Clicks on first product in recycler view
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(SHORT_WAIT);
        //Getting name of item
        TextView item = activityRule.getActivity().findViewById(R.id.productName);
        String itemName = item.getText().toString();

        //Input quantity
        int itemQuantity = 1;
        onView(withId(R.id.quantity)).perform(typeText("1"));
        Thread.sleep(SHORT_WAIT);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);
        //Add item to cart
        onView(withId(R.id.addToCart)).check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);
        //Go to cart fragment
        onView(withId(R.id.cartBtn)).check(matches(isDisplayed())).perform(click());
        //Click checkout
        onView(withId(R.id.checkout)).check(matches(isDisplayed())).perform(click());
        //Input address and card details
        onView(withId(R.id.townInput)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.townInput)).perform(typeText("Annagh"));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cityInput)).perform(click());
        onView(withId(R.id.cityInput)).perform(typeText("Tralee"));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.countyInput)).perform(click());
        onView(withId(R.id.countyInput)).perform(typeText("Co. Kerry"));
        //enter test card details
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cardNameInput)).perform(click());
        onView(withId(R.id.cardNameInput)).perform(typeText("Shelley Howarth"));
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cardNumInput)).perform(click());
        onView(withId(R.id.cardNumInput)).perform(typeText("1234567812345678"));
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.expiryDateInput)).perform(click());
        onView(withId(R.id.expiryDateInput)).perform(typeText("12/2021"));
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cvvInput)).perform(click());
        onView(withId(R.id.cvvInput)).perform(typeText("123"));
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);
        //click order button
        onView(withId(R.id.submitButton)).perform(click());
        Thread.sleep(LONG_WAIT);
        //Click confirm order
        onView(withText("Undo")).perform(click());
    }

    /*@Test
    public void verifyFilterDataPopulation() throws InterruptedException {
        //go to view clothes
        checkViewProductsType(R.id.clothesButton);
        checkSpinnerDefaultValues();
        for(ClothesStyles e : ClothesStyles.values()) {
            onView(withId(R.id.styleSpinner)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onData(allOf(is(instanceOf(String.class)), is(e.getValue()))).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.styleSpinner))
                    .check(matches(withSpinnerText(containsString(e.getValue()))));
            Thread.sleep(SHORT_WAIT);
        }
        verifyBrandSpinner();
        verifyAlphaSizesSpinner();
        verifyColourSpinner();
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);

        //go to view accessories
        checkViewProductsType(R.id.accButton);
        checkSpinnerDefaultValues();
        for(AccessoryStyles e : AccessoryStyles.values()) {
            onView(withId(R.id.styleSpinner)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onData(allOf(is(instanceOf(String.class)), is(e.getValue()))).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.styleSpinner))
                    .check(matches(withSpinnerText(containsString(e.getValue()))));
            Thread.sleep(SHORT_WAIT);
        }
        verifyBrandSpinner();
        verifyAlphaSizesSpinner();
        verifyColourSpinner();
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);

        //go to view shoes
        checkViewProductsType(R.id.shoeButton);
        checkSpinnerDefaultValues();
        for(ShoeStyles e : ShoeStyles.values()) {
            onView(withId(R.id.styleSpinner)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onData(allOf(is(instanceOf(String.class)), is(e.getValue()))).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.styleSpinner))
                    .check(matches(withSpinnerText(containsString(e.getValue()))));
            Thread.sleep(SHORT_WAIT);
        }
        verifyBrandSpinner();
        verifyColourSpinner();
        for(NumericalSize e : NumericalSize.values()) {
            onView(withId(R.id.sizeSpinner)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onData(allOf(is(instanceOf(String.class)), is(e.getValue()))).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.sizeSpinner))
                    .check(matches(withSpinnerText(containsString(e.getValue()))));
            Thread.sleep(SHORT_WAIT);
        }
    }

    @Test
    public void verifyFilterProductsUseCase() throws InterruptedException {
        //go to view clothes
        checkViewProductsType(R.id.clothesButton);
        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.recycler_view);
        int originalItemCount = recyclerView.getChildCount();

        //set size spinner to 'S'
        onView(withId(R.id.sizeSpinner)).perform(click());
        Thread.sleep(SHORT_WAIT);
        onData(allOf(is(instanceOf(String.class)), is(AlphaSize.SMALL.getValue()))).perform(click());
        Thread.sleep(SHORT_WAIT);

        //set style spinner to 'Hoodie'
        onView(withId(R.id.styleSpinner)).perform(click());
        Thread.sleep(SHORT_WAIT);
        onData(allOf(is(instanceOf(String.class)), is(ClothesStyles.HOODIES.getValue()))).perform(click());
        Thread.sleep(SHORT_WAIT);

        //set colour spinner to 'Red'
        onView(withId(R.id.colourSpinner)).perform(click());
        Thread.sleep(SHORT_WAIT);
        onData(allOf(is(instanceOf(String.class)), is(Colour.RED.getValue()))).perform(click());
        Thread.sleep(SHORT_WAIT);

        //set brand spinner to 'Nike'
        onView(withId(R.id.brandSpinner)).perform(click());
        Thread.sleep(SHORT_WAIT);
        onData(allOf(is(instanceOf(String.class)), is(Brand.NIKE.getValue()))).perform(click());
        Thread.sleep(SHORT_WAIT);

        //click filter and verify there are now less products displayed
        onView(withId(R.id.filter)).perform(click());
        Thread.sleep(SHORT_WAIT);
        int filteredCount = recyclerView.getChildCount();
        Assert.assertEquals(true, originalItemCount > filteredCount);

        //click reset filters and verify all products are now displayed
        onView(withId(R.id.resetFilters)).perform(click());
        Thread.sleep(SHORT_WAIT);
        Assert.assertEquals(true, originalItemCount == recyclerView.getChildCount());
    }*/

    /*@Test
    public void verifyPurchaseProductUseCase() throws InterruptedException {
        checkViewProductsType(R.id.clothesButton);
        //click on first product in recycler view
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.productImage)).check(matches(isDisplayed()));
        //add product to cart
        onView(withId(R.id.addToCart)).perform(click());
        Thread.sleep(SHORT_WAIT);
        TextView tv = activityRule.getActivity().findViewById(R.id.productName);
        String name = (String) tv.getText();
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);

        //log in if not logged in
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();
        if(loginText.equals("Log In")) {
            onView(withId(R.id.logInBtn)).perform(click());
            Thread.sleep(LONG_WAIT);
            onView(withId(R.id.fieldEmail)).check(matches(isDisplayed()));
            onView(withId(R.id.fieldEmail)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.fieldEmail)).perform(typeText("carla@gmail.com"));
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.fieldPassword)).perform(click());
            onView(withId(R.id.fieldPassword)).perform(typeText("testpassword"));
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.signIn)).perform(click());
            Thread.sleep(LONG_WAIT);
        }

        //go to cart
        onView(withId(R.id.cartBtn)).perform(click());
        Thread.sleep(MED_WAIT);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.productImage)).check(matches(isDisplayed()));
        TextView tvCart = activityRule.getActivity().findViewById(R.id.productName);
        String nameCart = (String) tvCart.getText();
        Assert.assertEquals("View Product Name matches Cart Product Name", true, name.equals(nameCart));
        onView(isRoot()).perform(pressBack());
        Thread.sleep(SHORT_WAIT);

        //go to checkout
        onView(withId(R.id.checkout)).perform(click());
        Thread.sleep(LONG_WAIT);
        //enter test address
        onView(withId(R.id.townInput)).check(matches(isDisplayed()));
        onView(withId(R.id.townInput)).perform(click());
        onView(withId(R.id.townInput)).perform(typeText("testTown"));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cityInput)).perform(click());
        onView(withId(R.id.cityInput)).perform(typeText("testCity"));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.countyInput)).perform(click());
        onView(withId(R.id.countyInput)).perform(typeText("testCounty"));
        //enter test card details
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cardNameInput)).perform(click());
        onView(withId(R.id.cardNameInput)).perform(typeText("testCardName"));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cardNumInput)).perform(click());
        onView(withId(R.id.cardNumInput)).perform(typeText("1234567812345678"));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.expiryDateInput)).perform(click());
        onView(withId(R.id.expiryDateInput)).perform(typeText("12/2021"));
        Thread.sleep(SHORT_WAIT);
        onView(withId(R.id.cvvInput)).perform(click());
        onView(withId(R.id.cvvInput)).perform(typeText("123"));
        onView(isRoot()).perform(pressBack());
        //click on order button and verify order confirmation
        onView(withId(R.id.submitButton)).perform(click());
        Thread.sleep(LONG_WAIT);
        onView(withText("Ok")).perform(click());
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed()));
    }

    public void checkViewProductsType(int buttonId) throws InterruptedException {
        //go to selected view
        onView(withId(buttonId)).check(matches(isDisplayed()));
        onView(withId(buttonId)).perform(click());
        Thread.sleep(LONG_WAIT);
        //check if recycler view is displayed and populated with data
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));

    }

    public void checkSpinnerDefaultValues() {
        onView(withId(R.id.styleSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.colourSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.brandSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.sizeSpinner)).check(matches(isDisplayed()));

        onView(withId(R.id.styleSpinner)).check(matches(withSpinnerText("All")));
        onView(withId(R.id.colourSpinner)).check(matches(withSpinnerText("All")));
        onView(withId(R.id.brandSpinner)).check(matches(withSpinnerText("All")));
        onView(withId(R.id.sizeSpinner)).check(matches(withSpinnerText("All")));
    }*/

    /*public void verifyBrandSpinner() throws InterruptedException {
        for(Brand e : Brand.values()) {
            onView(withId(R.id.brandSpinner)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onData(allOf(is(instanceOf(String.class)), is(e.getValue()))).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.brandSpinner))
                    .check(matches(withSpinnerText(containsString(e.getValue()))));
            Thread.sleep(SHORT_WAIT);
        }
    }

    public void verifyColourSpinner() throws InterruptedException {
        for(Colour e : Colour.values()) {
            onView(withId(R.id.colourSpinner)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onData(allOf(is(instanceOf(String.class)), is(e.getValue()))).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.colourSpinner))
                    .check(matches(withSpinnerText(containsString(e.getValue()))));
            Thread.sleep(SHORT_WAIT);
        }
    }

    public void verifyAlphaSizesSpinner() throws InterruptedException {
        for(AlphaSize e : AlphaSize.values()) {
            onView(withId(R.id.sizeSpinner)).perform(click());
            Thread.sleep(SHORT_WAIT);
            onData(allOf(is(instanceOf(String.class)), is(e.getValue()))).perform(click());
            Thread.sleep(SHORT_WAIT);
            onView(withId(R.id.sizeSpinner))
                    .check(matches(withSpinnerText(containsString(e.getValue()))));
            Thread.sleep(SHORT_WAIT);
        }
    }*/
}
