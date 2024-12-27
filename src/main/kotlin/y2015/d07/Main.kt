package y2015.d07

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.DataOutput
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d07/Input-test.txt"
        "src/main/kotlin/y2015/d07/Input.txt"
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

private enum class Type {
    PASS {
        override fun execute(instruction: String, wires: MutableMap<String, UInt>) = instruction.intOrFromWire(wires)
        override fun hasInputs(instruction: String, wires: MutableMap<String, UInt>) = instruction.isIntOrInWire(wires)
    },
    NOT {
        override fun execute(instruction: String, wires: MutableMap<String, UInt>) =
            instruction.split(" ")[1].intOrFromWire(wires).inv()

        override fun hasInputs(instruction: String, wires: MutableMap<String, UInt>) =
            instruction.split(" ")[1].isIntOrInWire(wires)
    },
    AND {
        override fun execute(instruction: String, wires: MutableMap<String, UInt>): UInt {
            val inputs = instruction.split(" AND ")
            return inputs[0].intOrFromWire(wires).and(inputs[1].intOrFromWire(wires))
        }

        override fun hasInputs(instruction: String, wires: MutableMap<String, UInt>): Boolean {
            val inputs = instruction.split(" AND ")
            return inputs[0].isIntOrInWire(wires) && inputs[1].isIntOrInWire(wires)
        }
    },
    OR {
        override fun execute(instruction: String, wires: MutableMap<String, UInt>): UInt {
            val inputs = instruction.split(" OR ")
            return inputs[0].intOrFromWire(wires).or(inputs[1].intOrFromWire(wires))
        }

        override fun hasInputs(instruction: String, wires: MutableMap<String, UInt>): Boolean {
            val inputs = instruction.split(" OR ")
            return inputs[0].isIntOrInWire(wires) && inputs[1].isIntOrInWire(wires)
        }
    },
    R_SHIFT {
        override fun execute(instruction: String, wires: MutableMap<String, UInt>): UInt {
            val inputs = instruction.split(" RSHIFT ")
            return inputs[0].intOrFromWire(wires).shr(inputs[1].toInt())
        }

        override fun hasInputs(instruction: String, wires: MutableMap<String, UInt>): Boolean {
            val inputs = instruction.split(" RSHIFT ")
            return inputs[0].isIntOrInWire(wires)
        }
    },
    L_SHIFT {
        override fun execute(instruction: String, wires: MutableMap<String, UInt>): UInt {
            val inputs = instruction.split(" LSHIFT ")
            return inputs[0].intOrFromWire(wires).shl(inputs[1].toInt())
        }

        override fun hasInputs(instruction: String, wires: MutableMap<String, UInt>): Boolean {
            val inputs = instruction.split(" LSHIFT ")
            return inputs[0].isIntOrInWire(wires)
        }
    };

    abstract fun execute(instruction: String, wires: MutableMap<String, UInt>): UInt
    abstract fun hasInputs(instruction: String, wires: MutableMap<String, UInt>): Boolean
}

private fun String.intOrFromWire(wires: Map<String, UInt>) = toUIntOrNull() ?: wires.getValue(this)
private fun String.isIntOrInWire(wires: Map<String, UInt>) = if (toUIntOrNull() == null) wires.contains(this)
else true

private fun String.toType() = when {
    this.contains(" AND ") -> Type.AND
    this.contains(" OR ") -> Type.OR
    this.contains(" LSHIFT ") -> Type.L_SHIFT
    this.contains(" RSHIFT ") -> Type.R_SHIFT
    this.contains("NOT ") -> Type.NOT
    else -> Type.PASS
}

private fun part1(input: String): Int {
    val wires = mutableMapOf<String, UInt>()

    val instructionQueue = input.lines().toMutableList()

    while (instructionQueue.isNotEmpty()) {
        val line = instructionQueue.removeFirst()
        val (ins, output) = line.split(" -> ")
        val type = ins.toType()
        if (wires.contains(output)) continue
        if (!type.hasInputs(ins, wires)) {
            instructionQueue.add(line)
            continue
        }
        val outputBits = type.execute(ins, wires).toString(2)
        wires[output] = outputBits.substring((outputBits.length - 16).coerceAtLeast(0), outputBits.length).toUInt(2)
    }

    return wires.getValue("a").toInt()
}

private fun part2(input: String): UInt {
    val wires = mutableMapOf<String, UInt>()

    wires["b"] = 46065.toUInt()
    val instructionQueue = input.lines().toMutableList()

    while (instructionQueue.isNotEmpty()) {
        val line = instructionQueue.removeFirst()
        val (ins, output) = line.split(" -> ")
        val type = ins.toType()
        if (wires.contains(output)) continue
        if (!type.hasInputs(ins, wires)) {
            instructionQueue.add(line)
            continue
        }
        val outputBits = type.execute(ins, wires).toString(2)
        wires[output] = outputBits.substring((outputBits.length - 16).coerceAtLeast(0), outputBits.length).toUInt(2)
    }

    return wires.getValue("a")
}