package sa.ksu.gpa.saleem

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun isCaloriCorrect() {
        var Ingredient="حليب"
        var Measurment="مل"
        var Quantity="100"
        var actual=CaloriCalculater.calculateCalories(Ingredient,Measurment,Quantity)
        var excpected=56
        assertEquals(excpected, actual)
    }
}
