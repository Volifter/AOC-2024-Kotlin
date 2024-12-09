package day09

import Day

data class DiskSpace(var id: Int?, var size: Int) {
    var prev: DiskSpace? = null
    var next: DiskSpace? = null

    fun asSequence() = generateSequence(this) { it.next }

    fun addLeft(other: DiskSpace) {
        other.prev = prev
        other.next = this

        prev?.next = other
        prev = other
    }

    fun addRight(other: DiskSpace) {
        other.prev = this
        other.next = next

        next?.prev = other
        next = other
    }

    fun pop() {
        prev?.next = next
        next?.prev = prev
    }

    fun fillWith(other: DiskSpace) {
        val blockSize = minOf(other.size, size)
        val freeSize = size - blockSize

        id = other.id
        size = blockSize

        if (freeSize > 0)
            addRight(DiskSpace(null, freeSize))
        if (size == 0)
            pop()

        other.size -= blockSize
        other.addRight(DiskSpace(null, blockSize))

        if (other.size == 0)
            other.pop()
    }
}

class Day09: Day<Long>() {
    fun parseInput(input: List<String>): Pair<DiskSpace, DiskSpace> {
        var head: DiskSpace? = null
        var tail: DiskSpace? = null

        input.first().mapIndexed { index, c ->
            val id = (index / 2).takeIf { index and 1 == 0 }
            val space = DiskSpace(id, c.digitToInt())

            if (tail == null) {
                head = space
                tail = space
            } else if (space.size > 0) {
                tail.addRight(space)
                tail = space
            }
        }

        return head!! to tail!!
    }

    fun getSumTo(n: Long) = n * (n - 1) / 2

    fun getChecksum(head: DiskSpace): Long =
        head
            .asSequence()
            .fold(0L to 0L) { (i, sum), space ->
                i + space.size to (
                    sum
                    + (
                        space.id?.let {
                            it * (getSumTo(i + space.size) - getSumTo(i))
                        } ?: 0L
                    )
                )
            }.second

    override fun solvePart1(input: List<String>): Long {
        var (head: DiskSpace?, tail: DiskSpace?) = parseInput(input)
        var first = head!!

        while (head != null && tail != null && head !== tail) {
            if (head.id != null) {
                head = head.next
                continue
            }

            if (tail.id == null || tail.size == 0) {
                tail = tail.prev
                continue
            }

            head.fillWith(tail)
        }

        return getChecksum(first)
    }

    fun findSpace(head: DiskSpace, target: DiskSpace): DiskSpace? =
        head
            .asSequence()
            .takeWhile { it !== target }
            .find { it.id == null && it.size >= target.size }

    override fun solvePart2(input: List<String>): Long {
        var (first, tail: DiskSpace?) = parseInput(input)

        while (tail != null) {
            if (tail.id != null)
                findSpace(first, tail)?.fillWith(tail)
            tail = tail.prev
        }

        return getChecksum(first)
    }
}

fun main() = Day09().run {
    runPart(1, 1928L)
    runPart(2, 2858L)
}
