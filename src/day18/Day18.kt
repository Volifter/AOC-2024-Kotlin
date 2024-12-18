package day18

import Day
import utils.Coords2D

data class Field(val allObstacles: List<Coords2D>) {
    val isSmall = allObstacles.size == 25

    val fieldSize = if (isSmall) Coords2D(7, 7) else Coords2D(71, 71)

    val defaultSteps = if (isSmall) 12 else 1024

    fun getNeighbors(
        positions: Set<Coords2D>,
        obstacles: Set<Coords2D>,
        visited: Set<Coords2D>
    ) = sequence<Coords2D> {
        positions.forEach { position ->
            Coords2D.directionsStraight.forEach { direction ->
                val newPosition = position + direction

                if (
                    newPosition !in obstacles
                    && newPosition !in visited
                    && newPosition in fieldSize
                )
                    yield(newPosition)
            }
        }
    }

    fun solve(steps: Int = defaultSteps): Int? {
        val obstacles = allObstacles.take(steps).toSet()
        val visited = mutableSetOf(Coords2D(0, 0))
        val target = fieldSize - Coords2D(1, 1)
        var stack = setOf(Coords2D(0, 0))
        var i = 1

        while (stack.isNotEmpty()) {
            stack = buildSet {
                getNeighbors(stack, obstacles, visited).forEach {
                    if (it == target)
                        return i

                    visited.add(it)
                    add(it)
                }
            }
            i++
        }

        return null
    }

    fun findBlockingPosition(): Coords2D? {
        val obstacles = allObstacles.toMutableSet()
        val visited = mutableSetOf(Coords2D(0, 0))
        val target = fieldSize - Coords2D(1, 1)
        var stack = setOf(Coords2D(0, 0))
        val index = allObstacles.withIndex().reversed().find { (_, obstacle) ->
            while (stack.isNotEmpty()) {
                stack = buildSet {
                    getNeighbors(stack, obstacles, visited).forEach {
                        if (it == target)
                            return@find true

                        visited.add(it)
                        add(it)
                    }
                }
            }

            obstacles.remove(obstacle)

            if (Coords2D.directionsStraight.any { direction ->
                obstacle + direction in visited
            }) {
                stack = setOf(obstacle)
                visited.add(obstacle)
            }

            false
        }!!.index

        return allObstacles[index + 1]
    }
}

class Day18: Day<String>() {
    fun parseInput(input: List<String>): Field =
        Field(input.map {
            val (x, y) = it.split(",").map(String::toInt)

            Coords2D(x, y)
        })

    override fun solvePart1(input: List<String>): String =
        "" + parseInput(input).solve()

    override fun solvePart2(input: List<String>): String =
        parseInput(input).findBlockingPosition()!!.run { "$x,$y" }
}

fun main() = Day18().run {
    runPart(1, "22")
    runPart(2, "6,1")
}
