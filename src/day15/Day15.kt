package day15

import Day
import utils.Coords2D
import utils.groupLines

open class Box(
    var position: Coords2D,
    val boxes: MutableMap<Coords2D, Box>,
    val walls: Set<Coords2D>,
) {
    open val positions get() = listOf(position)

    open fun getMovePositions(direction: Coords2D): List<Coords2D> =
        listOf(position + direction)

    fun canMove(direction: Coords2D): Boolean =
        getMovePositions(direction).all {
            it !in walls
            && boxes[it]?.canMove(direction) != false
        }

    fun move(direction: Coords2D): Boolean {
        if (!canMove(direction))
            return false

        getMovePositions(direction).forEach { boxes[it]?.move(direction) }

        positions.forEach { boxes.remove(it) }
        position += direction
        positions.forEach { boxes[it] = this }

        return true
    }
}

class DoubleBox(
    position: Coords2D,
    boxes: MutableMap<Coords2D, Box>,
    walls: Set<Coords2D>
): Box(position, boxes, walls) {
    override val positions get() = listOf(position, position + Coords2D(1, 0))

    override fun getMovePositions(direction: Coords2D): List<Coords2D> =
        when (direction) {
            Coords2D(1, 0) -> listOf(position + Coords2D(2, 0))
            Coords2D(0, 1) -> listOf(
                position + Coords2D(0, 1),
                position + Coords2D(1, 1)
            )
            Coords2D(-1, 0) -> listOf(position + Coords2D(-1, 0))
            Coords2D(0, -1) -> listOf(
                position + Coords2D(0, -1),
                position + Coords2D(1, -1)
            )
            else -> throw IllegalStateException("Invalid direction: $direction")
        }
}

class Warehouse(
    var position: Coords2D,
    val boxes: Map<Coords2D, Box>,
    val walls: Set<Coords2D>
) {
    fun move(direction: Coords2D): Boolean {
        val newPosition = position + direction

        if (
            newPosition in walls
            || boxes[newPosition]?.move(direction) == false
        )
            return false

        position = newPosition

        return true
    }
}

class Day15: Day<Int>() {
    fun parseInput(
        input: List<String>,
        isDouble: Boolean = false,
    ): Pair<Warehouse, List<Coords2D>> {
        val (field, path) = groupLines(input)
        var position: Coords2D? = null
        val boxes = mutableMapOf<Coords2D, Box>()
        val walls = mutableSetOf<Coords2D>()

        Coords2D.iterateOnField(field).forEach { coords ->
            val c = field[coords.y][coords.x]

            val allPositions = if (isDouble)
                listOf(
                    Coords2D(coords.x * 2, coords.y),
                    Coords2D(coords.x * 2 + 1, coords.y)
                )
            else
                listOf(coords)

            when (c) {
                '#' -> walls.addAll(allPositions)
                'O' -> (
                    if (isDouble)
                        DoubleBox(allPositions.first(), boxes, walls)
                    else
                        Box(allPositions.first(), boxes, walls)
                ).let { box ->
                    allPositions.forEach { boxes[it] = box }
                }
                '@' -> position = allPositions.first()
            }
        }

        return Warehouse(
            position!!,
            boxes,
            walls
        ) to path.joinToString("").map {
            when (it) {
                '>' -> Coords2D(1, 0)
                'v' -> Coords2D(0, 1)
                '<' -> Coords2D(-1, 0)
                '^' -> Coords2D(0, -1)
                else -> throw IllegalStateException(
                    "Invalid path character: $it"
                )
            }
        }
    }

    fun solve(input: List<String>, isDouble: Boolean = false): Int {
        val (field, path) = parseInput(input, isDouble)

        path.forEach { field.move(it) }

        return field.boxes.values
            .toSet()
            .map { it.position }
            .sumOf { it.y * 100 + it.x }
    }

    override fun solvePart1(input: List<String>): Int = solve(input)

    override fun solvePart2(input: List<String>): Int = solve(input, true)
}

fun main() = Day15().run {
    runPart(1, 10092, 2028)
    runPart(2, 9021)
}
