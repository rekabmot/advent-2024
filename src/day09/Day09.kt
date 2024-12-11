package day09

import utils.readText

fun main() {
    val input = readText("day09/input").map(Character::getNumericValue)

    part1(input)
    part2(input)
}

data class Block(val id: Int?, val size: Int) {
    override fun toString(): String {
        return (0..<size).map { id ?: "." }.joinToString("")
    }
}

private fun part2(input: List<Int>) {
    val diskMap = input.mapIndexed { index, i ->
        if (index % 2 == 0) {
            Block(index / 2, i)
        } else {
            Block(null, i)
        }
    }.toMutableList()
    
    var i = diskMap.size - 1

    while (i >= 0) {
        if (diskMap[i].id != null) {
            val currentBlock = diskMap[i]
            val spaceRequired = currentBlock.size

            val spaceIndex = diskMap.indexOfFirst { it.id == null && it.size >= spaceRequired }

            if (spaceIndex != -1 && spaceIndex < i) {
                val spaceSize = diskMap[spaceIndex].size
                diskMap[i] = Block(null, currentBlock.size)
                diskMap[spaceIndex] = Block(currentBlock.id, currentBlock.size)
                diskMap.add(spaceIndex + 1, Block(null, spaceSize - spaceRequired))
            }
        }

        i--
    }

    val p2 = diskMap.map { block -> (0..<block.size).map { block.id ?: 0 } }.flatten().mapIndexed { index, file -> (index * file).toLong() }.sum()
    println(p2)
}

private fun part1(input: List<Int>) {
    val output = mutableListOf<Long>()

    var i = 0
    var j = input.size + 1
    var jRemaining = 0

    while (i < j) {
        if (i % 2 == 0) {
            val size = input[i]

            repeat(size) {
                output.add((i / 2).toLong())
            }
        } else {
            var gapRemaining = input[i]

            while (gapRemaining > 0) {
                if (jRemaining == 0) {
                    j -= 2
                    jRemaining = input[j]
                }
                output.add((j / 2).toLong())
                jRemaining--
                gapRemaining--
            }
        }

        i++
    }

    repeat(jRemaining) {
        output.add(((i + 1) / 2).toLong())
    }

    val p1 = output.mapIndexed { index, file -> index * file }.sum()
    println(p1)
}