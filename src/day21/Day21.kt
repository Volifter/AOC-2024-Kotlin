package day21

import Day
import utils.Coords2D
import utils.asProgressing

data class Keypad(val rows: List<String>) {
    val directions = mapOf(
        '>' to Coords2D(1, 0),
        '<' to Coords2D(-1, 0),
        'v' to Coords2D(0, 1),
        '^' to Coords2D(0, -1),
        'A' to Coords2D(0, 0)
    )

    val keyPositions = Coords2D.iterateOnField(rows)
        .mapNotNull { position ->
            (rows[position.y][position.x] to position)
                .takeIf { it.first != ' ' }
        }
        .toMap()

    val validPositions = keyPositions.values.toSet()

    val paths: Map<Pair<Char, Char>, String> = buildMap {
        keyPositions.forEach { (keyFrom, positionFrom) ->
            keyPositions.forEach { (keyTo, positionTo) ->
                this[keyFrom to keyTo] = (
                    getPathsByDelta(positionTo - positionFrom)
                        .first { path -> isValidPath(positionFrom, path) }
                )
            }
        }
    }

    fun getPathsByDelta(delta: Coords2D): Sequence<String> = sequence {
        val vertical = (
            ">".repeat(maxOf(0, delta.x))
            + "<".repeat(maxOf(0, -delta.x))
        )
        val horizontal = (
            "v".repeat(maxOf(0, delta.y))
            + "^".repeat(maxOf(0, -delta.y))
        )

        if (delta.x > 0)
            yield(horizontal + vertical + "A")
        if (delta.y < 0)
            yield(vertical + horizontal + "A")
        if (delta.y > 0)
            yield(vertical + horizontal + "A")
        if (delta.x < 0)
            yield(horizontal + vertical + "A")

        yield("A")
    }

    fun isValidPath(startPosition: Coords2D, path: String): Boolean {
        val dirs = path.asSequence().asProgressing()

        return generateSequence(startPosition) { position ->
            dirs.firstOrNull()?.let {
                position + directions[it]!!
            }
        }.all { it in validPositions }
    }
}

class Day21: Day<Long>() {
    val digitsKeypad = Keypad(listOf(
        "789",
        "456",
        "123",
        " 0A"
    ))

    val arrowsKeypad = Keypad(listOf(
        " ^A",
        "<v>"
    ))

    fun getPairsFromPath(path: String): List<Pair<Char, Char>> =
        "A$path"
            .windowed(2)
            .map { it.first() to it.last() }

    fun getCodeLength(keypadsCount: Int, code: String): Long {
        val initialFrequencies = getPairsFromPath(code)
            .joinToString("") { pair -> digitsKeypad.paths[pair]!! }
            .let { getPairsFromPath(it) }
            .groupingBy { it }
            .eachCount()
            .entries
            .associate { (k, v) -> k to v.toLong() }

        return generateSequence(initialFrequencies) { frequencies ->
            buildMap {
                frequencies.forEach { (pair, frequency) ->
                    val path = arrowsKeypad.paths[pair]!!

                    getPairsFromPath(path).forEach {
                        set(it, (this[it] ?: 0) + frequency)
                    }
                }
            }
        }.drop(keypadsCount).first().values.sum()
    }

    fun solve(input: List<String>, count: Int): Long =
        input.sumOf { code ->
            getCodeLength(count, code) * code.dropLast(1).toInt()
        }

    override fun solvePart1(input: List<String>): Long = solve(input, 2)

    override fun solvePart2(input: List<String>): Long = solve(input, 25)
}

fun main() = Day21().run {
    runPart(1, 126384L)
    runPart(2, 154115708116294L)
}
