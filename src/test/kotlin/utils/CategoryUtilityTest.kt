package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.CategoryUtility.categories
import utils.CategoryUtility.isValidCategory

internal class CategoryUtilityTest {
    @Test
    fun categoriesReturnsFullCategoriesSet(){
        Assertions.assertEquals(6, categories.size)
        Assertions.assertTrue(categories.contains("Household"))
        Assertions.assertTrue(categories.contains("College"))
        Assertions.assertTrue(categories.contains("Work"))
        Assertions.assertTrue(categories.contains("Wellbeing"))
        Assertions.assertTrue(categories.contains("Fitness"))
        Assertions.assertTrue(categories.contains("Recreational"))
        Assertions.assertFalse(categories.contains(""))
    }

    @Test
    fun isValidCategoryTrueWhenCategoryExists(){
        Assertions.assertTrue(isValidCategory("Household"))
        Assertions.assertTrue(isValidCategory("household"))
        Assertions.assertTrue(isValidCategory("COLLEGE"))
        Assertions.assertTrue(isValidCategory("FITNESS"))
        Assertions.assertTrue(isValidCategory("RecReationaL"))
        Assertions.assertTrue(isValidCategory("wellbeinG"))
    }

    @Test
    fun isValidCategoryFalseWhenCategoryDoesNotExist(){
        Assertions.assertFalse(isValidCategory("Househol"))
        Assertions.assertFalse(isValidCategory("colllege"))
        Assertions.assertFalse(isValidCategory("wellbein"))
        Assertions.assertFalse(isValidCategory("recreation"))
        Assertions.assertFalse(isValidCategory(""))
    }
}