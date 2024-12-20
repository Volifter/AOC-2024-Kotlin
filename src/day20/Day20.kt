package day20

import Day
import utils.Coords2D
import kotlin.math.absoluteValue

data class Field(
    val size: Coords2D,
    val obstacles: Set<Coords2D>,
    val startPosition: Coords2D,
    val endPosition: Coords2D
) {
    fun getDistances(from: Coords2D): Map<Coords2D, Int> {
        val fieldSize = size
        var stack = setOf(from)
        val distances = mutableMapOf(from to 0)
        var i = 1

        while (stack.isNotEmpty()) {
            stack = buildSet {
                stack.forEach { position ->
                    Coords2D.directionsStraight.forEach { direction ->
                        val newPosition = position + direction

                        if (
                            newPosition in fieldSize
                            && newPosition !in obstacles
                            && newPosition !in distances
                        ) {
                            distances[newPosition] = i
                            add(newPosition)
                        }
                    }
                }
            }
            i++
        }

        return distances
    }

    fun getOffsets(size: Int): List<Coords2D> =
        (-size..size).flatMap { y ->
            val n = size - y.absoluteValue

            (-n..n).map { x -> Coords2D(x, y) }
        }

    fun solve(minSavedDistance: Int, maxShortcutLength: Int): Int {
        val startDistances = getDistances(startPosition)
        val endDistances = getDistances(endPosition)
        val offsets = getOffsets(maxShortcutLength)
        val maxDistance = startDistances[endPosition]!! - minSavedDistance

        return startDistances.entries.sumOf { (startPosition, startDistance) ->
            offsets.count { offset ->
                endDistances[startPosition + offset]?.let { endDistance ->
                    (
                        startDistance
                        + endDistance
                        + offset.manhattanDelta
                    ) <= maxDistance
                } == true
            }
        }
    }
}

class Day20: Day<Int>() {
    fun parseInput(input: List<String>): Field {
        val size = Coords2D(input.first().length, input.size)
        var startPosition: Coords2D? = null
        var endPosition: Coords2D? = null
        val obstacles = buildSet {
            Coords2D.iterateOnField(input).forEach { coords ->
                when (input[coords.y][coords.x]) {
                    '#' -> add(coords)
                    'S' -> startPosition = coords
                    'E' -> endPosition = coords
                }
            }
        }

        return Field(size, obstacles, startPosition!!, endPosition!!)
    }

    override fun solvePart1(input: List<String>): Int =
        parseInput(input).run { solve(if (size.x == 15) 1 else 100, 2) }

    override fun solvePart2(input: List<String>): Int =
        parseInput(input).run { solve(if (size.x == 15) 50 else 100, 20) }
}

fun main() = Day20().run {
    runPart(1, 44)
    runPart(2, 285)
}
