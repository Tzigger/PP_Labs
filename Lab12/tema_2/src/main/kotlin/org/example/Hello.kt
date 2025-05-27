package org.example

object Hello {
    @JvmStatic
    fun main(args: Array<String>) {
        fun caesarCipher(word: String, offset: Int): String =
            word.map {
                when {
                    it.isLetter() -> {
                        val base = if (it.isUpperCase()) 'A' else 'a'
                        ((it - base + offset) % 26 + base.code).toChar()
                    }
                    else -> it
                }
            }.joinToString("")

        val offset = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 3 else 3 // default offset 3 dacă nu se dă argument
        val fileName = if (args.size > 1) args[1] else "input.txt"

        val text = try {
            java.io.File(fileName).readText()
        } catch (e: Exception) {
            println("Eroare la citirea fișierului: ${e.message}")
            return
        }

        val rezultat = text.split(" ", "\n", "\r", "\t", ",", ".", ";", ":", "!", "?", "-", "(", ")", "[", "]", "{", "}")
            .filter { it.isNotBlank() }
            .map { cuv ->
                if (cuv.length in 4..7)
                    caesarCipher(cuv, offset)
                else
                    cuv
            }

        // Reconstruim textul criptat păstrând separatoarele originale
        val pattern = Regex("""([ \n\r\t,.;:!\?\-()\[\]{}])""")
        val parts = pattern.split(text)
        val seps = pattern.findAll(text).map { it.value }.toList()
        val sb = StringBuilder()
        for (i in parts.indices) {
            if (i < rezultat.size)
                sb.append(rezultat[i])
            else if (i < parts.size)
                sb.append(parts[i])
            if (i < seps.size)
                sb.append(seps[i])
        }
        println("Text criptat:\n$sb")
    }
}

