import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

interface NoteStorage {
    fun save(note: Note)
    fun delete(title: String)
    fun loadAll(): List<Note>
    fun load(title: String): Note?
}

class FileNoteStorage(private val storageDirectory: String) : NoteStorage {
    init {
        File(storageDirectory).mkdirs()
    }

    private fun getFile(title: String) = File("$storageDirectory${File.separator}$title.json")

    override fun save(note: Note) {
        getFile(note.title).writeText(Json.encodeToString(note))
    }

    override fun delete(title: String) {
        getFile(title).delete()
    }

    override fun loadAll(): List<Note> {
        return File(storageDirectory).listFiles { file -> file.extension == "json" }
            ?.mapNotNull { load(it.nameWithoutExtension) }
            ?: emptyList()
    }

    override fun load(title: String): Note? {
        return try {
            Json.decodeFromString<Note>(getFile(title).readText())
        } catch (e: Exception) {
            null
        }
    }
}