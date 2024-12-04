package day04

import utils.readInput

data class Vec2(val x: Int, val y: Int) {
    fun add(other: Vec2): Vec2 = Vec2(this.x + other.x, this.y + other.y)
}

val P1_DIRECTIONS = listOf(
    Vec2(0, -1), // N
    Vec2(1, -1), // NE
    Vec2(1, 0), // E
    Vec2(1, 1), // SE
    Vec2(0, 1), // S
    Vec2(-1, 1), // SW
    Vec2(-1, 0), // W
    Vec2(-1, -1), // NW
)

val WORD = "XMAS"

fun main() {
    val input = readInput("day04/input")

    part1(input)
    part2(input)

}

fun part1(input: List<String>) {
    val yIndices = input.indices
    val xIndices = input.first().indices

    var p1 = 0

    yIndices.forEach { y ->
        xIndices.forEach { x ->
            P1_DIRECTIONS.forEach { direction ->
                if (findWord(input, Vec2(x, y), direction, 0)) {
                    p1++
                }
            }
        }
    }

    println(p1)
}

fun findWord(grid: List<String>, position: Vec2, direction: Vec2, currentLetter: Int): Boolean {
    if (
        position.x < 0 || position.x >= grid.first().length ||
        position.y < 0 || position.y >= grid.size
    ) {
        return false
    }

    if (grid[position.y][position.x] == WORD[currentLetter]) {
        return if (currentLetter == WORD.length - 1) {
            true
        } else {
            findWord(grid, position.add(direction), direction, currentLetter + 1)
        }
    }

    return false
}

fun part2(input: List<String>) {
    val yIndices = 1..<input.size - 1
    val xIndices = 1 ..<input.first().length - 1

    var p2 = 0

    yIndices.forEach { y ->
        xIndices.forEach { x ->
            if (input[y][x] == 'A') {
                val d1 = listOf(input[y][x], input[y - 1][x - 1], input[y + 1][x + 1]).sorted().joinToString("")
                val d2 = listOf(input[y][x], input[y - 1][x + 1], input[y + 1][x - 1]).sorted().joinToString("")

                if (d1 == "AMS" && d2 == "AMS") {
                    p2++
                }
            }
        }
    }

    println(p2)
}