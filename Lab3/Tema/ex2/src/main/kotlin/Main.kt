import java.io.File

fun main() {

    val inputFile = File("input.txt")
    val outputFile = File("output.txt")

    val text = inputFile.readText()
    val processedText = processText(text)

    outputFile.writeText(processedText)
}

fun processText(text: String): String {
    var processed = text

    processed = eliminateMultipleSpaces(processed);
    processed = eliminateNewLines(processed);
    processed = eliminatePageNumbers(processed);
    processed = eliminateAuthor(processed);
    processed = eliminateChapters(processed);



    return processed.trim()
}

fun eliminateMultipleSpaces(text: String): String {
    var processed = text;
    processed = processed.replace(Regex(" +"), " ")
    return processed;
}

fun eliminateNewLines(text: String): String {
    var processed = text;
    processed = processed.replace(Regex("(\\r?\\n)+"), "\n")
    return processed;
}


fun eliminatePageNumbers(text: String): String {
    var processed = text;
    processed = processed.replace(Regex("""(?m)^\s*\d+\s*${'$'}"""), "")         // Linii întregi cu număr
    processed = processed.replace(Regex("""\s{2,}\d+\s{2,}"""), " ")            // Numere în interiorul textului
    return  processed;
}

fun eliminateAuthor(text: String): String {
    var processed = text;
    processed = processed.replace(Regex("""(?i)Autor:\s*[\p{L}\s]+\s*"""), " ") // Nume autor
 return processed;
}

fun eliminateChapters(text: String): String {
    var processed = text;
    processed = processed.replace(Regex("""(?m)^\s*Capitolul\s+[IVXLCDM0-9]+:\s+.*${'$'}"""), "")   // Titluri capitole
    return processed;
}

