package day17

import Day
import utils.groupLines

data class Program(
    val registers: MutableList<Long>,
    val instructions: List<Int>
) {
    val initialRegisters = registers.toList()

    fun getComboOperand(instruction: Int): Int = when (instruction) {
        in 0..3 -> instruction
        in 4..6 -> registers[instruction - 4].toInt()
        else -> throw IllegalStateException("Invalid instruction $instruction")
    }

    fun execute(): Sequence<Int> = sequence {
        var i = 0

        while (i < instructions.lastIndex) {
            val op = instructions[i]
            val arg = instructions[i + 1]
            var nextI = i + 2

            when (op) {
                0 -> registers[0] = registers[0] shr getComboOperand(arg)
                1 -> registers[1] = registers[1] xor arg.toLong()
                2 -> registers[1] = getComboOperand(arg).toLong() and 0b111
                3 -> if (registers[0] != 0L)
                    nextI = arg
                4 -> registers[1] = registers[1] xor registers[2]
                5 -> yield(getComboOperand(arg) and 0b111)
                6 -> registers[1] = registers[0] shr getComboOperand(arg)
                7 -> registers[2] = registers[0] shr getComboOperand(arg)
                else -> throw IllegalStateException("Invalid instruction: $op")
            }

            i = nextI
        }
    }

    fun resetWithRegisterA(n: Long) {
        registers[0] = n

        initialRegisters.drop(1).forEachIndexed { i, n ->
            registers[1 + i] = n
        }
    }
}

class Day17: Day<String>() {
    fun parseInput(input: List<String>): Program {
        val (registers, instructions) = groupLines(input)

        return Program(
            registers.map { it.drop(12).toLong() }.toMutableList(),
            instructions.first().drop(9).split(",").map { it.toInt() }
        )
    }

    override fun solvePart1(input: List<String>): String =
        parseInput(input).execute().joinToString(",")

    fun getValueFromGroups(groups: List<Int>): Long =
        groups.foldRight(0L) { n, acc -> (acc shl 3) or n.toLong() }

    fun solve(program: Program, groups: List<Int>, i: Int): Long? {
        if (i < 0)
            return getValueFromGroups(groups)

        (0..7).forEach { n ->
            val newGroups = groups.mapIndexed { j, v -> if (j == i) n else v }

            program.resetWithRegisterA(getValueFromGroups(newGroups))

            val result = program.execute().toList()

            if (
                result.size == program.instructions.size
                && result[i] == program.instructions[i]
            )
                solve(program, newGroups, i - 1)?.let { return it }
        }

        return null
    }

    override fun solvePart2(input: List<String>): String {
        val program = parseInput(input)
        val groups = program.instructions.map { 0 }

        return "" + solve(program, groups, groups.lastIndex)
    }
}

fun main() = Day17().run {
    runPart(1, "5,7,3,0", "4,6,3,5,6,3,5,2,1,0")
    runPart(2, "117440")
}
