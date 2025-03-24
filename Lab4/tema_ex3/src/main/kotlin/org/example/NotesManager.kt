class NotesManager(private val storage: NoteStorage) {
    private val notes = mutableListOf<Note>().apply { addAll(storage.loadAll()) }

    fun createNote(user: User, content: String, title: String? = null): Note {
        val note = Note(user, content, title = title.toString())
        storage.save(note)
        notes.add(note)
        return note
    }

    fun deleteNote(title: String): Boolean {
        return notes.find { it.title == title }?.let {
            storage.delete(title)
            notes.remove(it)
            true
        } ?: false
    }

    fun listNotes() = notes.map { it.title }
    fun loadNote(title: String) = notes.find { it.title == title }
}