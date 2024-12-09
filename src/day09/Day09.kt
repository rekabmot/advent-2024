package day09

import utils.readText

fun main() {
    val input = readText("day09/input").map(Character::getNumericValue)

    part1(input)
}

private fun part1(input: List<Int>) {
    val output = mutableListOf<Long>()

    var i = 0
    var j = input.size + 1
    var jRemaining = 0

    while (i < j) {
        if (i % 2 == 0) {
            val size = input[i]

            repeat(size) {
                output.add((i / 2).toLong())
            }
        } else {
            var gapRemaining = input[i]

            while (gapRemaining > 0) {
                if (jRemaining == 0) {
                    j -= 2
                    jRemaining = input[j]
                }
                output.add((j / 2).toLong())
                jRemaining--
                gapRemaining--
            }
        }

        i++
    }

    println(input)

    repeat(jRemaining) {
        output.add(((i + 1) / 2).toLong())
    }

    println(output.joinToString(""))

    val p1 = output.mapIndexed { index, file -> index * file }.sum()
    println(p1)
}