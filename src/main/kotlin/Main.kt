import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
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
          > |   6) Save notes                |
          > |   7) Load notes                |
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
            6 -> save()
            7 -> load()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    }while (true)
}

fun addNote(){
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))){
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
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllNotes();
            2 -> listActiveNotes();
            3 -> listArchivedNotes();
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
    val searchTitle = readNextLine("Enter description to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if(searchResults.isEmpty()){
        println("No Notes found")
    } else {
        println(searchResults)
    }
}

