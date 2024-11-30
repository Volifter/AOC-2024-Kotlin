import utils.Colors
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readLines
import kotlin.system.measureTimeMillis

open class Day<T> {
    open fun solvePart1(input: List<String>): T =
        throw IllegalStateException("Part 1 is not implemented")

    open fun solvePart2(input: List<String>): T =
        throw IllegalStateException("Part 2 is not implemented")

    val dayName = javaClass.kotlin.simpleName!!

    val exampleInputs = Path("inputs")
        .listDirectoryEntries("${dayName}_test*.txt")
        .takeIf { it.isNotEmpty() }
        ?.map { it.readLines() }
        ?: throw IllegalStateException(
            "Missing example inputs for day $dayName"
        )

    val puzzleInput = File("inputs", "${this.javaClass.kotlin.simpleName!!}.txt")
        .takeIf { it.exists() }
        ?.readLines()
        ?: throw IllegalStateException("Missing test input for day $dayName")

    private fun reportTest(testId: Int, expected: T, got: T, timeMs: Long) {
        val success = got == expected
        val colorCode = if (success) Colors.GREEN.code else Colors.RED.code
        val operator = "${if (success) "=" else "!"}="

        println(
            "  Test ${Colors.BLUE.code}#${testId}${Colors.RESET.code}: "
            + "${colorCode}$got${Colors.RESET.code} "
            + "$operator "
            + "${Colors.GREEN.code}$expected "
            + "${Colors.GRAY.code}(${timeMs}ms)${Colors.RESET.code}"
        )

        if (!success)
            throw IllegalStateException("Test #$testId failed")
    }

    fun runPart(partId: Int, vararg outputs: T) {
        val part = when (partId) {
            1 -> ::solvePart1
            2 -> ::solvePart2
            else -> throw IllegalArgumentException("Invalid part: $partId")
        }

        if (outputs.size != exampleInputs.size)
            throw Exception(
                "Invalid amount of outputs: "
                + "expected ${exampleInputs.size}, got ${outputs.size}"
            )

        println(
            "PART ${Colors.YELLOW.code}$partId${Colors.RESET.code}:"
        )
        exampleInputs.zip(outputs).forEachIndexed { i, (input, expected) ->
            var got: T
            val time = measureTimeMillis { got = part(input) }

            reportTest(i + 1, expected, got, time)
        }

        var got: T
        val time = measureTimeMillis { got = part(puzzleInput) }

        println()
        println(
            "  ${Colors.BLUE.code}Result${Colors.RESET.code}: "
            + "${Colors.YELLOW.code}$got "
            + "${Colors.GRAY.code}(${time}ms)${Colors.RESET.code}")
        println()

        if (dayName == "Day25")
            println(
                "🎄 Haha, well done, another christmas saved!\n"
                + "🍾 Now go celebrate!"
            )
    }
}
