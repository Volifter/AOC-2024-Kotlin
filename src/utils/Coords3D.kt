package utils

import kotlin.math.absoluteValue
import kotlin.math.sqrt

data class Coords3D(val x: Int, val y: Int, val z: Int): Comparable<Coords3D> {
    val delta: Float get() = sqrt(1f * x * x + y * y + z * z)

    val manhattanDelta: Int get() =
        x.absoluteValue + y.absoluteValue + z.absoluteValue

    val neighbors get() =
        (z - 1..z + 1).asSequence().flatMap { newZ ->
            (y - 1..y + 1).asSequence().flatMap { newY ->
                (x - 1..x + 1).mapNotNull { newX ->
                    Coords3D(newX, newY, newZ).takeIf { it != this }
                }
            }
        }

    fun wrapIn(size: Coords3D): Coords3D =
        Coords3D(
            Math.floorMod(x, size.x),
            Math.floorMod(y, size.y),
            Math.floorMod(z, size.z)
        )

    fun coerceIn(range: IntRange): Coords3D =
        Coords3D(x.coerceIn(range), y.coerceIn(range), z.coerceIn(range))

    operator fun contains(other: Coords3D): Boolean =
        other.x in 0..<x && other.y in 0..<y

    operator fun plus(other: Coords3D): Coords3D =
        Coords3D(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Coords3D): Coords3D =
        Coords3D(x - other.x, y - other.y, z - other.z)

    operator fun times(ratio: Int): Coords3D =
        Coords3D(x * ratio, y * ratio, z * ratio)

    override fun compareTo(other: Coords3D): Int =
        (z - other.z).takeIf { it != 0 }
            ?: (y - other.y).takeIf { it != 0 }
            ?: (x - other.x)
}
