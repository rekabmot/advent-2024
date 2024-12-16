package day15

import utils.Vec2
import utils.readInput

val MOVES = mapOf(
    '^' to Vec2.CARDINAL_DIRECTIONS[0],
    '>' to Vec2.CARDINAL_DIRECTIONS[1],
    'v' to Vec2.CARDINAL_DIRECTIONS[2],
    '<' to Vec2.CARDINAL_DIRECTIONS[3],
)

data class Box(val positions: List<Vec2>) {
    fun move(direction: Vec2) = positions.map { it + direction }
    fun intersects(position: Vec2) = positions.contains(position)
    fun score() = positions.first().y * 100 + positions.first().x
}

data class Environment(val walls: List<Vec2>, val boxes: MutableList<Box>, val robotPosition: Vec2)

fun main() {
    val input = readInput("day15/input")

    val grid = input.subList(0, input.indexOf(""))
    val instructions = input.subList(input.indexOf("") + 1, input.size).joinToString("")

    println(solve(buildEnvironment(1, grid), instructions))
    println(solve(buildEnvironment(2, grid), instructions))
}

fun solve(environment: Environment, instructions: String): Int {
    var robotPosition = environment.robotPosition

    instructions.forEach {
        val move = MOVES[it]!!

        val newPos = robotPosition + move

        if (environment.walls.contains(newPos)) {
            // Do nothing
        } else if (environment.boxes.any { box -> box.intersects(newPos) }) {
            val box = environment.boxes.first { box -> box.intersects(newPos) }
            if (checkPush(box, environment.boxes, environment.walls, move)) {
                pushBox(box, environment.boxes, environment.walls, move)
                robotPosition = newPos
            }
        } else {
            robotPosition = newPos
        }
    }

    return environment.boxes.sumOf { it.score() }
}

fun buildEnvironment(xFactor: Int, grid: List<String>): Environment {
    val walls = mutableListOf<Vec2>()
    val boxes = mutableListOf<Box>()
    var robotPosition: Vec2? = null

    grid.indices.forEach { y ->
        grid[y].indices.forEach { x ->
            when (grid[y][x]) {
                '#' -> {
                    repeat(xFactor) {
                        walls.add(Vec2(xFactor * x + it, y))
                    }
                }
                'O' -> {
                    boxes.add(Box((0..< xFactor).map { Vec2(xFactor * x + it, y) }))
                }
                '@' -> {
                    robotPosition = Vec2(x * xFactor, y)
                }
            }
        }
    }

    return Environment(walls, boxes, robotPosition!!)
}

fun printGrid(walls: List<Vec2>, boxes: List<Box>, robot: Vec2) {
    val xIndices = walls.minBy { it.x }.x .. walls.maxBy { it.x }.x
    val yIndices = walls.minBy { it.y }.y .. walls.maxBy { it.y }.y

    yIndices.forEach { y ->
        val line = xIndices.map { x ->
            if (walls.contains(Vec2(x, y))) {
                '#'
            } else if (boxes.any { it.intersects(Vec2(x, y)) })  {
                'O'
            } else if (y == robot.y && x == robot.x){
                '@'
            } else {
                '.'
            }
        }.joinToString("")

        println(line)
    }
}

fun checkPush(box: Box, boxes: MutableList<Box>, walls: List<Vec2>, direction: Vec2): Boolean {
    val newBoxPositions = box.move(direction)

    val canMove = newBoxPositions.all {
        !walls.contains(it) &&
                boxes.filter { b -> b.intersects(it) && b != box }.all { b -> checkPush(b, boxes, walls, direction) }
    }

    return canMove
}

fun pushBox(box: Box, boxes: MutableList<Box>, walls: List<Vec2>, direction: Vec2) {
    val newBoxPositions = box.move(direction)

    newBoxPositions.forEach {
        boxes.filter { b -> b.intersects(it) && b != box }.forEach { b -> pushBox(b, boxes, walls, direction) }
    }
    boxes[boxes.indexOfFirst { it.positions == box.positions }] = Box(newBoxPositions)
}