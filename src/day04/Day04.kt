package day04

import utils.Vec2
import utils.readInput

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
            xAcc + Vec2.ORDINAL_DIRECTIONS.fold(0) { dAcc, direction ->
                dAcc + if (findWord(input, Vec2(x, y), direction, 0)) 1 else 0
            }
        }
    }

    println(result)
}

fun findWord(grid: List<String>, pos: Vec2, dir: Vec2, current: Int): Boolean {
    if (pos.x < 0 || pos.x >= grid.first().length || pos.y < 0 || pos.y >= grid.size) return false
    if (grid[pos.y][pos.x] != WORD[current]) return false
    if (current == WORD.length - 1) return true

    return findWord(grid, pos + dir, dir, current + 1)
}

fun part2(input: List<String>) {
    val yIndices = 1..<input.size - 1
    val xIndices = 1 ..<input.first().length - 1

    val result = yIndices.fold(0) { yAcc, y ->
        yAcc + xIndices.fold(0) { xAcc, x ->
            if (input[y][x] == 'A') {
                val d1 = listOf(input[y - 1][x - 1], input[y + 1][x + 1]).sorted().joinToString("")
                val d2 = listOf(input[y - 1][x + 1], input[y + 1][x - 1]).sorted().joinToString("")

                xAcc + if (d1 == "MS" && d2 == "MS") 1 else 0
            } else {
                xAcc
            }
        }
    }

    println(result)
}