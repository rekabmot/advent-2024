package day22

import utils.readInput

fun main() {
    val input = readInput("day22/test2")

    val p1 = input.map {
        var x = it.toLong()
        var prev = x
        val deltas = mutableListOf(0L)
        repeat(9) {
            x = solve(x)
            deltas.add((x % 10) - (prev % 10))
            prev = x
        }

        Pair(x, deltas)
    }

    println(p1.sumOf { it.first })

    println(p1.first().second)
}

fun solve(number: Long): Long {
    val a = prune(mix(number, number * 64))
    val b = prune(mix(a, a / 32))
    val c = prune(mix(b, b * 2048))
    return c
}

fun mix(a: Long, b: Long): Long {
    return a xor b
}

fun prune(number: Long): Long {
    return number.mod(16777216L)
}