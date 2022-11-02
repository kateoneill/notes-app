import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.ProgressUtility.isValidProgress
import utils.ProgressUtility.progressPoints

internal class ProgressUtilityTest {
    @Test
    fun categoriesReturnsFullProgressPointSet(){
        Assertions.assertEquals(3, progressPoints.size)
        Assertions.assertTrue(progressPoints.contains("To-do"))
        Assertions.assertTrue(progressPoints.contains("Doing"))
        Assertions.assertTrue(progressPoints.contains("Done"))
        Assertions.assertFalse(progressPoints.contains(""))
    }

    @Test
    fun isValidProgressPointTrueWhenCategoryExists(){
        Assertions.assertTrue(isValidProgress("To-do"))
        Assertions.assertTrue(isValidProgress("TO-DO"))
        Assertions.assertTrue(isValidProgress("DoiNg"))
        Assertions.assertTrue(isValidProgress("done"))
    }

    @Test
    fun isValidProgressPointFalseWhenCategoryDoesNotExist(){
        Assertions.assertFalse(isValidProgress("todo"))
        Assertions.assertFalse(isValidProgress("finished"))
        Assertions.assertFalse(isValidProgress("don"))
        Assertions.assertFalse(isValidProgress("doin"))
        Assertions.assertFalse(isValidProgress(""))
    }
}