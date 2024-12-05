package day05

import utils.readInput

fun main() {
    val input = readInput("day05/input")
    val pivot = input.indexOf("")

    val afterRules = mutableMapOf<Int, MutableList<Int>>()
    val beforeRules = mutableMapOf<Int, MutableList<Int>>()

    input.subList(0, pivot).forEach { rule ->
        val (l, r) = rule.split("|").map { it.toInt() }

        afterRules.getOrPut(l) { mutableListOf() }.add(r)
        beforeRules.getOrPut(r) { mutableListOf() }.add(l)
    }

    val updates = input.subList(pivot + 1, input.size).map { update -> update.split(",").map { it.toInt() } }

    fun isUpdateValid(update: List<Int>): Boolean {
        update.forEachIndexed { index, page ->
            val beforePages = update.subList(0, index)
            val afterPages = update.subList(index + 1, update.size)

            val bRules = beforeRules.getOrDefault(page, mutableListOf())
            val aRules = afterRules.getOrDefault(page, mutableListOf())

            if (!beforePages.all { bRules.contains(it) } && afterPages.all { aRules.contains(it) }) {
                return false
            }
        }

        return true
    }

    val (validOrderings, invalidOrderings) = updates.partition { isUpdateValid(it) }

    val p1 = validOrderings.sumOf {
        it[it.size / 2]
    }
    println(p1)

    val p2 = invalidOrderings.map {
        it.sortedWith { a, b ->
            if (afterRules.getOrDefault(a, mutableListOf()).contains(b)) {
                -1
            } else if (beforeRules.getOrDefault(a, mutableListOf()).contains(b)) {
                1
            } else {
                0
            }
        }
    }.sumOf { it[it.size / 2] }

    println(p2)
}