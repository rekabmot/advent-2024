package day01

import utils.readInput
import kotlin.math.abs


fun main() {
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()

    readInput("day01/input").forEach {
        val (l, r) = it.split("   ")
        left.add(l.toInt())
        right.add(r.toInt())
    }

    left.sort()
    right.sort()

    val p1 = left.indices.sumOf { abs(left[it] - right[it]) }
    println(p1)

    val p2 = left.sumOf { it * right.count { r -> r == it }}
    println(p2)
}