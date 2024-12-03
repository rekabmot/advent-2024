package day02

import utils.readInput

fun main() {
    val input = readInput("day02/input").map { line -> line.split(" ").map { it.toInt() } }

    val deltas = input.map { it.windowed(2).map { window -> window[1] - window[0] } }

    val p1 = deltas.count { isSafe(it) }

    val p2 = input.count {
        it.indices.any {i ->
            val clone = it.toMutableList()
            clone.removeAt(i)
            isSafe(clone.windowed(2).map { window -> window[1] - window[0] })
        }
    }

    println(p1)
    println(p2)
}

fun isSafe(deltas: List<Int>) = deltas.all { it in 1..3 } || deltas.all { it in -1 downTo -3 }