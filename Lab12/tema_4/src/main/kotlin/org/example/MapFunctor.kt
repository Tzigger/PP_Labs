package org.example

/**
 * Functor pentru o colecție de tip MutableMap care prelucrează valorile.
 */
class MapFunctor<K, V>(private val mutableMap: MutableMap<K, V>) {

    /**
     * Aplică o funcție de transformare asupra valorilor din map.
     * @param transform Funcția care transformă o valoare de tip V într-o valoare de tip R.
     * @return Un nou MapFunctor cu cheile originale și valorile transformate.
     */
    fun <R> map(transform: (V) -> R): MapFunctor<K, R> {
        val newMap = mutableMap.mapValues { entry -> transform(entry.value) }.toMutableMap()
        return MapFunctor(newMap)
    }

    /**
     * Returnează MutableMap-ul intern.
     */
    fun getMap(): MutableMap<K, V> = mutableMap

    override fun toString(): String {
        return "MapFunctor(map=$mutableMap)"
    }
}

/**
 * Funcție extensie pentru String care convertește un text la formatul PascalCase.
 * Cuvintele sunt separate prin spațiu și fiecare cuvânt va începe cu majusculă.
 * Prefixul "Test" adăugat anterior va fi tratat corespunzător.
 * Exemplu: "Testfirst value" -> "TestfirstValue"
 * Exemplu: "Test another example" -> "TestAnotherExample"
 */
fun String.toPascalCase(): String {
    val words = this.split(' ')
    if (words.isEmpty()) return ""

    return words.mapIndexed { index, word ->
        // Pentru primul cuvânt (care poate conține "Test"), capitalizăm prima literă dacă este minusculă.
        // Pentru celelalte cuvinte, le convertim la litere mici și apoi capitalizăm prima literă.
        if (index == 0) {
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        } else {
            word.lowercase().replaceFirstChar { it.titlecase() }
        }
    }.joinToString("")
}
