package utils

object ProgressUtility {

    //NOTE: JvmStatic annotation means that the categories variable is static (i.e. we can reference it through the class
    //      name; we don't have to create an object of CategoryUtility to use it.
    @JvmStatic
    val progressPoints = setOf ("To-do", "Doing", "Done")

    @JvmStatic
    fun isValidProgress(progressToCheck: String?): Boolean {
        for (progress in progressPoints) {
            if (progress.equals(progressToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

}