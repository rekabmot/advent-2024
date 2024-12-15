package day13

import utils.Vec2L
import utils.readInput

val REGEX = ".*: X[+|=](\\d+), Y[+|=](\\d+)".toRegex()

data class Game(val prize: Vec2L, val aButton: Vec2L, val bButton: Vec2L)

val aCost = 3
val bCost = 1

const val P2_OFFSET = 10000000000000

fun main() {
    val p1Input = readInput("day13/input").chunked(4).map {
        val (aX, aY) = REGEX.matchEntire(it[0])!!.destructured
        val (bX, bY) = REGEX.matchEntire(it[1])!!.destructured
        val (pX, pY) = REGEX.matchEntire(it[2])!!.destructured

        Game(Vec2L(pX.toLong(), pY.toLong()), Vec2L(aX.toLong(), aY.toLong()), Vec2L(bX.toLong(), bY.toLong()))
    }

    val p1 = p1Input.map { solve(it) }.filter {it != -1L }.sum()
    println(p1)

    val p2Input = p1Input.map { Game(Vec2L(it.prize.x + P2_OFFSET, it.prize.y + P2_OFFSET), it.aButton, it.bButton) }

    val p2 = p2Input.map { solve(it) }.filter { it != -1L }.sum()

    println(p2)
}

data class Equation(val result: Long, val aCoefficient: Long, val bCoefficient: Long) {
    override fun toString(): String {
        return "$result = ${aCoefficient}a + ${bCoefficient}b"
    }
}

fun solve(game: Game): Long {
    val l = Equation(game.prize.x, game.aButton.x, game.bButton.x)
    val r = Equation(game.prize.y, game.aButton.y, game.bButton.y)

//    println(l)
//    println(r)

    // Eliminate "a"

    val l2 = Equation(l.result * r.aCoefficient, l.aCoefficient * r.aCoefficient, l.bCoefficient * r.aCoefficient)
    val r2 = Equation(r.result * l.aCoefficient, r.aCoefficient * l.aCoefficient, r.bCoefficient * l.aCoefficient)

//    println(l2)
//    println(r2)

    val result = Equation(r2.result - l2.result, 0, r2.bCoefficient - l2.bCoefficient)

    // Solve for B

    val b = result.result / result.bCoefficient

    // Substitute B into original

    val a = (l.result - (l.bCoefficient * b)) / l.aCoefficient

    if ((game.aButton * a) + (game.bButton * b) == game.prize) {
        return (a * aCost) + (b * bCost)
    } else {
        return -1
    }

//    return Pair(a, b)
}