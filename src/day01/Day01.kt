package day01

import Day
import kotlin.collections.unzip
import kotlin.math.absoluteValue

class Day01: Day<Int>() {
    val separator = """\s+""".toRegex()

    fun parseColumns(input: List<String>): Pair<List<Int>, List<Int>> = input
        .map {
            it
                .split(separator)
                .map(String::toInt)
                .let { (a, b) -> a to b }
        }
        .unzip()

    override fun solvePart1(input: List<String>): Int =
        parseColumns(input)
            .toList()
            .map { it.sorted() }
            .let { (left, right) -> left.zip(right) }
            .sumOf { (a, b) -> (a - b).absoluteValue }

    override fun solvePart2(input: List<String>): Int {
        val (left, right) = parseColumns(input)
        val frequencies = right.groupingBy { it }.eachCount()

        return left.sumOf { n -> frequencies.getOrDefault(n, 0) * n }
    }
}

fun main() = Day01().run {
    runPart(1, 11)
    runPart(2, 31)
}
