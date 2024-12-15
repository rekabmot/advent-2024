package day15

import utils.Vec2
import utils.readInput


val MOVES = mapOf(
    '^' to Vec2.CARDINAL_DIRECTIONS[0],
    '>' to Vec2.CARDINAL_DIRECTIONS[1],
    'v' to Vec2.CARDINAL_DIRECTIONS[2],
    '<' to Vec2.CARDINAL_DIRECTIONS[3],
)

fun main() {
    val input = readInput("day15/input")

    val walls = mutableListOf<Vec2>()
    val boxes = mutableListOf<Vec2>()
    var robotPosition: Vec2? = null

    val grid = input.subList(0, input.indexOf(""))
    val instructions = input.subList(input.indexOf("") + 1, input.size).joinToString("")

    grid.indices.forEach { y ->
        grid[y].indices.forEach { x ->
            when (grid[y][x]) {
                '#' -> walls.add(Vec2(x, y))
                'O' -> boxes.add(Vec2(x, y))
                '@' -> robotPosition = Vec2(x, y)
            }
        }
    }

    printGrid(walls, boxes, robotPosition!!)

    instructions.forEach {
        val move = MOVES[it]!!

        val newPos = robotPosition!! + move

        if (walls.contains(newPos)) {
            // Do nothing
        } else if (boxes.contains(newPos)) {
            if (pushBox(newPos, boxes, walls, move)) {
                robotPosition = newPos
            }
        } else {
            robotPosition = newPos
        }

//        printGrid(walls, boxes, robotPosition!!)
//        println()
    }

    val p1 = boxes.sumOf { (100 * it.y) + it.x }

    println(p1)
}

fun printGrid(walls: List<Vec2>, boxes: List<Vec2>, robot: Vec2) {
    val xIndices = walls.minBy { it.x }.x .. walls.maxBy { it.x }.x
    val yIndices = walls.minBy { it.y }.y .. walls.maxBy { it.y }.y

    yIndices.forEach { y ->
        val line = xIndices.map { x ->
            if (walls.contains(Vec2(x, y))) {
                '#'
            } else if (boxes.contains(Vec2(x, y))) {
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

fun pushBox(box: Vec2, boxes: MutableList<Vec2>, walls: List<Vec2>, direction: Vec2): Boolean {
    val newBoxPos = box + direction

    if (walls.contains(newBoxPos)) {
        return false
    } else if (boxes.contains(newBoxPos)) {
        val result = pushBox(newBoxPos, boxes, walls, direction)

        if (result) {
            boxes[boxes.indexOf(box)] = newBoxPos
            return true
        } else {
            return false
        }
    }

    boxes[boxes.indexOf(box)] = newBoxPos
    return true
}