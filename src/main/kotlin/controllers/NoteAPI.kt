package controllers

import models.Note
import persistence.Serializer
import utils.Utilities.isValidListIndex

class NoteAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var notes = ArrayList<Note>()

    private fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

    // add delete and update a note
    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            foundNote.noteProgress = note.noteProgress
            foundNote.collaborator = note.collaborator
            foundNote.dueBy = note.dueBy
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    //archive a note
    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }
        return false
    }

    //list all notes, count notes, find a note by index
    fun listAllNotes(): String =
        if  (notes.isEmpty()) "No notes stored"
        else formatListString(notes)

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        }else null
    }

    //list active, archived notes
    fun listActiveNotes(): String =
        if  (numberOfActiveNotes() == 0)  "No active notes stored"
        else formatListString(notes.filter { note -> !note.isNoteArchived})

    fun listArchivedNotes(): String =
        if  (numberOfArchivedNotes() == 0) "No archived notes stored"
        else formatListString(notes.filter { note -> note.isNoteArchived})

    //list notes by searched for priority
    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty()) "No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.notePriority == priority})
            if (listOfNotes.equals("")) "No notes with priority: $priority"
            else "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
        }

    //list notes by progress point
    fun listNotesByProgress(): String =
    if  (numberOfNotes() == 0)  "No notes stored"
    else formatListString(notes.filter { note -> note.noteProgress == "to-do"})

    //list notes by due date
    fun listNotesByDueDate(dueDate: String): String =
        if (notes.isEmpty()) "No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.dueBy == dueDate})
            if (listOfNotes.equals("")) "No notes with due by end of the $dueDate"
            else "$dueDate: $listOfNotes"
        }

//count archived, active notes. count notes in household category.
    fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }


    fun numberOfActiveNotes(): Int = notes.count { note: Note -> !note.isNoteArchived }

    fun numberOfNotesHouseholdCat(): Int = notes.count { note: Note -> note.noteCategory == "Household"  }

//count notes in searched for priority
    fun numberOfNotesByPriority(priority: Int): Int = notes.count {
        //helper method to determine how many notes there are of a specific priority
        note: Note -> note.notePriority == priority
    }

    //count notes by searched for category
    fun numberOfNotesByCategory(category: String): Int = notes.count {
        //helper method to determine how many notes there are of a specific priority
            note: Note -> note.noteCategory == category
    }

    //count notes by progress point
    fun numberOfNotesByProgress(progress: String): Int = notes.count{
        note: Note -> note.noteProgress == progress
    }

//search for note by title
    fun searchByTitle (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteTitle.contains(searchString, ignoreCase = true) })

    //search for notes by progress point
    fun searchByProgress (searchString : String) =
        formatListString(
            notes.filter { note -> note.noteProgress.equals(searchString, ignoreCase = true) })

    //search for note by collaborator
    fun searchByCollaborator(searchString: String) =
        formatListString(
            notes.filter { note -> note.collaborator.contains(searchString, ignoreCase = true)}
        )

    //search for notes by first letter of collaborator name
    fun searchByCollaboratorFirstL(searchString: String) =
        formatListString(
            notes.filter{ note -> note.collaborator.startsWith(searchString, ignoreCase = true)}
        )

    //check if it's a valid index
    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }

    //load and save notes to serializer
    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }
}


