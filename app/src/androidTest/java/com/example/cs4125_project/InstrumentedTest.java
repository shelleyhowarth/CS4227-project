package com.example.cs4125_project;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cs4125_project.enums.AccessoryStyles;
import com.example.cs4125_project.enums.AlphaSize;
import com.example.cs4125_project.enums.Brand;
import com.example.cs4125_project.enums.ClothesStyles;
import com.example.cs4125_project.enums.Colour;
import com.example.cs4125_project.enums.NumericalSize;
import com.example.cs4125_project.enums.ShoeStyles;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.contrib.RecyclerViewActions.*;
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
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void verifyViewProductsUseCase() throws InterruptedException {
        //go to view clothes
        checkViewProductsType(R.id.clothesButton);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);
        //go to view shoes
        checkViewProductsType(R.id.shoeButton);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);
        //go to view accessories
        checkViewProductsType(R.id.accButton);
    }

    @Test
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
    }

    public void verifyBrandSpinner() throws InterruptedException {
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
    }
}
