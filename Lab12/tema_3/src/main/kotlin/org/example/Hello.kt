package org.example
import java.io.File

fun perimeter(points: List<Pair<Int, Int>>): Double {
    val sideLengths = points.zipWithNext { a, b ->
        distance(a, b)
    }
    val closingSide = distance(points.first(), points.last())
    return sideLengths.sum() + closingSide
}

fun distance(a: Pair<Int, Int>, b: Pair<Int, Int>): Double =
    Math.hypot((a.first - b.first).toDouble(), (a.second - b.second).toDouble())

fun main(args: Array<String>) {
    val lines = File("input.txt").readLines().filter { it.isNotBlank() }
    val n = lines.first().toInt()
    val points = lines.drop(1).take(n).map {
        val (x, y) = it.split(" ").map { v -> v.toInt() }
        x to y
    }
    println(perimeter(points))
}
