package day07

import Day
import kotlin.math.floor

class Day07: Day<Long>() {
    fun parseInput(input: List<String>): List<Pair<Long, List<Long>>> =
        input.map { line ->
            val (left, right) = line.split(": ")

            left.toLong() to right.split(" ").map {
                it.toLong().also { check(it >= 0) }
            }
        }

    fun canBeSolved(
        target: Long,
        numbers: List<Long>,
        withConcatenation: Boolean = false
    ): Boolean {
        val last = numbers.last()

        if (numbers.size == 1)
            return target == last

        val head = numbers.dropLast(1)

        return (
                target >= last
                && canBeSolved(target - last, head, withConcatenation)
            )
            || (
                last > 0L
                && target % last == 0L
                && canBeSolved(target / last, head, withConcatenation)
            )
            || (
                withConcatenation
                && Math.pow(
                    10.0,
                    floor(Math.log10(last * 1.0)) + 1.0
                ).toLong().let { pow10 ->
                    target % pow10 == last
                    && canBeSolved(target / pow10, head, withConcatenation)
                }
            )
    }

    fun solve(input: List<String>, withConcatenation: Boolean = false): Long =
        parseInput(input).sumOf { (target, numbers) ->
            if (canBeSolved(target, numbers, withConcatenation)) target else 0L
        }

    override fun solvePart1(input: List<String>): Long = solve(input)

    override fun solvePart2(input: List<String>): Long = solve(input, true)
}

fun main() = Day07().run {
    runPart(1, 3749)
    runPart(2, 11387)
}
