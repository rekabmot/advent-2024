package day03

import utils.readText

val P1_MATCHER = "mul\\((\\d+),(\\d+)\\)".toRegex()
val P2_MATCHER = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)".toRegex()

fun main() {
    val input = readText("day03/input")

    val p1Results = P1_MATCHER.findAll(input)
    val p2Results = P2_MATCHER.findAll(input)

    val p1 = p1Results.sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    println(p1)

    var flag = true

    val p2 = p2Results.sumOf {
        if (it.value == "don't()") {
            flag = false
            0
        } else if (it.value == "do()") {
            flag = true
            0
        } else {
            if (flag) it.groupValues[1].toInt() * it.groupValues[2].toInt() else 0
        }
    }

    println(p2)
}