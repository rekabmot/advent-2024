package day06

import utils.Vec2
import utils.readInput

fun main() {

    val input = readInput("day06/input")

    val xBounds = input.first().indices
    val yBounds = input.indices
    val startPosition = Vec2(input.first { it.contains("^") }.indexOf("^"), input.indexOfFirst { it.contains("^") })

    var position = startPosition
    var direction = 0

    val visitedPositions = mutableListOf<Pair<Vec2, Int>>()

    fun inBounds(position: Vec2) = xBounds.contains(position.x) && yBounds.contains(position.y)

    while (inBounds(position)) {
        visitedPositions.add(Pair(position, direction))
        val newPosition = position + Vec2.CARDINAL_DIRECTIONS[direction]

        if (inBounds(newPosition) && input[newPosition.y][newPosition.x] == '#') {
            direction = (direction + 1) % 4
        } else {
            position = newPosition
        }
    }

    val p1 = visitedPositions.map { it.first }.distinct()

    // Part 1
    println(p1.count())
    printGrid(input, p1)

    val obstacles = mutableListOf<Vec2>()

    p1.forEach {
        if (it != startPosition) {
            if (findLoop(input, it, startPosition)) {
                obstacles.add(it)
            }
        }
    }

    println(obstacles.distinct().size)
}

fun findLoop(input: List<String>, obstacle: Vec2, startPosition: Vec2): Boolean {
    val xBounds = input.first().indices
    val yBounds = input.indices
    fun inBounds(position: Vec2) = xBounds.contains(position.x) && yBounds.contains(position.y)

    var position = startPosition
    var direction = 0

    val visitedPositions = mutableListOf<Pair<Vec2, Int>>()

    while (inBounds(position)) {
        if (visitedPositions.contains(Pair(position, direction))) {
            return true
        }

        visitedPositions.add(Pair(position, direction))

        val newPosition = position + Vec2.CARDINAL_DIRECTIONS[direction]

        if (inBounds(newPosition) && (input[newPosition.y][newPosition.x] == '#' || newPosition == obstacle)) {
            direction = (direction + 1) % 4
        } else {
            position = newPosition
        }
    }

    return false
}



fun printGrid(input: List<String>, path: List<Vec2>) {
    input.indices.map { y ->
        input[y].indices.joinToString("") { x ->
            if (path.contains(Vec2(x, y))) {
                "X"
            } else if (input[y][x] == '#') {
                "#"
            } else {
                "."
            }
        }
    }.forEach { println(it) }
}
