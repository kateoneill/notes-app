package models

data class Note (
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var isNoteArchived: Boolean,
    var noteProgress: String,
    var collaborator: String,
    var dueBy: String
    ){
}