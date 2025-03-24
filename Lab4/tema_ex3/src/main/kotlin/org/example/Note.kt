import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
class Note(
    val author: User,
    val content: String,
    val creationDate: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    val title: String = "Note_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))}"
)