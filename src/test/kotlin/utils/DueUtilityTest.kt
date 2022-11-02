import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.DueUtility

internal class DueUtilityTest {
    @Test
    fun categoriesReturnsFullDueDateSet(){
        Assertions.assertEquals(4, DueUtility.dueOptions.size)
        Assertions.assertTrue(DueUtility.dueOptions.contains("Year"))
        Assertions.assertTrue(DueUtility.dueOptions.contains("Week"))
        Assertions.assertTrue(DueUtility.dueOptions.contains("Month"))
        Assertions.assertTrue(DueUtility.dueOptions.contains("Day"))
        Assertions.assertFalse(DueUtility.dueOptions.contains(""))
    }

    @Test
    fun isValidCategoryTrueWhenDueDateExists(){
        Assertions.assertTrue(DueUtility.isValidDueDate("Year"))
        Assertions.assertTrue(DueUtility.isValidDueDate("year"))
        Assertions.assertTrue(DueUtility.isValidDueDate("MONTH"))
        Assertions.assertTrue(DueUtility.isValidDueDate("dAy"))
        Assertions.assertTrue(DueUtility.isValidDueDate("week"))
    }

    @Test
    fun isValidCategoryFalseWhenDueDateDoesNotExist(){
        Assertions.assertFalse(DueUtility.isValidDueDate("yer"))
        Assertions.assertFalse(DueUtility.isValidDueDate("mont"))
        Assertions.assertFalse(DueUtility.isValidDueDate(""))
    }
}