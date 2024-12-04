package day04

import utils.Vec2
import utils.readInput

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

const val WORD = "XMAS"

fun main() {
    val input = readInput("day04/input")

    part1(input)
    part2(input)
}

fun part1(input: List<String>) {
    val yIndices = input.indices
    val xIndices = input.first().indices

    val result = yIndices.fold(0) { yAcc, y ->
        yAcc + xIndices.fold(0) { xAcc, x ->
            xAcc + P1_DIRECTIONS.fold(0) { dAcc, direction ->
                if (findWord(input, Vec2(x, y), direction, 0)) dAcc + 1 else dAcc
            }
        }
    }

    println(result)
}

fun findWord(grid: List<String>, pos: Vec2, dir: Vec2, current: Int): Boolean {
    if (pos.x < 0 || pos.x >= grid.first().length || pos.y < 0 || pos.y >= grid.size) return false
    if (grid[pos.y][pos.x] != WORD[current]) return false
    if (current == WORD.length - 1) return true

    return findWord(grid, pos.add(dir), dir, current + 1)
}

fun part2(input: List<String>) {
    val yIndices = 1..<input.size - 1
    val xIndices = 1 ..<input.first().length - 1

    val result = yIndices.fold(0) { yAcc, y ->
        yAcc + xIndices.fold(0) { xAcc, x ->
            if (input[y][x] == 'A') {
                val d1 = listOf(input[y][x], input[y - 1][x - 1], input[y + 1][x + 1]).sorted().joinToString("")
                val d2 = listOf(input[y][x], input[y - 1][x + 1], input[y + 1][x - 1]).sorted().joinToString("")

                if (d1 == "AMS" && d2 == "AMS") xAcc + 1 else xAcc
            } else {
                xAcc
            }
        }
    }

    println(result)
}