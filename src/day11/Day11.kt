package day11

import utils.readText

fun main() {
    val input = readText("day11/input").split(" ").map { it.toLong() }

    val p1 = input.sumOf { processNumber(it, 0, 25) }
    println(p1)

    cache.clear()

    val p2 = input.sumOf { processNumber(it, 0, 75) }
    println(p2)
}

val cache = mutableMapOf<Pair<Long, Int>, Long>()

fun processNumber(number: Long, i: Int, maxDepth: Int): Long {
    return if (i == maxDepth) {
        1
    } else {
        applyRules(number).sumOf {
            cache.getOrPut(Pair(it, i + 1)) {
                processNumber(it, i + 1, maxDepth)
            }
        }
    }
}

fun applyRules(number: Long): List<Long> = if (number == 0L) {
    listOf(1)
} else if (number.toString().length % 2 == 0) {
    val x = number.toString()
    listOf(x.substring(0, x.length / 2).toLong(), x.substring(x.length / 2, x.length).toLong())
} else {
    listOf(number * 2024)
}