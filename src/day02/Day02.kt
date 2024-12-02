package day02

import Day
import utils.withoutIndex

class Day02: Day<Int>() {
    fun isSafeReport(ints: List<Int>, maxErrors: Int): Boolean =
        isSafeReport(ints, 1..3, maxErrors)
        || isSafeReport(ints, -3..-1, maxErrors)

    fun isSafeReport(
        ints: List<Int>,
        range: IntRange,
        maxErrors: Int
    ): Boolean = when (ints.size) {
        1 -> true
        2 -> maxErrors > 0 || ints[1] - ints[0] in range
        else -> when {
            ints[1] - ints[0] !in range ->
                maxErrors > 0 && (
                    isSafeReport(ints.drop(1), range, maxErrors - 1)
                    || isSafeReport(ints.withoutIndex(1), range, maxErrors - 1)
                )
            ints[2] - ints[1] !in range ->
                maxErrors > 0 && (
                    isSafeReport(ints.withoutIndex(1), range, maxErrors - 1)
                    || isSafeReport(ints.withoutIndex(2), range, maxErrors - 1)
                )
            else -> isSafeReport(ints.drop(1), range, maxErrors)
        }
    }

    fun parseLine(line: String): List<Int> = line.split(" ").map { it.toInt() }

    fun solve(input: List<String>, maxErrors: Int = 0): Int =
        input.count { isSafeReport(parseLine(it), maxErrors) }

    override fun solvePart1(input: List<String>): Int = solve(input)

    override fun solvePart2(input: List<String>): Int = solve(input ,1)
}

fun main() = Day02().run {
    runPart(1, 2)
    runPart(2, 4)
}
