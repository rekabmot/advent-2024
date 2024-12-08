package day07

import utils.readInput

val ADD = { x: Long, y: Long -> x + y }
val MUL = { x: Long, y: Long -> x * y }
val CONCAT = { x: Long, y: Long -> (x.toString() + y.toString()).toLong()}

fun main() {
    val input = readInput("day07/input").associate {
        val (target, values) = it.split(": ")
        target.toLong() to values.split(" ").map { v -> v.toLong() }
    }

    val p1 = input.filter { (k, v) -> isValid(k, v, listOf(ADD, MUL)) }.map { it.key }.sum()
    val p2 = input.filter { (k, v) -> isValid(k, v, listOf(ADD, MUL, CONCAT)) }.map { it.key }.sum()

    println(p1)
    println(p2)
}

fun isValid(target: Long, values: List<Long>, operations: List<(Long, Long) -> Long>) =
    solve(values.first(), target, values, 1, operations)

fun solve(current: Long, target: Long, values: List<Long>, i: Int, operations: List<(Long, Long) -> Long>): Boolean {
    if (i == values.size) return current == target
    return operations.any { solve(it(current, values[i]), target, values, i + 1, operations) }
}
