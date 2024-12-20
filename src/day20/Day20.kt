package day20

import utils.Vec2
import utils.readInput

fun main() {
    val input = readInput("day20/input")
    val startPosition = findChar('S', input)
    val endPosition = findChar('E', input)

    val path = findPath(input, startPosition, endPosition)

    println(path.mapIndexed { index, position -> findShortcuts(path, index, position, 2) }.sum())
    println(path.mapIndexed { index, position -> findShortcuts(path, index, position, 20) }.sum())
}

private fun findPath(input: List<String>, start: Vec2, end: Vec2): List<Vec2> {
    var current = start

    val path = mutableListOf<Vec2>()

    val xRange = input.first().indices
    val yRange = input.indices

    while (current != end) {
        path.add(current)

        current += Vec2.CARDINAL_DIRECTIONS.first {
            val newPos = current + it
            !path.contains(newPos) && yRange.contains(newPos.y) && xRange.contains(newPos.x) && input[newPos.y][newPos.x] != '#'
        }
    }

    path.add(end)
    return path
}

fun findShortcuts(path: List<Vec2>, index: Int, position: Vec2, cheat: Int) =
    path.withIndex().drop(index + 1).count { (i, it) ->
        val d = position.distance(it)
        d <= cheat && path[index + d] != it && (i - index) - d >= 100
    }

fun findChar(c: Char, g: List<String>) = Vec2(g.first { it.contains(c) }.indexOf(c), g.indexOfFirst { it.contains(c) })
