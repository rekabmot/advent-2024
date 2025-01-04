package day23

import utils.readInput

fun main() {
    val connections = mutableMapOf<String, MutableSet<String>>()

    readInput("day23/input").forEach {
        val (from, to) = it.split("-")

        connections.getOrPut(from) { mutableSetOf() }.add(to)
        connections.getOrPut(to) { mutableSetOf() }.add(from)
    }

    val p1 = connections.asSequence().map { (current, neighbours) ->
        neighbours.map { neighbour ->
            val x = connections.getValue(neighbour)

            val a = x.filter { neighbours.contains(it) }.map { listOf(current, neighbour, it) }
            a
        }
    }.flatten().flatten().map { it.sorted() }.distinct().filter { triple -> triple.any { it.startsWith("t") } }.toList()

    println(p1.size)

    // Bron-Kerbosch Algorithm
    fun explore(currentSet: Set<String>, candidateSet: List<String>, exclusionSet: List<String>): Set<String> {
        if (candidateSet.isEmpty() && exclusionSet.isEmpty()) return currentSet

        val candidates = candidateSet.toMutableSet()
        val exclusions = exclusionSet.toMutableSet()

        return candidateSet.map { c ->
            val neighbours = connections.getValue(c)

            val r = explore(currentSet + c, candidates.filter { neighbours.contains(it) }, exclusions.filter { neighbours.contains(it) })
            candidates.remove(c)
            exclusions.add(c)
            r
        }.maxByOrNull { it.size } ?: setOf()
    }

    val p2 = explore(setOf(), connections.keys.toList(), listOf())
    println(p2.sorted().joinToString(","))
}
