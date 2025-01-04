package day24

import utils.readInput


abstract class Rule(val input1: String, val input2: String, val output: String) {
    fun canApply(wires: MutableMap<String, Int>) = wires.containsKey(input1) && wires.containsKey(input2)
    abstract fun apply(wires: MutableMap<String, Int>);
}

class And(input1: String, input2: String, output: String) : Rule(input1, input2, output) {
    override fun apply(wires: MutableMap<String, Int>) {
        wires[output] = wires.getValue(input1) and wires.getValue(input2)
    }
}

class Xor(input1: String, input2: String, output: String) : Rule(input1, input2, output) {
    override fun apply(wires: MutableMap<String, Int>) {
        wires[output] = wires.getValue(input1) xor wires.getValue(input2)
    }
}

class Or(input1: String, input2: String, output: String) : Rule(input1, input2, output) {
    override fun apply(wires: MutableMap<String, Int>) {
        wires[output] = wires.getValue(input1) or wires.getValue(input2)
    }
}

val REGEX = "(\\w+) (AND|OR|XOR) (\\w+) -> (\\w+)".toRegex()

fun main() {
    val input = readInput("day24/input")

    val wires = input.subList(0, input.indexOf("")).associateTo(mutableMapOf()) {
        val (k, v) = it.split(": ")
        k to v.toInt()
    }

    val rules = input.subList(input.indexOf("") + 1, input.size).map {
        val (input1, operand, input2, output) = REGEX.matchEntire(it)!!.destructured

        when(operand) {
            "AND" -> And(input1, input2, output)
            "XOR" -> Xor(input1, input2, output)
            "OR" -> Or(input1, input2, output)
            else -> { throw Error("Not a valid operand")}
        }
    }.toMutableList()

    while (rules.isNotEmpty()) {
        val rule = rules.indexOfFirst { it.canApply(wires) }
        rules.removeAt(rule).apply(wires)
    }

    val filtered = wires.filter { it.key.startsWith("z") }

    val p1 = filtered.keys
        .sortedWith { a, b -> b.drop(1).toInt() - a.drop(1).toInt() }
        .map { wires.getValue(it) }.joinToString("")

    println(p1.toLong(2))

}