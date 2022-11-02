package utils

object DueUtility {

    //NOTE: JvmStatic annotation means that the categories variable is static (i.e. we can reference it through the class
    //      name; we don't have to create an object of CategoryUtility to use it.
    @JvmStatic
    val dueOptions = setOf ("Day","Week","Month","Year")

    @JvmStatic
    fun isValidDueDate(duedateToCheck: String?): Boolean {
        for (dueDate in dueOptions) {
            if (dueDate.equals(duedateToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }


}