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

    val p2 = explore(setOf(), connections.keys, setOf(), connections)

    println(p2.sorted().joinToString(","))
}

// Bron-Kerbosch Algorithm
fun explore(currentSet: Set<String>, candidateSet: Set<String>, exclusionSet: Set<String>, connections: Map<String, Set<String>>): Set<String> {
    if (candidateSet.isEmpty() && exclusionSet.isEmpty()) return currentSet

    val candidates = candidateSet.toMutableSet()
    val exclusions = exclusionSet.toMutableSet()

    return candidateSet.map {
        val neighbours = connections.getValue(it)

        val r = explore(currentSet + setOf(it), candidates.filter { c -> neighbours.contains(c) }.toSet(), exclusions.filter { x -> neighbours.contains(x) }.toSet(), connections)
        candidates.remove(it)
        exclusions.add(it)
        r
    }.maxByOrNull { it.size } ?: setOf()
}

