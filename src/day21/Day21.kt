package day21

import utils.Vec2
import utils.readInput
import kotlin.math.abs


// NUMBER PAD
//     0   1   2
//   +---+---+---+
// 0 | 7 | 8 | 9 |
//   +---+---+---+
// 1 | 4 | 5 | 6 |
//   +---+---+---+
// 2 | 1 | 2 | 3 |
//   +---+---+---+
// 3     | 0 | A |
//       +---+---+

val NUMBER_PAD = mapOf(
    'A' to Vec2(2, 3),
    '0' to Vec2(1, 3),
    '1' to Vec2(0, 2),
    '2' to Vec2(1, 2),
    '3' to Vec2(2, 2),
    '4' to Vec2(0, 1),
    '5' to Vec2(1, 1),
    '6' to Vec2(2, 1),
    '7' to Vec2(0, 0),
    '8' to Vec2(1, 0),
    '9' to Vec2(2, 0),
    'X' to Vec2(2, 3)
)

// DIRECTION PAD
//     0   1   2
//       +---+---+
// 0     | ^ | A |
//   +---+---+---+
// 1 | < | v | > |
//   +---+---+---+

val DIRECTION_PAD = mapOf(
    'A' to Vec2(2, 0),
    '^' to Vec2(1, 0),
    '<' to Vec2(0, 1),
    'v' to Vec2(1, 1),
    '>' to Vec2(2, 1),
    'X' to Vec2(0, 0)
)

fun navigateNumberPad(from: Vec2, to: Vec2): String {
    val delta = to - from

    val lateralMove = if (delta.x > 0) '>' else '<'
    val verticalMove = if (delta.y > 0) 'v' else '^'

    val moves = mutableListOf<Char>()

    if ((from.y == 3 && to.x == 0)) {
        move(abs(delta.y), moves, verticalMove)
        move(abs(delta.x), moves, lateralMove)
    } else {
        move(abs(delta.x), moves, lateralMove)
        move(abs(delta.y), moves, verticalMove)
    }

    moves.add('A')

    return moves.joinToString("")
}

fun navigateDirectionPad(from: Vec2, to: Vec2): String {
    val delta = to - from

    val lateralMove = if (delta.x > 0) '>' else '<'
    val verticalMove = if (delta.y > 0) 'v' else '^'

    val moves = mutableListOf<Char>()

    if (from.y == 0 && to.x == 0) {
        move(abs(delta.y), moves, verticalMove)
        move(abs(delta.x), moves, lateralMove)
    } else {
        move(abs(delta.x), moves, lateralMove)
        move(abs(delta.y), moves, verticalMove)
    }

    moves.add('A')

    return moves.joinToString("")
}


private fun move(n: Int, moves: MutableList<Char>, move: Char) {
    repeat(n) {
        moves.add(move)
    }
}


fun main() {
    val input = readInput("day21/input")

    println(input)


    val p1 = input.sumOf {
        println(it)

        val doorRobot = solve(NUMBER_PAD, ::navigateNumberPad, it)
        println(doorRobot)

        val directionalRobot1 = solve(DIRECTION_PAD, ::navigateDirectionPad, doorRobot)
        println(directionalRobot1)

        val directionalRobot2 = solve(DIRECTION_PAD, ::navigateDirectionPad, directionalRobot1)
        println(directionalRobot2)


        it.dropLast(1).toInt() * directionalRobot2.length
    }

    println(p1)
}

fun solve(pad: Map<Char, Vec2>, movementFunction: (Vec2, Vec2) -> String, requiredInput: String): String {
    var currentPosition = pad.getValue('A')

    val result = requiredInput.map {
        val target = pad.getValue(it)

        val moves = movementFunction(currentPosition, target)
        currentPosition = target
        moves
    }

    return result.joinToString("")
}
