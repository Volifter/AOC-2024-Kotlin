package day16

import Day
import utils.Coords2D
import java.util.PriorityQueue

class State(
    val position: Coords2D,
    val direction: Coords2D,
    prevPath: Set<Pair<Coords2D, Coords2D>> = emptySet(),
    val score: Int = 0,
): Comparable<State> {
    val key get() = position to direction

    var path = prevPath + key

    fun getChildren(walls: Set<Coords2D>) = sequence {
        listOf(
            direction to 1,
            Coords2D(-direction.y, direction.x) to 1001,
            Coords2D(direction.y, -direction.x) to 1001
        ).forEach { (direction, cost) ->
            val newPosition = position + direction

            if (newPosition !in walls)
                yield(
                    State(
                        newPosition,
                        direction,
                        path,
                        score + cost
                    )
                )
        }
    }

    override fun compareTo(other: State): Int = score.compareTo(other.score)
}

data class Maze(
    val walls: Set<Coords2D>,
    val startPosition: Coords2D,
    val endPosition: Coords2D
) {
    fun solve(): Int {
        var stack = PriorityQueue<State>()
        val visited = mutableSetOf<Pair<Coords2D, Coords2D>>()

        State(startPosition, Coords2D(1, 0)).let {
            stack.add(it)
            visited.add(it.key)
        }

        while (stack.isNotEmpty()) {
            val state = stack.remove()

            state.getChildren(walls).filter { it.key !in visited }.forEach {
                if (it.position == endPosition)
                    return it.score

                stack.add(it)
                visited.add(it.key)
            }
        }

        throw IllegalStateException("No path found")
    }

    fun countBestPlaces(): Int {
        var stack = PriorityQueue<State>()
        val visited = mutableMapOf<Pair<Coords2D, Coords2D>, State>()
        var bestState: State? = null

        State(startPosition, Coords2D(1, 0)).let {
            stack.add(it)
            visited[it.key] = it
        }

        while (stack.isNotEmpty()) {
            val state = stack.remove()

            if (state.position == endPosition && bestState == null)
                bestState = state

            if (bestState?.let { state.score > it.score } == true)
                return bestState.path
                    .flatMap { pos -> visited[pos]!!.path.map { it.first } }
                    .toSet()
                    .size

            state.getChildren(walls).forEach { child ->
                visited[child.key]
                    ?.let { prev ->
                        if (prev.score == child.score)
                            prev.path = prev.path + child.path
                    }
                    ?: run {
                        stack.add(child)
                        visited[child.key] = child
                    }
            }
        }

        throw IllegalStateException("No path found")
    }
}

class Day16: Day<Int>() {
    fun parseInput(input: List<String>): Maze {
        var startPosition: Coords2D? = null
        var endPosition: Coords2D? = null
        val walls = buildSet {
            Coords2D.iterateOnField(input).forEach { position ->
                when (input[position.y][position.x]) {
                    '#' -> add(position)
                    'S' -> startPosition = position
                    'E' -> endPosition = position
                }
            }
        }

        return Maze(walls, startPosition!!, endPosition!!)
    }

    override fun solvePart1(input: List<String>): Int
        = parseInput(input).solve()

    override fun solvePart2(input: List<String>): Int
        = parseInput(input).countBestPlaces()
}

fun main() = Day16().run {
    runPart(1, 7036, 11048)
    runPart(2, 45, 64)
}
