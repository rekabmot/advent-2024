package day17

import utils.readInput
import kotlin.math.pow

const val DEFAULT_MOVE = 2

class Computer(private var a: Long, private var b: Long, private var c: Long, val input: List<Long>) {
    private var instuctionPointer = 0

    private val instructions = mapOf(
        0L to { operand: Long -> adv(resolveComboOperand(operand)) },
        1L to { operand: Long -> bxl(operand.toLong()) },
        2L to { operand: Long -> bst(resolveComboOperand(operand)) },
        3L to { operand: Long -> jnz(operand.toLong()) },
        4L to { _: Long -> bxc() },
        5L to { operand: Long -> out(resolveComboOperand(operand)) },
        6L to { operand: Long -> bdv(resolveComboOperand(operand)) },
        7L to { operand: Long -> cdv(resolveComboOperand(operand)) }
    )

    fun run() {
        while (instuctionPointer < input.size) {
            val result = instructions.getValue(input[instuctionPointer]).invoke(input[instuctionPointer + 1])
            instuctionPointer += result
        }
        println()
        println(toString())
        println()
    }

    private fun resolveComboOperand(operand: Long): Long = when(operand) {
        0L, 1L, 2L, 3L -> operand
        4L -> a
        5L -> b
        6L -> c
        else -> {
            println("$operand !!!!!!")
            -1
        }
    }

    private fun adv(operand: Long): Int {
        a = a shr operand.toInt()
        return DEFAULT_MOVE
    }

    private fun bxl(operand: Long): Int {
        b = b xor operand
        return DEFAULT_MOVE
    }

    private fun bst(operand: Long): Int {
        b = operand.mod(8).toLong()
        return DEFAULT_MOVE
    }

    private fun jnz(operand: Long): Int {
        if (a != 0L) {
            instuctionPointer = operand.toInt()
            return 0
        } else {
            return DEFAULT_MOVE
        }
    }

    private fun bxc(): Int {
        b = b xor c
        return DEFAULT_MOVE
    }

    private fun out(operand: Long): Int {
        print("${operand.mod(8)},")
        return DEFAULT_MOVE
    }

    private fun bdv(operand: Long): Int {
        b = a shr operand.toInt()
        return DEFAULT_MOVE
    }

    private fun cdv(operand: Long): Int {
        c = a shr operand.toInt()
        return DEFAULT_MOVE
    }

    override fun toString(): String {
        return "A: $a, B: $b, C: $c"
    }
}

fun main() {
    val input = readInput("day17/input")
        .filter { it.isNotEmpty() }
        .map { it.split(": ")[1] }

    Computer(0, 0, 9, listOf(2, 6)).run() // Set B to 1
    Computer(10, 0, 0, listOf(5, 0, 5, 1, 5, 4)).run() // Print 0,1,2
    Computer(2024, 0, 0, listOf(0, 1, 5, 4, 3, 0)).run() // Print 4,2,5,6,7,7,7,7,3,1,0, leave 0 in A
    Computer(0, 29, 0, listOf(1, 7)).run() // Set B to 26
    Computer(0, 2024, 43690, listOf(4, 0)).run() // Set B to 44354


    val computer = Computer(input[0].toLong(), input[1].toLong(), input[2].toLong(), input[3].split(",").map { it.toLong() })
    computer.run()
}