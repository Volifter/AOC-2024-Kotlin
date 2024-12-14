package day13

import Day
import utils.Coords2D
import utils.groupLines
import kotlin.math.roundToLong

data class ClawMachine(
    val buttonA: Coords2D,
    val buttonB: Coords2D,
    val prize: Coords2D
) {
    fun solve(offset: Long, max: Long?): Long {
        val aX = buttonA.x.toLong()
        val aY = buttonA.y.toLong()
        val bX = buttonB.x.toLong()
        val bY = buttonB.y.toLong()
        val pX = prize.x + offset
        val pY = prize.y + offset

        if (aX == 0L)
            return 0

        val nom = 1.0 * aY * pX / aX - pY
        val den = 1.0 * aY * bX / aX - bY

        if (den == 0.0)
            return 0

        val b = (nom / den).roundToLong()
        val a = (pX - bX * b) / aX

        if (
            max !== null && (a > max || b > max)
            || aX * a + bX * b != pX
            || aY * a + bY * b != pY
        )
            return 0

        return a * 3 + b
    }
}

val buttonRegex = """^Button [AB]: X\+(\d+), Y\+(\d+)$""".toRegex()
val prizeRegex = """^Prize: X=(\d+), Y=(\d+)$""".toRegex()

class Day13: Day<Long>() {
    fun parseInput(input: List<String>): List<ClawMachine> =
        groupLines(input)
            .map { (buttonALine, buttonBLine, prizeLine) ->
                check(buttonALine.startsWith("Button A:"))
                check(buttonBLine.startsWith("Button B:"))

                val coords = listOf(buttonALine, buttonBLine, prizeLine)
                    .zip(listOf(buttonRegex, buttonRegex, prizeRegex))
                    .map { (line, regex) ->
                         regex
                            .find(line)!!
                            .groupValues
                            .drop(1)
                            .map { it.toInt() }
                            .let { (x, y) -> Coords2D(x, y) }
                    }

                ClawMachine(coords[0], coords[1], coords[2])
            }

    override fun solvePart1(input: List<String>): Long =
        parseInput(input).sumOf { it.solve(0, 100) }

    override fun solvePart2(input: List<String>): Long =
        parseInput(input).sumOf { it.solve(10000000000000, null) }
}

fun main() = Day13().run {
    runPart(1, 480)
    runPart(2, 875318608908)
}
