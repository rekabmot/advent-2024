package day14

import utils.Vec2
import utils.readInput

val REGEX = "p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)".toRegex()

data class Robot(val position: Vec2, val velocity: Vec2)

fun main() {
    val robots = readInput("day14/input").map {
        val matches = REGEX.matchEntire(it)!!.groupValues

        Robot(Vec2(matches[1].toInt(), matches[2].toInt()), Vec2(matches[3].toInt(), matches[4].toInt()))
    }

    val gridSize = Vec2(101, 103)
    val steps = 100

    part1(robots, steps, gridSize)
    part2(robots, gridSize)
}

fun scoreGrid(positions: List<Vec2>, gridSize: Vec2) = positions.fold(listOf(0, 0, 0, 0)) { acc, it ->
    if (it.x < gridSize.x / 2 && it.y < gridSize.y / 2) {
        listOf(acc[0] + 1, acc[1], acc[2], acc[3])
    } else if (it.x > gridSize.x / 2 && it.y < gridSize.y / 2) {
        listOf(acc[0], acc[1] + 1, acc[2], acc[3])
    } else if (it.x < gridSize.x / 2 && it.y > gridSize.y / 2) {
        listOf(acc[0], acc[1], acc[2] + 1, acc[3])
    } else if (it.x > gridSize.x / 2 && it.y > gridSize.y / 2) {
        listOf(acc[0], acc[1], acc[2], acc[3] + 1)
    } else {
        acc
    }
}.reduce { acc, next -> acc * next }

fun part1(robots: List<Robot>, steps: Int, gridSize: Vec2) {
    val positions = moveRobots(robots, steps, gridSize)
    println(scoreGrid(positions, gridSize))
}

private fun moveRobots(robots: List<Robot>, steps: Int, gridSize: Vec2) = robots.map {
    Vec2(
        (it.position.x + (it.velocity.x * steps)).mod(gridSize.x),
        (it.position.y + (it.velocity.y * steps)).mod(gridSize.y)
    )
}

fun part2(robots: List<Robot>, gridSize: Vec2) {
    var lowestScore = Int.MAX_VALUE
    repeat(10000) { i ->
        val positions = moveRobots(robots, i, gridSize)

        val score = scoreGrid(positions, gridSize)
        if (score < lowestScore) {
            lowestScore = score
            println(i)
            printGrid(gridSize, positions)
        }
    }
}

fun printGrid(gridSize: Vec2, positions: List<Vec2>) {
    val grid = (0..<gridSize.y).map { y ->
        (0..<gridSize.x).map { x ->
            if (positions.contains(Vec2(x, y))) {
                "#"
            } else {
                "."
            }
        }
    }

    grid.forEach { println(it.joinToString("")) }
}

