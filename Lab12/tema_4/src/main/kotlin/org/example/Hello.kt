package org.example
// Am eliminat import java.io.File deoarece nu este utilizat.

fun main() { // Am eliminat args: Array<String> deoarece nu este utilizat.
    val initialMap: MutableMap<Int, String> = mutableMapOf(
        1 to "first value example",
        2 to "another string here",
        3 to "yet another one"
    )

    println("Initial Map: $initialMap")

    val functor = MapFunctor(initialMap)

    // Primul map: adaugă prefixul "Test" la fiecare valoare
    val functorAfterFirstMap = functor.map { value -> "Test$value" }
    println("After first map: ${functorAfterFirstMap.getMap()}")

    // Al doilea map: apelează funcția extensie toPascalCase
    val functorAfterSecondMap = functorAfterFirstMap.map { value -> value.toPascalCase() }
    println("After second map: ${functorAfterSecondMap.getMap()}")
}
