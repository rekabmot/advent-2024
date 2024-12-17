package day16

import utils.Vec2
import utils.readInput

fun main() {

    val input = readInput("day16/input")
    val startPosition = findChar('S', input)
    val endPosition = findChar('E', input)

    val p1 = solve(startPosition, endPosition, input)

    println(p1)
}

data class Position(val position: Vec2, val facing: Vec2)

fun solve(start: Vec2, end: Vec2, grid: List<String>): Int {
    val cameFrom = mutableMapOf<Position, MutableList<Position>>()

    val startPosition = Position(start, Vec2.CARDINAL_DIRECTIONS[1])

    val gScore = mutableMapOf(startPosition to 0)

    val frontier = mutableListOf(startPosition)

    while (frontier.isNotEmpty()) {
        val current = frontier.removeAt(0)

        if (current.position != end) {
            Vec2.CARDINAL_DIRECTIONS.filter { it != current.facing * -1 }.forEach {
                val newPos = current.position + it

                if (grid[newPos.y][newPos.x] != '#') {
                    val cost = if (it == current.facing) {
                        1
                    } else {
                        1001
                    }

                    val newScore = gScore.getValue(current) + cost

                    val p = Position(newPos, it)

                    if (!gScore.containsKey(p) || newScore < gScore.getValue(p)) {
                        cameFrom[p] = mutableListOf(current)
                        gScore[p] = newScore

                        if (!frontier.contains(p)) {
                            frontier.add(p)

                            frontier.sortWith { a, b -> gScore.getValue(a) - gScore.getValue(b) }
                        }
                    }
                }
            }
        }
    }

    val ends = gScore.filter { it.key.position == end }
    println(ends.minBy{ it.value }.value)

    return -1
}

fun findChar(c: Char, g: List<String>) = Vec2(g.first { it.contains(c) }.indexOf(c), g.indexOfFirst { it.contains(c) })