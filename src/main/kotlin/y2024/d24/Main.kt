package y2024.d24

import y2024.d24.Gate.Type.*
import java.io.File
import java.math.BigInteger
import java.text.DecimalFormat
import kotlin.random.Random
import kotlin.system.measureTimeMillis

private suspend fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d24/Input-test.txt"
        "src/main/kotlin/y2024/d24/Input.txt"
    ).readText(Charsets.UTF_8)

    println(
        "${
            measureTimeMillis {
                println(
                    "part1: ${part1(input)}"
                )
            }
        } ms\n")

    println(
        "${
            measureTimeMillis {
                println(
                    "part2: ${part2(input)}"
                )

            }
        } ms")
}

private data class Gate(
    val inputs: Pair<String, String>,
    val type: Type,
    val output: String,
) {
    constructor(oldGate: Gate, output: String) : this(oldGate.inputs, oldGate.type, output)

    fun <K> readyToExecute(map: Map<String, K>) =
        map.containsKey(inputs.first) && map.containsKey(inputs.second) && !map.containsKey(output)

    fun execute(map: MutableMap<String, Boolean>) {
        if (!readyToExecute(map)) return
        map[output] = type.apply(map.getValuePair(inputs))
    }

    private fun Map<String, Boolean>.getValuePair(inputs: Pair<String, String>) =
        getValue(inputs.first) to getValue(inputs.second)

    enum class Type {
        AND {
            override fun apply(inputs: Pair<Boolean, Boolean>) = inputs.first.and(inputs.second)
        },
        OR {
            override fun apply(inputs: Pair<Boolean, Boolean>) = inputs.first.or(inputs.second)
        },
        XOR {
            override fun apply(inputs: Pair<Boolean, Boolean>) = inputs.first.xor(inputs.second)
        };

        abstract fun apply(inputs: Pair<Boolean, Boolean>): Boolean
    }
}

private fun List<Gate>.swapOutputs(pairs: List<Pair<String, String>>): List<Gate> {
    val newList = this.toMutableList()
    pairs.forEach { (output1, output2) ->
        val index1 = indexOfFirst { it.output == output1 }
        val index2 = indexOfFirst { it.output == output2 }
        val newGate1 = Gate(this[index1], output2)
        val newGate2 = Gate(this[index2], output1)
        newList[index1] = newGate1
        newList[index2] = newGate2
    }
    return newList
}

private fun String.toGateType() = when (this) {
    "AND" -> AND
    "OR" -> OR
    "XOR" -> XOR
    else -> throw Exception("Unknown type")
}

private fun part1(input: String): BigInteger {
    val (aInput, bInput) = input.split("\n\n")
    val wires = aInput.lines().associate { line ->
        val (name, value) = line.split(": ")
        name to (value == "1")
    }.toMutableMap()

    val gates = bInput.lines().map { line ->
        val (input1, type, input2, _, output) = line.split(" ")
        Gate(input1 to input2, type.toGateType(), output)
    }

    var changed = true
    while (changed) {
        gates.firstOrNull {
            it.readyToExecute(wires)
        }.apply {
            if (this == null) changed = false
            else execute(wires)
        }
    }

    return wires.filter { it.key.startsWith('z') }
        .toSortedMap().values.foldIndexed(0.toBigInteger()) { index, acc: BigInteger, b ->
            if (b) acc + 2.toBigInteger().pow(index)
            else acc
        }
}

private fun part2(input: String): Int {
    val (aInput, bInput) = input.split("\n\n")
    val initialWires = aInput.lines().associate { line ->
        val (name, value) = line.split(": ")
        name to (value == "1")
    }

    val initialGates = bInput.lines().map { line ->
        val (input1, type, input2, _, output) = line.split(" ")
        Gate(input1 to input2, type.toGateType(), output)
    }
    val decimalFormat = DecimalFormat("00")
    val testWires = mutableListOf<Map<String, Boolean>>()
    repeat(20) {
        val testWire = mutableMapOf<String, Boolean>()
        for (index in 0..44) {
            testWire["x${decimalFormat.format(index)}"] = Random.nextBoolean()
            testWire["y${decimalFormat.format(index)}"] = Random.nextBoolean()
        }
        testWires.add(testWire)
    }
    testWires.add(initialWires)

    val gatesByInputM = mutableMapOf<String, Gate>()
    initialGates.forEach { gate ->
        gatesByInputM[gate.inputs.first] = gate
        gatesByInputM[gate.inputs.second] = gate
    }

    val swaps = listOf (
        "z18" to "hmt",
        "z31" to "hkh",
        "z27" to "bfq",
    )

//    initialGates
//        .filter { it.type == OR || it.type == AND }
//        .filter { it.output.startsWith('z') }
//        .forEach { println(it) }

//    gates.sortedBy { it.output }.forEach { gate ->
//        print("{${gate.inputs.first} ${gate.inputs.second}} -> ${gate.output} ")
//        when(gate.type) {
//            AND -> print(" [style=dashed, color=green];")
//            OR -> print(" [style=dotted, color=red];")
//            XOR -> print(" [color=blue];")
//        }
//        println()
//    }

    for (swap1 in initialGates.indices) {
        for (swap2 in initialGates.indices) {
            val newSwaps = swaps.plus(initialGates[swap1].output to initialGates[swap2].output)
            val gates = initialGates.swapOutputs(newSwaps)
            testSwaps(gates, testWires, newSwaps)
        }
    }

    return 0
}

private fun testSwaps(
    gates: List<Gate>,
    testWires: List<Map<String, Boolean>>,
    swaps: List<Pair<String, String>>
) {
    if (testWires
            .map {
                val wires = it.toMutableMap()
                var changed = true
                while (changed) {
                    gates.firstOrNull {
                        it.readyToExecute(wires)
                    }.apply {
                        if (this == null) changed = false
                        else execute(wires)
                    }
                }
                val (x, y, z) = listOf('x', 'y', 'z').map { char ->
                    wires.filter { it.key.startsWith(char) }
                        .toSortedMap().values.foldIndexed(0.toBigInteger()) { index, acc: BigInteger, b ->
                            if (b) acc + 2.toBigInteger().pow(index)
                            else acc
                        }
                }

                val zc = (x + y).toString(2)
                val zs = z.toString(2)
                zc.startsWith(zs)
            }.all { it }
    ) {
        println(swaps.map { it.toList() }.flatten().sorted().joinToString(","))
    }
}
