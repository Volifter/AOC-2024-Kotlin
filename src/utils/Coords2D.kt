package utils

import kotlin.math.absoluteValue
import kotlin.math.sqrt

data class Coords2D(var x: Int, var y: Int): Comparable<Coords2D> {
    val delta: Float get() = sqrt(1f * x * x + y * y)

    val manhattanDelta: Int get() = x.absoluteValue + y.absoluteValue

    val neighbors get() =
        (y - 1..y + 1).asSequence().flatMap { newY ->
            (x - 1..x + 1).mapNotNull { newX ->
                Coords2D(newX, newY).takeIf { it != this }
            }
        }

    fun wrapIn(size: Coords2D): Coords2D =
        Coords2D(
            Math.floorMod(x, size.x),
            Math.floorMod(y, size.y)
        )

    fun coerceIn(range: IntRange): Coords2D =
        Coords2D(x.coerceIn(range), y.coerceIn(range))

    operator fun contains(other: Coords2D): Boolean =
        other.x in 0..<x && other.y in 0..<y

    operator fun plus(other: Coords2D): Coords2D =
        Coords2D(x + other.x, y + other.y)

    operator fun minus(other: Coords2D): Coords2D =
        Coords2D(x - other.x, y - other.y)

    operator fun times(ratio: Int): Coords2D =
        Coords2D(x * ratio, y * ratio)

    companion object {
        fun iterate(size: Coords2D): Sequence<Coords2D> =
            (0..<size.y).asSequence().flatMap { y ->
                (0..<size.x).asSequence().map { x ->
                    Coords2D(x, y)
                }
            }

        fun iterateOnField(field: List<String>) : Sequence<Coords2D> =
            iterate(Coords2D(field.first().length, field.size))

        fun getLowerBound(coords: Iterable<Coords2D>): Coords2D =
            coords.reduce { (ax, ay), (bx, by) ->
                Coords2D(minOf(ax, bx), minOf(ay, by))
            }

        fun getUpperBound(coords: Iterable<Coords2D>): Coords2D =
            coords.reduce { (ax, ay), (bx, by) ->
                Coords2D(maxOf(ax, bx), maxOf(ay, by))
            }
    }

    override fun compareTo(other: Coords2D): Int =
        (y - other.y).takeIf { it != 0 } ?: (x - other.x)
}
