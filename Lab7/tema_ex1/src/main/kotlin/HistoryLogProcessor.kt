import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.io.File

class HistoryLogRecord(
    val timestamp: Long,
    val command: String,
    val startDate: String,
    val otherMetadata: Map<String, String> = emptyMap()
) : Comparable<HistoryLogRecord> {


    override fun compareTo(other: HistoryLogRecord): Int {
        return this.timestamp.compareTo(other.timestamp)
    }

    override fun toString(): String {
        return "HistoryLogRecord(timestamp=$timestamp, command='$command', startDate='$startDate')"
    }
}

fun <T : Comparable<T>> findMax(a: T, b: T): T {
    return if (a > b) a else b
}

fun <T> replaceInMap(
    search: T,
    replace: T,
    map: MutableMap<Long, out T>
) where T : HistoryLogRecord {
    val entry = map.entries.find { it.value == search }
    if (entry != null) {
        (map as MutableMap<Long, T>)[entry.key] = replace
    }
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
        val lines = file.readLines().takeLast(50)
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
                line.startsWith("End-Date: ") -> {
                    if (startDate != null && command != null) {
                        val timestamp = try {
                            val parsedDate = LocalDateTime.parse(startDate, formatter)
                            parsedDate.toEpochSecond(java.time.ZoneOffset.UTC)
                        } catch (e: DateTimeParseException) {
                            0L
                        }
                        result[timestamp] = HistoryLogRecord(
                            timestamp = timestamp,
                            command = command,
                            startDate = startDate,
                            otherMetadata = otherMetadata.toMap()
                        )
                    }
                    startDate = null
                    command = null
                    otherMetadata.clear()
                }
                else -> {
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
    
    var output = "Found ${historyLogs.size} history.log files:\n"
    historyLogs.forEach { output += "${it.path}\n" }
    
    val allRecords = mutableMapOf<Long, HistoryLogRecord>()
    
    historyLogs.forEach { logFile ->
        output += "\nProcessing ${logFile.path}:\n"
        val records = parseHistoryLog(logFile)
        allRecords.putAll(records)
    }
    
    if (allRecords.isNotEmpty()) {
        val firstRecord = allRecords.values.first()
        val lastRecord = allRecords.values.last()
        val maxRecord = findMax(firstRecord, lastRecord)
        output += "\nMost recent record across all files: $maxRecord\n"
    }
    
    output += "\nAll records from all files (sorted by timestamp):\n"
    allRecords.values.sortedBy { it.timestamp }.forEach { output += "$it\n" }
    
    outputFile.writeText(output)
    println("Output written to ${outputFile.absolutePath}")
} 