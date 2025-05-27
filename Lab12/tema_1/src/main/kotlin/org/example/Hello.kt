package org.example

fun main(args: Array<String>) {
    println("Hello, World")

    val lista = listOf(1, 21, 75, 39, 7, 2, 35, 3, 31, 7, 8)

    // 1. Eliminarea oricărui număr < 5
    val listaFiltrata = lista.filter { it >= 5 }
    println("Lista filtrată: $listaFiltrata")

    // 2. Gruparea numerelor în perechi
    val perechi = listaFiltrata.windowed(2, 2, partialWindows = false)
        .map { Pair(it[0], it[1]) }
    println("Perechi: $perechi")

    // 3. Multiplicarea numerelor din perechi
    val produse = perechi.map { it.first * it.second }
    println("Produse: $produse")

    // 4. Sumarea rezultatelor
    val suma = produse.sum()
    println("Suma: $suma")
}
