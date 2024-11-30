package utils

data class Box3D(val start: Coords3D, val end: Coords3D) {
    val isEmpty = end.x < start.x || end.y < start.y || end.z < start.z

    val isNotEmpty = !isEmpty

    val xRange get() = (start.x..end.x)

    val yRange get() = (start.y..end.y)

    val zRange get() = (start.z..end.z)

    fun intersect(other: Box3D): Box3D =
        Box3D(
            Coords3D(
                maxOf(start.x, other.start.x),
                maxOf(start.y, other.start.y),
                maxOf(start.z, other.start.z)
            ),
            Coords3D(
                minOf(end.x, other.end.x),
                minOf(end.y, other.end.y),
                minOf(end.z, other.end.z)
            )
        )

    operator fun plus(offset: Coords3D) =
        Box3D(start + offset, end + offset)

    operator fun minus(offset: Coords3D) =
        Box3D(start - offset, end - offset)

    companion object {
        fun fromCorners(cornerA: Coords3D, cornerB: Coords3D): Box3D =
            Box3D(
                Coords3D(
                    minOf(cornerA.x, cornerB.x),
                    minOf(cornerA.y, cornerB.y),
                    minOf(cornerA.z, cornerB.z)
                ),
                Coords3D(
                    maxOf(cornerA.x, cornerB.x),
                    maxOf(cornerA.y, cornerB.y),
                    maxOf(cornerA.z, cornerB.z)
                )
            )
    }
}
