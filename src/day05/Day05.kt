package day05

import Day
import utils.groupLines

class Day05: Day<Int>() {
    fun parseInput(
        input: List<String>
    ): Pair<Set<Pair<Int, Int>>, List<List<Int>>> =
        groupLines(input).let { (rulesLines, pagesLines) ->
            Pair(
                rulesLines.map { line ->
                    line.split("|").map { it.toInt() }.let { it[0] to it[1] }
                }.toSet(),
                pagesLines.map { line ->
                    line.split(",").map { it.toInt() }
                }
            )
        }

    fun sortPages(pages: List<Int>, rulesSet: Set<Pair<Int, Int>>) =
        pages
            .withIndex()
            .sortedWith { (ai, a), (bi, b) ->
                when {
                    a to b in rulesSet -> -1
                    b to a in rulesSet -> 1
                    else -> ai - bi
                }
            }
            .map { it.value }

    fun solve(input: List<String>, takeSorted: Boolean): Int {
        val (rules, pages) = parseInput(input)

        return pages.sumOf { pages ->
            val sorted = sortPages(pages, rules)

            if (sorted == pages == takeSorted) sorted[sorted.size / 2] else 0
        }
    }

    override fun solvePart1(input: List<String>): Int = solve(input, true)

    override fun solvePart2(input: List<String>): Int = solve(input, false)
}

fun main() = Day05().run {
    runPart(1, 143)
    runPart(2, 123)
}
