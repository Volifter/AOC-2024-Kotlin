package day25

import Day
import utils.groupLines

class Day25: Day<Int>() {
    fun parseInput(
        input: List<String>
    ): Pair<List<List<Int>>, List<List<Int>>> {
        var topKeys = mutableListOf<List<Int>>()
        var bottomKeys = mutableListOf<List<Int>>()

        groupLines(input).forEach { lines ->
            val keys = if ('#' in lines.first()) topKeys else bottomKeys

            keys.add(
                lines.first().indices.map { i -> lines.count { it[i] == '#' }}
            )
        }

        return topKeys to bottomKeys
    }

    override fun solvePart1(input: List<String>): Int {
        val (topKeys, bottomKeys) = parseInput(input)

        return topKeys.sumOf { topKey ->
            bottomKeys.count { bottomKey ->
                topKey.zip(bottomKey).all { (a, b) -> a + b <= 7 }
            }
        }
    }
}

fun main() = Day25().run {
    runPart(1, 3)
}
