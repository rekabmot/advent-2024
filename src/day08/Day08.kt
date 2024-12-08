package day08

import utils.Vec2
import utils.readInput

fun main() {
    val input = readInput("day08/input")

    val xRange = input.first().indices
    val yRange = input.indices

    val p1Antinodes = mutableListOf<Vec2>()
    val p2Antinodes = mutableListOf<Vec2>()

    yRange.forEach { y ->
        xRange.forEach { x ->
            if (input[y][x] != '.') {
                val frequency = input[y][x]

                val antennaPosition = Vec2(x, y)
                p2Antinodes.add(antennaPosition)

                yRange.forEach { y2 ->
                    xRange.forEach { x2 ->
                        if (!(x == x2 && y == y2) && input[y2][x2] == frequency) {
                            val otherPosition = Vec2(x2, y2)

                            val difference = otherPosition + antennaPosition
                            var offset = antennaPosition + (difference * 2)

                            if (xRange.contains(offset.x) && yRange.contains(offset.y)) {
                                p1Antinodes.add(offset)
                            }

                            while (xRange.contains(offset.x) && yRange.contains(offset.y)) {
                                p2Antinodes.add(offset)
                                offset += difference
                            }
                        }
                    }
                }
            }
        }
    }

    println(p1Antinodes.distinct().count())
    println(p2Antinodes.distinct().count())
}