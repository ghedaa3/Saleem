package sa.ksu.gpa.saleem

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("sa.ksu.gpa.saleem", appContext.packageName)
    }

    @get:Rule
    var mActivityRule = ActivityTestRule(
        MainActivity::class.java)
    @Test
    fun addWater(){
        // this method is testing if the user is able to add an excercize


        Espresso.onView(ViewMatchers.withId(R.id.addWaterBtn)).perform(ViewActions.click())

    }

}
