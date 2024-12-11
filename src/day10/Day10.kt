package day10

import utils.Vec2
import utils.readInput

fun main() {
    val input = readInput("day10/input").map { line -> line.map { it.digitToInt() } }

    val yIndices = input.indices
    val xIndices = input.first().indices

    fun exploreTrailhead(startPos: Vec2): Pair<Int, Int> {
        val summits = mutableListOf<Vec2>()
        val frontier = mutableListOf(startPos)
        val visited = mutableListOf<Vec2>()

        while (frontier.isNotEmpty()) {
            val toExplore = frontier.removeAt(0)
            val currentElevation = input[toExplore.y][toExplore.x]

            Vec2.CARDINAL_DIRECTIONS.forEach {
                val newPosition = toExplore + it

                if (yIndices.contains(newPosition.y) && xIndices.contains(newPosition.x)) {
                    if (currentElevation == 8 && input[newPosition.y][newPosition.x] == 9) {
                        summits.add(newPosition)
                    } else if (input[newPosition.y][newPosition.x] - currentElevation == 1 && !visited.contains(newPosition)) {
                        frontier.add(newPosition)
                    }
                }
            }

            visited.add(toExplore)
        }

        return Pair(summits.distinct().size, summits.size)
    }

    val (p1, p2) = yIndices.fold(Pair(0, 0)) { yAcc, y ->
        val yResult = xIndices.fold(Pair(0, 0)) { xAcc, x ->
            if (input[y][x] == 0) {
                val xResult = exploreTrailhead(Vec2(x, y))
                Pair(xAcc.first + xResult.first, xAcc.second + xResult.second)
            } else {
                xAcc
            }
        }
        Pair(yAcc.first + yResult.first, yAcc.second + yResult.second)
    }

    println(p1)
    println(p2)
}

