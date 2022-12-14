package controllers

import models.Note
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals


class NoteAPITest {
    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var hoover: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))

    @BeforeEach
    fun setup(){
        learnKotlin = Note("Learning Kotlin", 5, "College", false, "to-do","nobody","month")
        summerHoliday = Note("Summer Holiday to France", 1, "Recreational", false, "to-do","sally","year")
        codeApp = Note("Code App", 4, "Work", true, "doing","Maria", "week")
        testApp = Note("Test App", 4, "Work", false, "doing", "Maria", "month")
        swim = Note("Swim - Pool", 3, "Fitness", true,"to-do", "Sally", "year")
        hoover = Note("Hoovering", 3, "Household", false, "to-do", "Leah", "year")

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
        populatedNotes!!.add(hoover!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false,"doing","Nobody", "week")
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(7, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false,"doing","Nobody", "week")
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }

        @Test
        fun `listActiveNotes returns no active notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
            assertTrue(
                emptyNotes!!.listActiveNotes().lowercase().contains("no active notes")
            )
        }

        @Test
        fun `listActiveNotes returns active notes when ArrayList has active notes stored`() {
            assertEquals(4, populatedNotes!!.numberOfActiveNotes())
            val activeNotesString = populatedNotes!!.listActiveNotes().lowercase()
            assertTrue(activeNotesString.contains("learning kotlin"))
            assertFalse(activeNotesString.contains("code app"))
            assertTrue(activeNotesString.contains("summer holiday"))
            assertTrue(activeNotesString.contains("test app"))
            assertFalse(activeNotesString.contains("swim"))
        }

        @Test
        fun `listArchivedNotes returns no archived notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
            assertTrue(
                emptyNotes!!.listArchivedNotes().lowercase().contains("no archived notes")
            )
        }

        @Test
        fun `listArchivedNotes returns archived notes when ArrayList has archived notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            val archivedNotesString = populatedNotes!!.listArchivedNotes().lowercase()
            assertFalse(archivedNotesString.contains("learning kotlin"))
            assertTrue(archivedNotesString.contains("code app"))
            assertFalse(archivedNotesString.contains("summer holiday"))
            assertFalse(archivedNotesString.contains("test app"))
            assertTrue(archivedNotesString.contains("swim"))
        }

        @Test
        fun `listNotesBySelectedPriority returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val priority2String = populatedNotes!!.listNotesBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no notes"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listNotesBySelectedPriority returns all notes that match that priority when notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val priority1String = populatedNotes!!.listNotesBySelectedPriority(1).lowercase()
            assertTrue(priority1String.contains("1 note"))
            assertTrue(priority1String.contains("priority 1"))
            assertTrue(priority1String.contains("summer holiday"))
            assertFalse(priority1String.contains("swim"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("test app"))


            val priority4String = populatedNotes!!.listNotesBySelectedPriority(4).lowercase()
            assertTrue(priority4String.contains("2 note"))
            assertTrue(priority4String.contains("priority 4"))
            assertFalse(priority4String.contains("swim"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("summer holiday"))
        }

        @Test
        fun `listNotesYetToBeDone returns all notes when marked to-do`() {
            assertEquals(6, populatedNotes!!.numberOfNotes())

            val toDoNotesString = populatedNotes!!.listNotesByProgress().lowercase()
            assertTrue(toDoNotesString.contains("learning kotlin"))
            assertFalse(toDoNotesString.contains("test app"))
        }

        @Test
        fun `listNotesByDueDate returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesByDueDate("day").lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesByDueDate returns no notes when no notes due then exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val dayString = populatedNotes!!.listNotesByDueDate("day").lowercase()
            assertTrue(dayString.contains("no notes"))
            assertTrue(dayString.contains("day"))
        }

        @Test
        fun `listNotesByDueDate returns all notes that match that due date when notes due then exist`() {
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val weekString = populatedNotes!!.listNotesByDueDate("week").lowercase()
            assertTrue(weekString.contains("code app"))
            assertFalse(weekString.contains("test app"))
            assertFalse(weekString.contains("learning kotlin"))
            assertFalse(weekString.contains("summer holiday"))
            assertFalse(weekString.contains("hoovering"))
            assertFalse(weekString.contains("swim - pool"))


            val monthString = populatedNotes!!.listNotesByDueDate("month").lowercase()
            assertFalse(monthString.contains("summer holiday to france"))
            assertTrue(monthString.contains("learning kotlin"))
            assertTrue(monthString.contains("test app"))
            assertFalse(monthString.contains("code app"))
            assertFalse(monthString.contains("hoovering"))
            assertFalse(monthString.contains("swim - pool"))
        }
    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(6))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(4, populatedNotes!!.numberOfNotes())
        }
    }

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`(){
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", false,"doing","Molly", "month")))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "Work", false,"doing","Molly", "month")))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", false,"doing","Molly", "month")))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            //check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Fitness", populatedNotes!!.findNote(4)!!.noteCategory)

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", false, "doing","nobody","week")))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            //Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            //Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    @Nested
    inner class ArchiveNotes {
        @Test
        fun `archiving notes that do not exist to make sure it's false`(){
            assertFalse(populatedNotes!!.archiveNote(6))
            assertFalse(populatedNotes!!.archiveNote(-1))
            assertFalse(emptyNotes!!.archiveNote(0))
        }

        @Test
        fun `archiving an already archived note to make sure it's false`(){
            assertTrue(populatedNotes!!.findNote(2)!!.isNoteArchived)
            assertFalse(populatedNotes!!.archiveNote(2))
        }

        @Test
        fun `archiving an active note that exists`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteArchived)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(4, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(1))
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority(2))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(3))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(5))
            assertEquals(0, emptyNotes!!.numberOfNotesByPriority(1))
        }

        @Test
        fun numberOfNotesInHouseholdCatCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesHouseholdCat())
            assertEquals(0, emptyNotes!!.numberOfNotesHouseholdCat())
        }

        @Test
        fun numberOfNotesByCategoryCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfNotesByCategory("Work"))
            assertEquals(1, populatedNotes!!.numberOfNotesByCategory("College"))
            assertEquals(1, populatedNotes!!.numberOfNotesByCategory("Household"))
            assertEquals(1, populatedNotes!!.numberOfNotesByCategory("Fitness"))
            assertEquals(1, populatedNotes!!.numberOfNotesByCategory("Recreational"))
            assertEquals(0, emptyNotes!!.numberOfNotesByCategory("Wellbeing"))
        }

        @Test
        fun numberOfNotesByProgressCalculatedCorrectly() {
            assertEquals(4, populatedNotes!!.numberOfNotesByProgress("to-do"))
            assertEquals(2, populatedNotes!!.numberOfNotesByProgress("doing"))
            assertEquals(0, populatedNotes!!.numberOfNotesByProgress("done"))
        }
    }

    @Nested
    inner class SearchMethods {
        @Test
        fun `search notes by title returns when no notes with that title exist`(){
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by title returns notes when notes with that title exist`(){
            assertEquals(6, populatedNotes!!.numberOfNotes())

            var searchResults = populatedNotes!!.searchByTitle("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            searchResults = populatedNotes!!.searchByTitle("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))

            searchResults = populatedNotes!!.searchByTitle("aPp")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }

        @Test
        fun `search notes by progress returns when no notes with that progress exist`(){
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByProgress("no results expected")
            assertTrue(searchResults.isEmpty())

            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByProgress("").isEmpty())
        }

        @Test
        fun `search notes by progress returns notes when notes with that progress exist`(){
            assertEquals(6, populatedNotes!!.numberOfNotes())

            var searchResults = populatedNotes!!.searchByProgress("to-do")
            assertTrue(searchResults.contains("to-do"))
            assertFalse(searchResults.contains("doing"))

            searchResults = populatedNotes!!.searchByProgress("do")
            assertFalse(searchResults.contains("to-do"))
            assertFalse(searchResults.contains("doing"))
            assertFalse(searchResults.contains("done"))

        }

        @Test
        fun `search notes by collaborator returns when no notes with that collaborator exist`(){
            assertEquals(6, populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByTitle("Kate")
            assertTrue(searchResults.isEmpty())

            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by collaborator returns notes when notes with that collaborator exist`(){
            assertEquals(6, populatedNotes!!.numberOfNotes())

            var searchResults = populatedNotes!!.searchByCollaborator("Maria")
            assertTrue(searchResults.contains("Maria"))
            assertFalse(searchResults.contains("sally"))

            searchResults = populatedNotes!!.searchByCollaborator("Sal")
            assertTrue(searchResults.contains("Sally"))
            assertFalse(searchResults.contains("Leah"))

            searchResults = populatedNotes!!.searchByCollaborator("leAh")
            assertTrue(searchResults.contains("Leah"))
            assertFalse(searchResults.contains("Maria"))
        }

        @Test
        fun `search notes by collaborator first letter returns notes when notes with that collaborator exist`(){
            assertEquals(6, populatedNotes!!.numberOfNotes())

            var searchResults = populatedNotes!!.searchByCollaboratorFirstL("M")
            assertTrue(searchResults.contains("Maria"))
            assertFalse(searchResults.contains("sally"))

            searchResults = populatedNotes!!.searchByCollaboratorFirstL("L")
            assertTrue(searchResults.contains("Leah"))
            assertFalse(searchResults.contains("Sally"))
        }
    }

}