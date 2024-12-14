package day14

import Day
import utils.Coords2D

val robotRegex = """^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""".toRegex()

val christmasTree = listOf(
    "*******************************",
    "*.............................*",
    "*.............................*",
    "*.............................*",
    "*.............................*",
    "*..............*..............*",
    "*.............***.............*",
    "*............*****............*",
    "*...........*******...........*",
    "*..........*********..........*",
    "*............*****............*",
    "*...........*******...........*",
    "*..........*********..........*",
    "*.........***********.........*",
    "*........*************........*",
    "*..........*********..........*",
    "*.........***********.........*",
    "*........*************........*",
    "*.......***************.......*",
    "*......*****************......*",
    "*........*************........*",
    "*.......***************.......*",
    "*......*****************......*",
    "*.....*******************.....*",
    "*....*********************....*",
    "*.............***.............*",
    "*.............***.............*",
    "*.............***.............*",
    "*.............................*",
    "*.............................*",
    "*.............................*",
    "*.............................*",
    "*******************************"
)

val christmasTreeOffsets = buildSet {
    Coords2D.iterateOnField(christmasTree).forEach { position ->
        if (christmasTree[position.y][position.x] == '*')
            add(position)
    }
}

data class Robot(var position: Coords2D, val velocity: Coords2D) {
    fun move(n: Int, size: Coords2D) {
        position = (position + velocity * n).wrapIn(size)
    }

    fun getQuadrant(size: Coords2D): Int? {
        val mid = size / 2

        if (position.x == mid.x || position.y == mid.y)
            return null

        return (
            (if (position.y < mid.y) 1 else 0) shl 1
            or (if (position.x < mid.x) 1 else 0)
        )
    }
}

class Day14: Day<Int>() {
    var isTest = true

    fun parseInput(input: List<String>): List<Robot> = input.map { line ->
        robotRegex
            .matchEntire(line)!!
            .groupValues
            .drop(1)
            .map { it.toInt() }
            .let { (pX, pY, vX, vY) ->
                Robot(Coords2D(pX, pY), Coords2D(vX, vY))
            }
    }

    override fun solvePart1(input: List<String>): Int {
        val size = if (isTest) Coords2D(11, 7) else Coords2D(101, 103)
        val quadrants = parseInput(input).mapNotNull { robot ->
            robot.move(100, size)
            robot.getQuadrant(size)
        }

        isTest = !isTest

        return quadrants
            .groupingBy { it }
            .eachCount()
            .values
            .reduce(Int::times)
    }

    override fun solvePart2(input: List<String>): Int {
        val robots = parseInput(input)

        return generateSequence(setOf<Coords2D>()) {
            buildSet {
                robots.forEach {
                    it.move(1, Coords2D(101, 103))
                    add(it.position)
                }
            }
        }.indexOfFirst { positions ->
            positions.any { position ->
                christmasTreeOffsets.all {
                    position + it in positions
                }
            }
        }
    }
}

fun main() = Day14().run {
    runPart(1, 12)
    runPart(2)
}
