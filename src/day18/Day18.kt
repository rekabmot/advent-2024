package day18

import utils.Vec2
import utils.readInput
import kotlin.time.measureTime

const val GRID_SIZE = 70
const val FALLEN_BYTES = 1024

val validRange = (0..GRID_SIZE)
val startPosition = Vec2(0, 0)
val endPosition = Vec2(GRID_SIZE, GRID_SIZE)

fun main() {
    val input = readInput("day18/input")
        .map { it.split(",").map { x -> x.toInt() } }
        .map { (x, y) -> Vec2(x, y) }

    val p1 = solve(input.take(FALLEN_BYTES).toSet())
    println(p1.size - 1)

    var bestPath = p1

    val blockingIndex = (FALLEN_BYTES..input.size).first {
        if (bestPath.contains(input[it])) {
            bestPath = solve(input.take(it + 1).toSet())
        }

        bestPath.isEmpty()
    }

    val p2 = input[blockingIndex]
    println("${p2.x},${p2.y}")
}

fun solve(corruptedSpaces: Set<Vec2>): List<Vec2> {
    val cameFrom = mutableMapOf<Vec2, Vec2>()
    val gScore = mutableMapOf(startPosition to 0)

    val frontier = mutableListOf(startPosition)

    while (frontier.isNotEmpty()) {
        val current = frontier.removeAt(0)

        if (current == endPosition) {
            val path = mutableListOf(current)
            var c = current

            while (c in cameFrom.keys) {
                c = cameFrom.getValue(c)
                path.add(0, c)
            }

            return path
        }

        Vec2.CARDINAL_DIRECTIONS.forEach {
            val newPos = current + it

            if (!corruptedSpaces.contains(newPos) && validRange.contains(newPos.x) && validRange.contains(newPos.y)) {
                val newCost = gScore.getValue(current) + 1

                if (newCost < gScore.getOrDefault(newPos, Int.MAX_VALUE)) {
                    cameFrom[newPos] = current
                    gScore[newPos] = newCost

                    if (!frontier.contains(newPos)) {
                        frontier.add(newPos)

                        frontier.sortWith { a, b -> gScore.getValue(a) - gScore.getValue(b) }
                    }
                }
            }
        }
    }
    return mutableListOf()
}

fun drawGrid(corruptedSpaces: List<Vec2>, path: List<Vec2>) {
    (0..GRID_SIZE).map { y ->
        (0..GRID_SIZE).map { x ->
            if (path.contains(Vec2(x, y))) {
                'O'
            } else if (corruptedSpaces.contains(Vec2(x, y))) {
                '#'
            } else {
                '.'
            }
        }.joinToString("")
    }.forEach { println(it) }
}