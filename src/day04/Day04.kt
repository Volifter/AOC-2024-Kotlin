package day04

import Day
import utils.Coords2D

class Day04: Day<Int>() {
    fun hasWord(
        input: List<String>,
        size: Coords2D,
        word: String,
        start: Coords2D,
        direction: Coords2D
    ): Boolean =
        word
            .asSequence()
            .zip(generateSequence(start) { it + direction })
            .all { (letter, position) ->
                size.contains(position)
                && letter == input[position.y][position.x]
            }

    fun getWordsInField(
        field: List<String>,
        directions: List<Coords2D>,
        word: String
    ) = sequence {
        val size = Coords2D(field.first().length, field.size)

        Coords2D.iterateOnField(field).forEach { position ->
            directions.forEach { direction ->
                if (hasWord(field, size, word, position, direction))
                    yield(position to direction)
            }
        }
    }

    val part1Word = "XMAS"

    override fun solvePart1(input: List<String>): Int =
        getWordsInField(input, Coords2D.directions, part1Word)
            .count()

    val part2Word = "MAS"

    override fun solvePart2(input: List<String>): Int =
        getWordsInField(input, Coords2D.directionsDiagonal, part2Word)
            .map { (position, direction) ->
                position + direction * (part2Word.length / 2)
            }
            .groupingBy { it }
            .eachCount()
            .values
            .count { it == 2 }
}

fun main() = Day04().run {
    runPart(1, 18)
    runPart(2, 9)
}
