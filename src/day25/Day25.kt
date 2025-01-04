package day25

import utils.readInput

fun main() {
    val (locks, keys) = readInput("day25/input").chunked(8).map { c ->
        val combo = (0..<5).map { i -> c.dropLast(1).map { it[i] }.count { it == '#' } - 1 }
        Pair(if (c.first().startsWith("#")) "l" else "k", combo)
    }.partition { it.first == "l" }

    println(locks.fold(0) { a, l -> a + keys.count { k -> (0..4).all { i -> l.second[i] + k.second[i] < 6 } } })
}