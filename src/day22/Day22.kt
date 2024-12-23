package day22

import Day

class Day22: Day<Int>() {
    fun normalize(n: Long, secret: Long): Long =
        (n xor secret) % 0x1000000L

    fun getPrices(n: Int) = generateSequence(n.toLong()) { it ->
        var secret = it

        secret = normalize(secret shl 6, secret)
        secret = normalize(secret shr 5, secret)
        secret = normalize(secret shl 11, secret)

        secret
    }

    override fun solvePart1(input: List<String>): Int =
        input.sumOf { getPrices(it.toInt()).drop(2000).first().toInt() }

    override fun solvePart2(input: List<String>): Int {
        val sequencePrices = mutableMapOf<List<Int>, Int>()

        input.forEach {
            val prices = getPrices(it.toInt())
                .map { it.toInt() % 10 }
                .take(2000)
                .toList()
            val deltas = prices.zipWithNext().map { (a, b) -> b - a }
            val visited = mutableSetOf<List<Int>>()

            deltas.windowed(4).zip(prices.drop(4)).forEach { (group, price) ->
                if (visited.add(group))
                    sequencePrices[group] = (sequencePrices[group] ?: 0) + price
            }
        }

        return sequencePrices.values.max()
    }
}

fun main() = Day22().run {
    runPart(1, 37327623)
    runPart(2, 24, 23)
}
