package day10

import Day
import utils.Coords2D

class Day10: Day<Int>() {
    fun parseInput(input: List<String>): List<List<Int>> = input.map { line ->
        line.toCharArray().toList().map { it.digitToInt() }
    }

    fun getTrailsScore(
        field: List<List<Int>>,
        size: Coords2D,
        startPosition: Coords2D
    ): Int {
        var score = 0
        var positions = listOf<Coords2D>(startPosition)
        val visited = mutableSetOf<Coords2D>(startPosition)

        while (positions.isNotEmpty()) {
            positions = buildList<Coords2D> {
                positions.forEach { position ->
                    val target = field[position.y][position.x] + 1

                    Coords2D.directionsStraight.forEach { direction ->
                        val newPosition = position + direction

                        if (
                            newPosition in size
                            && newPosition !in visited
                            && field[newPosition.y][newPosition.x] == target
                        ) {
                            if (target == 9)
                                score++

                            add(newPosition)
                            visited.add(newPosition)
                        }
                    }
                }
            }
        }

        return score
    }

    fun getTrailsRating(
        field: List<List<Int>>,
        size: Coords2D,
        position: Coords2D,
        visited: MutableMap<Coords2D, Int> = mutableMapOf()
    ): Int = visited.getOrPut(position) {
        val target = field[position.y][position.x] + 1

        if (target == 10)
            1
        else
            Coords2D.directionsStraight.sumOf { direction ->
                val newPosition = position + direction

                if (
                    newPosition in size
                    && field[newPosition.y][newPosition.x] == target
                )
                    getTrailsRating(field, size, newPosition, visited)
                else
                    0
            }
    }

    fun solve(
        input: List<String>,
        solver: (List<List<Int>>, Coords2D, Coords2D) -> Int
    ): Int {
        val field = parseInput(input)
        val size = Coords2D(field.first().size, field.size)

        return Coords2D.iterate(size)
            .filter { (x, y) -> field[y][x] == 0 }
            .sumOf { solver(field, size, it) }
    }

    override fun solvePart1(input: List<String>): Int =
        solve(input, ::getTrailsScore)

    override fun solvePart2(input: List<String>): Int =
        solve(input, ::getTrailsRating)
}

fun main() = Day10().run {
    runPart(1, 36)
    runPart(2, 81)
}
