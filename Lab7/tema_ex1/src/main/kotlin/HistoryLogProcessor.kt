import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.io.File

class HistoryLogRecord(
    val timestamp: Long,
    val command: String,
    val startDate: String
) : Comparable<HistoryLogRecord> {

    override fun compareTo(other: HistoryLogRecord): Int {
        return this.timestamp.compareTo(other.timestamp)
    }

    override fun toString(): String {
        return "HistoryLogRecord(timestamp=$timestamp, command='$command', startDate='$startDate')"
    }
}

fun <T : Comparable<T>> findMax(a: T, b: T): T {
    return if (a.compareTo(b) > 0) a else b
}

fun findHistoryLogFiles(directory: File): List<File> {
    val historyLogs = mutableListOf<File>()
    
    fun scanDir(dir: File) {
        dir.listFiles()?.forEach { file ->
            when {
                file.isDirectory -> scanDir(file)
                file.name == "history.log" || file.name == "history.log.10" || file.name == "history.log.1" -> historyLogs.add(file)
            }
        }
    }
    
    scanDir(directory)
    return historyLogs
}

fun parseHistoryLog(file: File): MutableMap<Long, HistoryLogRecord> {
    val result = mutableMapOf<Long, HistoryLogRecord>()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")
    
    try {
        val lines = file.readLines().takeLast(50) // citim ultimele 50 de linii
        var startDate: String? = null
        var command: String? = null
        val otherMetadata = mutableMapOf<String, String>()

        for (line in lines) {
            when {
                line.startsWith("Start-Date: ") -> {
                    startDate = line.substringAfter("Start-Date: ").trim()
                }
                line.startsWith("Commandline: ") -> {
                    command = line.substringAfter("Commandline: ").trim()
                }
                line.startsWith("End-Date: ") -> { // daca linia incepe cu End-Date:
                    // verificam daca am datele necesare pentru a crea un obiect HistoryLogRecord
                    // daca am startDate si command, putem crea un obiect HistoryLogRecord     
                    if (startDate != null && command != null) {
                        // convertim data in timestamp
                        val timestamp = try {
                            val parsedDate = LocalDateTime.parse(startDate, formatter)
                            parsedDate.toEpochSecond(java.time.ZoneOffset.UTC)
                        } catch (e: DateTimeParseException) {
                            0L
                        }
                        result[timestamp] = HistoryLogRecord(
                            timestamp = timestamp,
                            command = command,
                            startDate = startDate
                        )
                    }
                    startDate = null
                    command = null
                    otherMetadata.clear()
                }
                else -> {   
                    // daca linia nu incepe cu Start-Date:, Commandline: sau End-Date:
                    // adaugam datele in otherMetadata
                    if (line.contains(":")) {
                        val parts = line.split(":", limit = 2)
                        if (parts.size == 2) {
                            otherMetadata[parts[0].trim()] = parts[1].trim()
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        File("error.log").appendText("Error processing log file ${file.path}: ${e.message}\n")
    }
    
    return result
}

fun main() {
    val outputFile = File("output.txt")
    val searchDirectory = File("/var/log/apt")
    val historyLogs = findHistoryLogFiles(searchDirectory)
    
    if (historyLogs.isEmpty()) {
        outputFile.writeText("No history.log files found in ${searchDirectory.path}\n")
        return
    }
    // afisam numarul de fisiere history.log gasite
    var output = "Found ${historyLogs.size} history.log files:\n"
    // afisam numele fisierelor
    historyLogs.forEach { output += "${it.path}\n" }
    
    val allRecords = mutableMapOf<Long, HistoryLogRecord>()
    
    historyLogs.forEach { logFile ->
        output += "\nProcessing ${logFile.path}:\n"
        val records = parseHistoryLog(logFile)
        allRecords.putAll(records)
    }
    
    if (allRecords.isNotEmpty()) {
        // Găsim cea mai recentă înregistrare folosind findMax
        var maxRecord = allRecords.values.first()
        allRecords.values.forEach { record ->
            maxRecord = findMax(maxRecord, record)
        }
        output += "\nMost recent record across all files: $maxRecord\n"
        
        // Afișăm toate înregistrările
        output += "\nAll records from all files:\n"
        allRecords.values.forEach { output += "$it\n" }
    }
    
    outputFile.writeText(output)
    println("Output written to ${outputFile.absolutePath}")
} 