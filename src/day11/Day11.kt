package day11

import Day
import kotlin.math.log10
import kotlin.math.pow

class Day11: Day<Long>() {
    fun expand(n: Long) = sequence {
        if (n == 0L)
            return@sequence yield(1L)

        val length = log10(n * 1.0).toLong() + 1L

        if (length and 1L == 1L)
            return@sequence yield(n * 2024L)

        val pow10 = 10.0.pow(length / 2.0).toLong()

        yield(n / pow10)
        yield(n % pow10)
    }

    fun solve(input: List<String>, iterations: Int): Long {
        val numbers = input[0].split(" ").map { it.toLong() }
        var frequencies = numbers
            .groupingBy { it }
            .eachCount()
            .entries
            .associate { (k, v) -> k to v.toLong() }

        repeat(iterations) {
            frequencies = buildMap {
                frequencies.forEach {
                    (n, frequency) -> expand(n).forEach {
                        set(it, getOrDefault(it, 0L) + frequency)
                    }
                }
            }
        }

        return frequencies.values.sum()
    }

    override fun solvePart1(input: List<String>): Long = solve(input, 25)

    override fun solvePart2(input: List<String>): Long = solve(input, 75)
}

fun main() = Day11().run {
    runPart(1, 55312L)
    runPart(2, 65601038650482L)
}
