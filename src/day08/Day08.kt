package day08

import Day
import utils.Coords2D

class Day08: Day<Int>() {
    fun parseInput(input: List<String>): Pair<List<List<Coords2D>>, Coords2D> =
        buildMap {
            Coords2D.iterateOnField(input).forEach { coords ->
                val c = input[coords.y][coords.x]

                if (c != '.')
                    getOrPut(c) { mutableListOf() }.add(coords)
            }
        }.values.toList() to Coords2D(input.first().length, input.size)

    fun solve(input: List<String>, isInfinite: Boolean): Int {
        val (antennaGroups, size) = parseInput(input)

        return buildSet<Coords2D> {
            antennaGroups.forEach { positions ->
                positions.forEachIndexed { i, positionA ->
                    positions.slice(0..<i).forEach { positionB ->
                        val delta = positionB - positionA
                        val left = generateSequence(positionA) {
                            (it - delta).takeIf { it in size }
                        }
                        val right = generateSequence(positionB) {
                            (it + delta).takeIf { it in size }
                        }

                        addAll(
                            if (isInfinite)
                                (left + right)
                            else
                                (left.drop(1).take(1) + right.drop(1).take(1))
                        )
                    }
                }
            }
        }.size
    }

    override fun solvePart1(input: List<String>): Int =
        solve(input, false)

    override fun solvePart2(input: List<String>): Int =
        solve(input, true)
}

fun main() = Day08().run {
    runPart(1, 14)
    runPart(2, 34)
}
