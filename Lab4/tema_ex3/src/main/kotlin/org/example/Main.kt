import kotlinx.serialization.Serializable

fun main() {
    try {
        val storage = FileNoteStorage("notes_storage")
        val manager = NotesManager(storage)
        CLI(manager).run()
    } catch (e: Exception) {
        println("Fatal error: ${e.message}")
        e.printStackTrace()
    }
}