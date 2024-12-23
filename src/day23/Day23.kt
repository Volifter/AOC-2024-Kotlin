package day23

import Day

data class Computer(val name: String) {
    val neighbors = mutableSetOf<Computer>()

    fun connectWith(other: Computer) {
        neighbors.add(other)
        other.neighbors.add(this)
    }
}

class Day23: Day<String>() {
    fun parseInput(input: List<String>): Map<String, Computer> {
        val computers = mutableMapOf<String, Computer>()

        input.forEach { line ->
            line.split("-")
                .map { computers.getOrPut(it) { Computer(it) } }
                .let { (computerA, computerB) ->
                    computerA.connectWith(computerB)
                }
        }

        return computers
    }

    fun findCliques(
        computers: Set<Computer>,
        maxSize: Int? = null
    ): Set<Set<Computer>> {
        var cliques: Set<Set<Computer>> = buildSet {
            computers.forEach { computer ->
                computer.neighbors.forEach { neighbor ->
                    add(setOf(computer, neighbor))
                }
            }
        }
        var size = 2

        while (cliques.size > 1 && maxSize?.let { size < it } != false) {
            cliques = buildSet {
                cliques.forEach { clique ->
                    computers.forEach { computer ->
                        if (clique.all { it in computer.neighbors })
                            add(clique + computer)
                    }
                }
            }
            size++
        }

        return cliques
    }

    override fun solvePart1(input: List<String>): String =
        findCliques(parseInput(input).values.toSet(), 3)
            .count { it.any { it.name.startsWith("t") } }
            .let { "$it" }

    override fun solvePart2(input: List<String>): String =
        findCliques(parseInput(input).values.toSet())
            .single()
            .map { it.name }
            .sorted()
            .joinToString(",")
}

fun main() = Day23().run {
    runPart(1, "7")
    runPart(2, "co,de,ka,ta")
}
