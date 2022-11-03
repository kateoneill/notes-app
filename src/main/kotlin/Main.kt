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
    logger.info { "🚀Launching models.notes App 🚀" }
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

//add note
fun addNote(){
    logger.info { "You are adding a note 📝" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readValidCategory("Enter a category for the note: ")
    val noteProgress = readValidProgress("Enter a progress (To-do, doing, done): ")
    val collaborator = readNextLine("Enter a note collaborator: ")
    val dueBy = readValidDueDate("Add due by time (day, week, month, year): ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteProgress, collaborator,dueBy))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

//update note
fun updateNote() {
    logger.info { "You are updating a note ✏️" }
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
            val dueBy = readValidDueDate("Add due by time (day, week, month, year): ")

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

//delete note
fun deleteNote(){
    logger.info { "You are deleting a note 🗑" }
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

//list notes submenu
fun listNotes(){
    logger.info { "Listing notes 🔢" }
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > |   4) View notes by priority  |
                  > |   5) View all to-do notes    |
                  > |   6) View notes by due date  |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
            4 -> listNotesBySelectedPriority();
            5 -> groupByProgressTodo();
            6 -> listNotesByDueDate();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - There are no notes");
    }
}

//list all notes
fun listAllNotes() {
    logger.info { "Listing all notes 📒" }
    println(noteAPI.listAllNotes())
}

//list active notes
fun listActiveNotes() {
    logger.info { "Listing active notes 🏋️‍️" }
    println(noteAPI.listActiveNotes())
}

//list archived notes
fun listArchivedNotes() {
    logger.info { "Listing archived notes 🏛"}
    println(noteAPI.listArchivedNotes())
}

//list notes marked as to-do
fun groupByProgressTodo() {
    logger.info{ "Listing to-do's 🤔"}
    println(noteAPI.listNotesByProgress())
}

//list notes by priority
fun listNotesBySelectedPriority(){
    logger.info{ "Listing by selected priority 🎯"}
    val priorityValue = readValidPriority("Enter priority level (1-5) to search by: ")
    val searchResults = noteAPI.listNotesBySelectedPriority(priorityValue)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

//list notes by due date
fun listNotesByDueDate(){
    logger.info{ "Listing by due date 📆"}
    val dueDate = readValidDueDate("Enter due date (day, week, month, year) to search by: ")
    val searchResults = noteAPI.listNotesByDueDate(dueDate)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

//exit app
fun exitApp(){
    logger.info{ "Bye bye, come back soon 👋"}
    logger.info { "exitApp() function invoked" }
    exit(0)
}

// save notes
fun save() {
    logger.info { "saving notes ⤵️"}
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

//load notes
fun load() {
    logger.info {"loading notes ↗️"}
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

//archive a note
fun archiveNote() {
    logger.info {"archive notes 🗂"}
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

//search notes submenu
fun searchNotes(){
    logger.info{"Search for notes 🔎"}
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > -------------------------------------------------
                  > |   1) Search notes by description               |
                  > |   2) Search notes by progress                  |
                  > |   3) Search notes by collaborator              |
                  > |   4) Search notes by collaborator first letter |
                  > -------------------------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> searchNotesByDesc();
            2 -> searchNotesByProgress();
            3 -> searchNotesByCollaborator();
            4 -> searchNotesByCollabLetter();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - There are no notes");
    }
}

//search notes by their description
fun searchNotesByDesc(){
    logger.info{"search by title 🔎"}
    val searchTitle = readNextLine("Enter description to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

//search notes by progress
fun searchNotesByProgress(){
    logger.info{"search by progress ✅"}
    val searchProgress = readNextLine("Enter progress to search by (to-do, doing, done): ")
    val searchResults = noteAPI.searchByProgress(searchProgress)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

//search notes by collaborator
fun searchNotesByCollaborator(){
    logger.info{"search by collaborator 👥"}
    val searchCollaborator = readNextLine("Enter collaborator to search by: ")
    val searchResults = noteAPI.searchByCollaborator(searchCollaborator)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

// search notes by first letter of collaborator's name
fun searchNotesByCollabLetter(){
    logger.info{"search by collaborator, first letter 👥"}
    val searchCollaborator = readNextLine("Enter collaborator to search by: ")
    val searchResults = noteAPI.searchByCollaboratorFirstL(searchCollaborator)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

//count notes
fun countNotes(){
    logger.info{"counting employees menu 🔢"}
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

//count all notes
fun countAllNotes() {
    logger.info{"Count all notes 📒"}
    println(noteAPI.numberOfNotes())
}

//count active notes
fun countActiveNotes() {
    logger.info{"count active notes 🏋️‍️"}
    println(noteAPI.numberOfActiveNotes())
}

//count archived notes
fun countArchivedNotes() {
    logger.info{"count archived notes 🏛"}
    println(noteAPI.numberOfArchivedNotes())
}

//count notes in household category
fun countHouseholdNotesCategory() {
    logger.info{"count notes in household category 🏠"}
    println(noteAPI.numberOfNotesHouseholdCat())
}

//count notes by priority
fun countNotesBySelectedPriority(){
    logger.info{"count notes by selected priority 🎯"}
    val priorityValue = readValidPriority("Enter priority level (1-5) to search by: ")
    val searchResults = noteAPI.numberOfNotesByPriority(priorityValue)
    println(searchResults)
}

//count notes by category
fun countNotesByCategory(){
    logger.info{"count notes by selected category 📂"}
    val category = readValidCategory("Enter category to search by: ")
    val searchResults = noteAPI.numberOfNotesByCategory(category)
    println(searchResults)
}

//count notes by progress
fun countNotesByProgress() {
    logger.info{"count notes by progress ✅"}
    val progress = readValidProgress("Enter progress level to search by: ")
    val searchResults = noteAPI.numberOfNotesByProgress(progress)
    println(searchResults)
}