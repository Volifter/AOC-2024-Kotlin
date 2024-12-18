package day06

import Day
import utils.Coords2D

class State(
    val obstacles: Set<Coords2D>,
    val size: Coords2D,
    val position: Coords2D,
    val direction: Coords2D
) {
    val next: State? get() = (position + direction).let { nextPosition ->
        when (nextPosition) {
            !in size -> null
            !in obstacles -> State(
                obstacles,
                size,
                nextPosition,
                direction
            )
            else -> State(
                obstacles,
                size,
                position,
                when (direction) {
                    Coords2D(1, 0) -> Coords2D(0, 1)
                    Coords2D(0, 1) -> Coords2D(-1, 0)
                    Coords2D(-1, 0) -> Coords2D(0, -1)
                    Coords2D(0, -1) -> Coords2D(1, 0)
                    else -> throw IllegalStateException("invalid direction")
                }
            )
        }
    }

    val path get() = generateSequence(this) { it.next }

    fun canMoveTo(position: Coords2D): Boolean =
        position in size && !obstacles.contains(position)

    fun withObstacle(obstaclePosition: Coords2D): State = State(
        obstacles + obstaclePosition,
        size,
        position,
        direction
    )

    fun isLooped(): Boolean {
        val visited = mutableSetOf<Pair<Coords2D, Coords2D>>()

        return path.any { !visited.add(it.position to it.direction) }
    }
}

class Day06: Day<Int>() {
    fun parseInput(input: List<String>): State {
        val obstacles = mutableSetOf<Coords2D>()
        var position: Coords2D? = null
        var direction: Coords2D? = null

        Coords2D.iterateOnField(input).forEach { coords ->
            val (x, y) = coords

            when (input[y][x]) {
                '#' -> obstacles.add(coords)
                '>' -> { position = coords; direction = Coords2D(1, 0); }
                'v' -> { position = coords; direction = Coords2D(0, 1); }
                '<' -> { position = coords; direction = Coords2D(-1, 0); }
                '^' -> { position = coords; direction = Coords2D(0, -1); }
            }
        }

        return State(
            obstacles,
            Coords2D(input.first().length, input.size),
            position!!,
            direction!!
        )
    }

    override fun solvePart1(input: List<String>): Int =
        parseInput(input).path.map { it.position }.toSet().size

    override fun solvePart2(input: List<String>): Int {
        val visited = mutableSetOf<Coords2D>()

        return parseInput(input).path.count { state ->
            val obstaclePosition = state.position + state.direction

            visited.add(state.position)

            obstaclePosition !in visited
                && state.canMoveTo(obstaclePosition)
                && state.withObstacle(obstaclePosition).isLooped()
        }
    }
}

fun main() = Day06().run {
    runPart(1, 41)
    runPart(2, 6)
}
