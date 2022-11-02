package utils

import utils.ScannerInput.readNextInt
import java.util.*

object ValidateInput {

    @JvmStatic
    fun readValidCategory(prompt: String?): String {
        print(prompt)
        var input = Scanner(System.`in`).nextLine()
        do {
            if (CategoryUtility.isValidCategory(input))
                return input
            else {
                print("Invalid category $input.  Please try again: ")
                input = Scanner(System.`in`).nextLine()
            }
        } while (true)
    }

    @JvmStatic
    fun readValidPriority(prompt: String?): Int {
        var input =  readNextInt(prompt)
        do {
            if (Utilities.validRange(input, 1 ,5))
                return input
            else {
                print("Invalid priority $input.")
                input = readNextInt(prompt)
            }
        } while (true)
    }

    @JvmStatic
    fun readValidProgress(prompt: String?): String {
        print(prompt)
        var input = Scanner(System.`in`).nextLine()
        do {
            if (ProgressUtility.isValidProgress(input))
                return input
            else {
                print("Invalid progress point: $input.  Please try again: ")
                input = Scanner(System.`in`).nextLine()
            }
        } while (true)
    }

    @JvmStatic
    fun readValidDueDate(prompt: String?): String {
        print(prompt)
        var input = Scanner(System.`in`).nextLine()
        do {
            if (DueUtility.isValidDueDate(input))
                return "This note is due by end of the $input"
            else {
                print("Invalid due date: $input.  Please try again: ")
                input = Scanner(System.`in`).nextLine()
            }
        } while (true)
    }

}