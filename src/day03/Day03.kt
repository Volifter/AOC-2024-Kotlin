package day03

import Day

class Day03: Day<Int>() {
    val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()

    override fun solvePart1(input: List<String>): Int =
        mulRegex
            .findAll(input.joinToString("\n"))
            .sumOf { match ->
                match.groupValues[1].toInt() * match.groupValues[2].toInt()
            }

    val operationRegex =
        """(mul)\((\d+),(\d+)\)|(do)\(\)|(don't)\(\)""".toRegex()

    override fun solvePart2(input: List<String>): Int =
        operationRegex
            .findAll(input.joinToString("\n"))
            .fold(true to 0) { (isEnabled, sum), match ->
                val args = match.groupValues
                    .filter(String::isNotEmpty)
                    .drop(1)
                    .toList()

                when (args[0]) {
                    "mul" -> (
                        isEnabled to sum + if (isEnabled)
                            args[1].toInt() * args[2].toInt()
                        else
                            0
                    )
                    "don't" -> false to sum
                    "do" -> true to sum
                    else -> throw IllegalStateException(
                        "Invalid operation: ${args[0]}"
                    )
                }
            }
            .second
}

fun main() = Day03().run {
    runPart(1, 161)
    runPart(2, 48)
}
