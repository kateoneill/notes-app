# notes-app

## Introduction

Over the past weeks I spend time creating this notes app, i employed a github workflow as follow: Issue - branch - pull request - merge - close issue - delete branch. 
Everytime i intended to add new functionality a new issue was added.
<br><br>
A bulk of work was done in the labs, functionality to add a note, list all notes, delete notes, update a note, count all notes and more. For the assignment,
I added new fields to the note class (dueBy, collaborator and progress), added user validation when entering data, added new count functions (count notes by priority, count household category, count by category, count by progress), added new lists (list by priority, list all to-do's, list by dueBy) and added new search fields(search by progress, search by collaborator, search by first letter of collaborator name)
<br><br>
JUnit testing was of huge importance for this assignment, it was important to ensure there was 100% coverage on all noteAPI functions in the test file.
<br><br>
This app is built using Kotlin and is only my second attempt at coding in Kotlin with lots of help! 

## Requirements
I built it as a console application in IntelliJ

## Features
- Add a note
- Update a note
- Delete a note
- View all notes
- Add note to archive
- List submenu which lists:
  - all notes
  - active notes
  - archived notes
  - notes by selected priority
  - notes marked to-do
  - notes by due date
- Count submenu which counts:
  - all notes
  - archived notes
  - active notes
  - notes by selected priority
  - notes in category household
  - notes by selected category
  - notes by progress
- Search submenu which allows searching by:
  - description
  - progress
  - collaborator
  - collaborator by first letter
- Added testing for all components in NoteAPI
- Added persistence
- Added Serialization using JSON and XML


## References
A lot of this project was reconfiguring code we were already given however there were a few references I used
- To help me add colour to the console output I used: [https://discuss.kotlinlang.org/t/printing-in-colors/22492](https://discuss.kotlinlang.org/t/printing-in-colors/22492)
and to get more ANSI colour variables I used: [https://www.lihaoyi.com/post/BuildyourownCommandLinewithANSIescapecodes.html](https://discuss.kotlinlang.org/t/printing-in-colors/22492)
You can see these on lines 18-23 of main.kt
- I used an ascii text generator to create the entry screen [https://patorjk.com/software/taag/#p=display&f=Slant&t=Notes%20App](https://patorjk.com/software/taag/#p=display&f=Slant&t=Notes%20App) I used slant font.
- For the search by collaborator by first letter, https://www.tutorialkart.com/kotlin/kotlin-check-if-string-starts-with-specified-character/#:~:text=with%20Specified%20Character-,To%20check%20if%20String%20starts%20with%20specified%20character%20in%20Kotlin,the%20method%20as%20shown%20below.
<br><br>
As I mentioned, a lot of work for this assignment was done purely off lab work and reconfiguring it and the relevant testing.
