package day19

import Day
import utils.groupLines

class PatternNode(
    var pattern: String? = null,
    val children: MutableMap<Char, PatternNode> = mutableMapOf()
) {
    fun addPattern(pattern: String): PatternNode {
        if (pattern.isEmpty())
            return this

        return children
            .getOrPut(pattern.first()) { PatternNode() }
            .addPattern(pattern.drop(1))
            .also { it.pattern = pattern }
    }

    fun getMatching(str: String): Sequence<String> = sequence {
        pattern?.let { yield(it) }

        children[str.firstOrNull()]?.let { child ->
            yieldAll(child.getMatching(str.drop(1)))
        }
    }
}

class Day19: Day<Long>() {
    fun parseInput(input: List<String>): Pair<PatternNode, List<String>> {
        val (patterns, towels) = groupLines(input)
        val root = PatternNode()

        patterns.single().split(", ").forEach { root.addPattern(it) }

        return root to towels
    }

    fun solve(
        towel: String,
        root: PatternNode,
        cache: MutableMap<String, Long> = mutableMapOf()
    ): Long =
        cache.getOrPut(towel) {
            if (towel.isEmpty())
                1L
            else
                root
                    .getMatching(towel)
                    .sumOf { solve(towel.drop(it.length), root, cache) }
        }

    override fun solvePart1(input: List<String>): Long {
        val (patterns, towels) = parseInput(input)

        return towels.count { solve(it, patterns) > 0 } * 1L
    }

    override fun solvePart2(input: List<String>): Long {
        val (patterns, towels) = parseInput(input)

        return towels.sumOf { solve(it, patterns) }
    }
}

fun main() = Day19().run {
    runPart(1, 6)
    runPart(2, 16)
}
