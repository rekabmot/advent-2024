package day19

import utils.readInput

val cache = mutableMapOf<String, Long>()

fun main() {
    val input = readInput("day19/input")
    val towels = input.first().split(", ")
    val patterns = input.drop(2)

    println(patterns.filter { canMakePattern(it, towels) }.size)
    println(patterns.fold(0L) { acc, next -> acc + countPossibilities(next, towels) })
}

fun canMakePattern(pattern: String, towels: List<String>): Boolean =
    pattern.isEmpty() || towels.any { pattern.startsWith(it) && canMakePattern(pattern.drop(it.length), towels) }

fun countPossibilities(pattern: String, towels: List<String>): Long {
    if (pattern.isEmpty()) return 1

    return towels.filter { pattern.startsWith(it) }.fold(0L) { acc, next ->
        acc + cache.getOrPut(pattern.drop(next.length)) { countPossibilities(pattern.drop(next.length), towels) }
    }
}