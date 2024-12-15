package day12

import utils.Vec2
import utils.readInput

fun main() {
    val input = readInput("day12/input")

    val visited = mutableListOf<Vec2>()

    val xRange = input.first().indices
    val yRange = input.indices

    fun exploreRegion(start: Vec2): Pair<Int, Int> {
        val regionId = input[start.y][start.x]

        val frontier = mutableListOf(start)
        val region = mutableListOf<Vec2>()

        var area = 1
        var perimeter = 0

        visited.add(start)

        while (frontier.isNotEmpty()) {
            val current = frontier.removeAt(0)
            region.add(current)

            val perimeterNeighbours = Vec2.CARDINAL_DIRECTIONS.mapNotNull {
                val newPosition = current + it

                if (xRange.contains(newPosition.x) && yRange.contains(newPosition.y) &&
                    input[newPosition.y][newPosition.x] == regionId
                ) {
                    newPosition
                } else {
                    null
                }
            }

            val areaNeighbours = perimeterNeighbours.filter {
                !visited.contains(it)
            }

            area += areaNeighbours.size
            perimeter += (4 - perimeterNeighbours.size)

            frontier.addAll(areaNeighbours)
            visited.addAll(areaNeighbours)
        }

        val sides = countSides(region) + findSubRegions(region).sumOf { countSides(it) }

        return Pair(area * perimeter, area * sides)
    }

    val answers = yRange.fold(Pair(0, 0)) { yAcc, y ->
        val yResult = xRange.fold(Pair(0, 0)) { xAcc, x ->
            if (!visited.contains(Vec2(x, y))) {
                val xResult = exploreRegion(Vec2(x, y))
                Pair(xAcc.first + xResult.first, xAcc.second + xResult.second)
            } else {
                xAcc
            }
        }
        Pair(yAcc.first + yResult.first, yAcc.second + yResult.second)
    }

    println(answers.first)
    println(answers.second)
}

fun countSides(region: List<Vec2>): Int {
    val yMin = region.minBy { it.y }.y
    val xMin = region.filter { it.y == yMin }.minBy { it.x }.x

    val startPos = Vec2(xMin, yMin - 1)
    var currentPos = startPos
    var direction = 1

    var sides = 0

    while (currentPos != startPos || sides == 0) {
        if (!region.contains(currentPos + Vec2.CARDINAL_DIRECTIONS[(direction + 1) % 4])) {
            sides += 1
            direction = (direction + 1) % 4
            currentPos += Vec2.CARDINAL_DIRECTIONS[direction]
        } else if (region.contains(currentPos + Vec2.CARDINAL_DIRECTIONS[direction])) {
            sides += 1
            direction = (direction - 1) % 4
            if (direction == -1) {
                direction = 3
            }
        } else {
            currentPos += Vec2.CARDINAL_DIRECTIONS[direction]
        }
    }

    return sides
}

fun findSubRegions(region: List<Vec2>): List<List<Vec2>> {
    val xMin = region.minBy { it.x }.x
    val yMin = region.minBy { it.y }.y
    val xMax = region.maxBy { it.x }.x
    val yMax = region.maxBy { it.y }.y

    val offsetRegion = region.map { Vec2((it.x - xMin) + 1, (it.y - yMin) + 1) }

    val borderedGrid = (0..(yMax - yMin) + 2).map { y ->
        (0..(xMax - xMin) + 2).map { x ->
            if (offsetRegion.contains(Vec2(x, y))) {
                1
            } else {
                0
            }
        }
    }

    val floodBorder = mutableListOf<Vec2>()

    val frontier = mutableListOf(Vec2(0, 0))

    val yRange = borderedGrid.indices
    val xRange = borderedGrid.first().indices

    while (frontier.isNotEmpty()) {
        val current = frontier.removeAt(0)

        floodBorder.add(current)

        Vec2.CARDINAL_DIRECTIONS.forEach {
            val newPos = current + it
            if (xRange.contains(newPos.x) && yRange.contains(newPos.y)
                && !floodBorder.contains(newPos)
                && !frontier.contains(newPos)
                && borderedGrid[newPos.y][newPos.x] == 0
            ) {
                frontier.add(newPos)
            }
        }
    }

    val floodedGrid = (0..(yMax - yMin) + 2).map { y ->
        (0..(xMax - xMin) + 2).map { x ->
            if (offsetRegion.contains(Vec2(x, y)) || floodBorder.contains(Vec2(x, y))) {
                1
            } else {
                0
            }
        }
    }

    return getSubRegions(yRange, xRange, floodedGrid)
}

private fun getSubRegions(
    yRange: IntRange,
    xRange: IntRange,
    floodedGrid: List<List<Int>>,
): List<List<Vec2>> {
    val processedPositions = mutableListOf<Vec2>()

    val subRegions = mutableListOf<List<Vec2>>()

    yRange.forEach { y ->
        xRange.forEach { x ->
            if (floodedGrid[y][x] == 0 && !processedPositions.contains(Vec2(x, y))) {

                val newRegion = mutableListOf<Vec2>()
                val newRegionFrontier = mutableListOf(Vec2(x, y))

                while (newRegionFrontier.isNotEmpty()) {
                    val current = newRegionFrontier.removeAt(0)
                    newRegion.add(current)
                    processedPositions.add(current)

                    Vec2.CARDINAL_DIRECTIONS.forEach {
                        val newPos = current + it
                        if (xRange.contains(newPos.x) && yRange.contains(newPos.y)
                            && floodedGrid[newPos.y][newPos.x] == 0 && !processedPositions.contains(newPos)
                        ) {
                            newRegionFrontier.add(newPos)
                        }
                    }
                }

                subRegions.add(newRegion.distinct())
            }
        }
    }
    return subRegions
}