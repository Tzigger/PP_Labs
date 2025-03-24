import java.util.*

class CLI(private val manager: NotesManager) {
    fun run() {
        val scanner = Scanner(System.`in`)
        while (true) {
            printMenu()
            when (scanner.nextLine().trim()) {
                "1" -> listNotes()
                "2" -> loadNote(scanner)
                "3" -> createNote(scanner)
                "4" -> deleteNote(scanner)
                "5" -> return
                else -> println("Invalid option")
            }
        }
    }

    private fun printMenu() {
        println(
            """
                
            1. List notes
            2. Load note
            3. Create note
            4. Delete note
            5. Exit
            Choice: 
            """.trimIndent()
        )
    }

    private fun listNotes() {
        manager.listNotes().takeIf { it.isNotEmpty() }?.forEachIndexed { i, title ->
            println("${i + 1}. $title")
        } ?: println("No notes available")
    }

    private fun loadNote(scanner: Scanner) {
        print("Enter title: ")
        manager.loadNote(scanner.nextLine().trim())?.let {
            println("\nTitle: ${it.title}")
            println("Author: ${it.author.username}")
            println("Date: ${it.creationDate}")
            println("Content:\n${it.content}\n")
        } ?: println("Note not found")
    }

    private fun createNote(scanner: Scanner) {
        print("Username: ")
        val user = User(scanner.nextLine().trim())
        print("Content: ")
        val content = scanner.nextLine().trim()
        print("Title (optional): ")
        val titleInput = scanner.nextLine().trim()
        val title = if (titleInput.isEmpty()) null else titleInput
        println("Created note: ${manager.createNote(user, content, title).title}")
    }

    private fun deleteNote(scanner: Scanner) {
        print("Title to delete: ")
        println(if (manager.deleteNote(scanner.nextLine().trim())) "Deleted" else "Not found")
    }
}