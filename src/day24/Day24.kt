package day24

import Day
import utils.groupLines

abstract class Gate(
    var name: String,
    var inputA: Gate? = null,
    var inputB: Gate? = null
) {
    open val value get(): Boolean? = null

    val inputs get() = setOf(inputA!!, inputB!!)
}

class ValueGate(name: String, override val value: Boolean) : Gate(name)

class AndGate(
    name: String,
    inputA: Gate? = null,
    inputB: Gate? = null
) : Gate(name, inputA, inputB) {
    override val value get(): Boolean? =
        inputA?.value?.let { valueA ->
            inputB?.value?.let { valueB ->
                valueA && valueB
            }
        }
}

class OrGate(
    name: String,
    inputA: Gate? = null,
    inputB: Gate? = null
) : Gate(name, inputA, inputB) {
    override val value get(): Boolean? =
        inputA?.value?.let { valueA ->
            inputB?.value?.let { valueB ->
                valueA || valueB
            }
        }
}

class XorGate(
    name: String,
    inputA: Gate? = null,
    inputB: Gate? = null
) : Gate(name, inputA, inputB) {
    override val value get(): Boolean? =
        inputA?.value?.let { valueA ->
            inputB?.value?.let { valueB ->
                valueA != valueB
            }
        }
}

class Day24: Day<String>() {
    fun parseInput(input: List<String>): List<Gate> {
        val (valueLines, gateLines) = groupLines(input)
        val gates = mutableMapOf<String, Gate>()

        valueLines.forEach { line ->
            val (name, value) = line.split(": ")

            gates[name] = ValueGate(name, value == "1")
        }

        gateLines
            .map { line ->
                val (inNameA, type, inNameB, _, name) = line.split(" ")
                val gate = when (type) {
                    "AND" -> AndGate(name)
                    "OR" -> OrGate(name)
                    "XOR" -> XorGate(name)
                    else -> throw IllegalStateException(
                        "invalid gate type: $type"
                    )
                }

                gates[name] = gate

                gate to (inNameA to inNameB)
            }
            .forEach { (gate, inputs) ->
                gate.inputA = gates[inputs.first]
                gate.inputB = gates[inputs.second]
            }

        return gates.values.toList()
    }

    override fun solvePart1(input: List<String>): String =
        parseInput(input)
            .filter { it.name.startsWith("z") }
            .sortedByDescending { it.name }
            .map { it.value!! }
            .fold(0L) { acc, value -> acc shl 1 or (if (value) 1 else 0) }
            .let { "$it" }

    override fun solvePart2(input: List<String>): String {
        val gates = parseInput(input)
        val inputs = buildMap {
            gates.forEach { gate ->
                if (gate !is ValueGate)
                    gate.inputs.forEach { inputGate ->
                        this[inputGate] = (
                            getOrPut(inputGate) { setOf<Gate>() }
                            + setOf(gate)
                        )
                    }
            }
        }

        return gates
            .filter { gate ->
                val isOutput = gate.name.startsWith("z")

                when (gate) {
                    is AndGate -> (
                        gate.inputs.map { it.name }.toSet()
                            != setOf("x00", "y00")
                        && inputs[gate]?.all { it !is OrGate } != false
                    )
                    is OrGate -> (
                        isOutput
                        && gate.name != "z45"
                    )
                    is XorGate -> (
                        (
                            gate.name != "z00"
                            && gate.inputs.all { it is ValueGate }
                            && inputs[gate]?.any { it is XorGate } != true
                        ) || (
                            !isOutput
                            && gate.inputs.any { it !is ValueGate }
                        )
                    )
                    else -> false
                }
            }
            .map { it.name }
            .sorted()
            .joinToString(",")
    }
}

fun main() = Day24().run {
    runPart(1, "4", "2024")
    runPart(2)
}
