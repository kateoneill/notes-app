import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.ValidateInput.readValidCategory
import utils.ValidateInput.readValidDueDate
import utils.ValidateInput.readValidPriority
import utils.ValidateInput.readValidProgress
import java.io.File
import java.lang.System.exit
private val logger = KotlinLogging.logger {}
//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))

val blue ="\u001b[34m"
val cyan = "\u001b[36m"
val red = "\u001b[31m"
val yellow = "\u001b[33m"
val green = "\u001b[32m"
val reset ="\u001b[0m"


fun main(args: Array<String>) {
    entryscreen()
}

fun entryscreen() {
    do {
        val option = entryscreendisplay()
        when (option){
            1 -> runMenu()
            else-> runMenu()
        }
    } while(true)
}

fun entryscreendisplay(): Int{
    return ScannerInput.readNextInt("""
          >   $blue  _   __      __               ___             
          >    / | / /___  / /____  _____   /   |  ____  ____
          >   /  |/ / __ \/ __/ _ \/ ___/  / /| | / __ \/ __ \
          >  / /|  / /_/ / /_/  __(__  )  / ___ |/ /_/ / /_/ /
          > /_/ |_/\____/\__/\___/____/  /_/  |_/ .___/ .___/ 
          >                                    /_/   /_/  $reset
          >                                    
          >        $cyan[Press any number key to enter]$reset
          >         
          >""".trimMargin(">"))

}

fun mainMenu() : Int {
    return ScannerInput.readNextInt("""
          >$red               _'_
          >            .-|   |-.
          >           /  .===.  \$reset
          >           $red\/$reset$blue 6   6$reset $red\/$reset
          >           (  $red\___/$reset  )
          >  ___ooo____\_______/_____________
          > |                                |
          > |        NOTE KEEPER APP         |
          > $green----------------------------------
          > |  NOTE MENU                     |
          > |   1) Add a note                |
          > |   2) List all notes            |
          > |   3) Update a note             |
          > |   4) Delete a note             |
          > |   5) Archive notes             |
          > |   6) Count notes               |
          > |   7) Search notes              |
          > |   10) Save notes               |
          > |   11) Load notes               |
          > ----------------------------------$reset 
          > |   0) Exit                      |
          > _________________________ooo______
          >         $cyan|  |      |  |
          >         |  |      |  |
          >         |$reset$blue-$reset $blue|      |$reset$blue-$reset $cyan|
          >         |  |      |  |
          >         |__|      |__|$reset
          >       $red  /- ')     (' -\
          >        (___/       \___)$reset
          > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            5 -> archiveNote()
            6 -> countNotes()
            7 -> searchNotes()
            10 -> save()
            11 -> load()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    }while (true)
}

fun addNote(){
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readValidCategory("Enter a category for the note: ")
    val noteProgress = readValidProgress("Enter a progress (To-do, doing, done): ")
    val collaborator = readNextLine("Enter a note collaborator: ")
    val dueBy = readValidDueDate("Add due by time (today, week, month, year): ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteProgress, collaborator,dueBy))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listAllNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readValidCategory("Enter a category for the note: ")
            val noteProgress = readValidProgress("Enter a progress (To-do, doing, done): ")
            val collaborator = readNextLine("Enter a note collaborator: ")
            val dueBy = readValidDueDate("Add due by time (today, week, month, year): ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false, noteProgress, collaborator, dueBy))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun listNotes(){
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > |   4) View notes by priority  |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
            4 -> listNotesBySelectedPriority();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - There are no notes");
    }
}

fun countNotes(){
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) Count ALL notes         |
                  > |   2) Count ACTIVE notes      |
                  > |   3) Count ARCHIVED notes    |
                  > |   4) Count notes by priority |
                  > |   5) Count household notes   |
                  > |   6) Count notes by category |
                  > |   7) Count notes by progress |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> countAllNotes();
            2 -> countActiveNotes();
            3 -> countArchivedNotes();
            4 -> countNotesBySelectedPriority();
            5 -> countHouseholdNotesCategory();
            6 -> countNotesByCategory();
            7 -> countNotesByProgress();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - There are no notes");
    }
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())
}
fun deleteNote(){
//    logger.info { "deleteNote() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exit(0)
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun archiveNote() {
    listAllNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun searchNotes(){
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > -------------------------------------
                  > |   1) Search notes by description  |
                  > |   2) Search notes by progress     |
                  > -------------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> searchNotesByDesc();
            2 -> searchNotesByProgress();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - There are no notes");
    }
}

fun searchNotesByDesc(){
    val searchTitle = readNextLine("Enter description to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

fun searchNotesByProgress(){
    val searchProgress = readNextLine("Enter progress to search by (to-do, doing, done): ")
    val searchResults = noteAPI.searchByProgress(searchProgress)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

fun listNotesBySelectedPriority(){
    val priorityValue = readValidPriority("Enter priority level (1-5) to search by: ")
    val searchResults = noteAPI.listNotesBySelectedPriority(priorityValue)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

fun countAllNotes() {
    println(noteAPI.numberOfNotes())
}

fun countActiveNotes() {
    println(noteAPI.numberOfActiveNotes())
}

fun countArchivedNotes() {
    println(noteAPI.numberOfArchivedNotes())
}

fun countHouseholdNotesCategory() {
    println(noteAPI.numberOfNotesHouseholdCat())
}

fun countNotesBySelectedPriority(){
    val priorityValue = readValidPriority("Enter priority level (1-5) to search by: ")
    val searchResults = noteAPI.numberOfNotesByPriority(priorityValue)
    println(searchResults)
}

fun countNotesByCategory(){
    val category = readValidCategory("Enter category to search by: ")
    val searchResults = noteAPI.numberOfNotesByCategory(category)
    println(searchResults)
}

fun countNotesByProgress() {
    val progress = readValidProgress("Enter progress level to search by: ")
    val searchResults = noteAPI.numberOfNotesByProgress(progress)
    println(searchResults)
}