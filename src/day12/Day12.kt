package day12

import Day
import utils.Coords2D

class Day12: Day<Int>() {
    fun getArea(
        field: List<String>,
        size: Coords2D,
        startingPosition: Coords2D
    ): Set<Coords2D> {
        var stack = setOf<Coords2D>(startingPosition)
        val name = field[startingPosition.y][startingPosition.x]

        return buildSet<Coords2D> result@{
            add(startingPosition)

            while (!stack.isEmpty()) {
                stack = buildSet stack@{
                    stack.forEach { position ->
                        Coords2D.directionsStraight.forEach { direction ->
                            val newPosition = position + direction

                            if (
                                newPosition in size
                                && newPosition !in this@result
                                && field[newPosition.y][newPosition.x] == name
                            ) {
                                this@result.add(newPosition)
                                this@stack.add(newPosition)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getAreas(input: List<String>): List<Set<Coords2D>> {
        val size = Coords2D(input.first().length, input.size)
        val visited = mutableSetOf<Coords2D>()

        return buildList<Set<Coords2D>> {
            Coords2D.iterateOnField(input).forEach { position ->
                if (position !in visited) {
                    val area = getArea(input, size, position)

                    visited.addAll(area)
                    add(area)
                }
            }
        }
    }

    fun getAreaPerimeter(area: Set<Coords2D>): Int =
        area.sumOf { position ->
            Coords2D.directionsStraight.count { position + it !in area }
        }

    val corners = Coords2D.directionsStraight.zip(
        Coords2D.directionsStraight.drop(1)
        + listOf(Coords2D.directionsStraight[0])
    )

    fun getSidesCount(area: Set<Coords2D>): Int =
        area.sumOf { position ->
            corners.count { (a, b) ->
                val hasNeighborA = position + a in area
                val hasNeighborB = position + b in area

                (
                    (!hasNeighborA && !hasNeighborB)
                    || (
                        hasNeighborA
                        && hasNeighborB
                        && position + a + b !in area
                    )
                )
            }
        }

    override fun solvePart1(input: List<String>): Int =
        getAreas(input).sumOf { area -> area.size * getAreaPerimeter(area) }

    override fun solvePart2(input: List<String>): Int =
        getAreas(input).sumOf { area -> area.size * getSidesCount(area) }
}

fun main() = Day12().run {
    runPart(1, 140, 772, 1930, 692, 1184)
    runPart(2, 80, 436, 1206, 236, 368)
}
