package y2024.d17

import utilities.isPowerOf
import java.io.File
import java.math.BigInteger
import kotlin.math.pow
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d17/Input-test.txt"
        "src/main/kotlin/y2024/d17/Input.txt"
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

private val outputs = mutableListOf<Int>()

private enum class Instruction {
    adv0 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            registers['A'] = registers.getValue('A') / 2.toBigInteger().pow(operand.getComboValue(registers).toInt())
            return instructionPointer + 2
        }
    },
    bxl1 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            registers['B'] = registers.getValue('B').xor(operand.literalValue.toBigInteger())
            return instructionPointer + 2
        }
    },
    bst2 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            registers['B'] = operand.getComboValue(registers) % 8.toBigInteger()
            return instructionPointer + 2
        }
    },
    jnz3 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            if (registers['A'] == 0.toBigInteger()) return instructionPointer + 2
            return operand.literalValue
        }
    },
    bxc4 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            registers['B'] = registers.getValue('B').xor(registers.getValue('C'))
            return instructionPointer + 2
        }
    },
    out5 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            outputs.add((operand.getComboValue(registers) % 8.toBigInteger()).toInt())
            return instructionPointer + 2
        }
    },
    bdv6 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            registers['B'] = registers.getValue('A') / 2.toBigInteger().pow(operand.getComboValue(registers).toInt())
            return instructionPointer + 2
        }
    },
    cdv7 {
        override fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int {
            registers['C'] = registers.getValue('A') / 2.toBigInteger().pow(operand.getComboValue(registers).toInt())
            return instructionPointer + 2
        }
    };

    abstract fun execute(registers: MutableMap<Char, BigInteger>, operand: Operand, instructionPointer: Int): Int
}

private data class Operand(val literalValue: Int) {
    fun getComboValue(registers: Map<Char, BigInteger>) = ComboOperand.entries[literalValue].getValue(registers)
}

private enum class ComboOperand {
    C0 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger = 0.toBigInteger()
    },
    C1 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger = 1.toBigInteger()
    },
    C2 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger = 2.toBigInteger()
    },
    C3 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger = 3.toBigInteger()
    },
    C4 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger = registers.getValue('A')
    },
    C5 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger = registers.getValue('B')
    },
    C6 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger = registers.getValue('C')
    },
    C7 {
        override fun getValue(registers: Map<Char, BigInteger>): BigInteger =
            throw Exception("Invalid combo operand 7!")
    };

    abstract fun getValue(registers: Map<Char, BigInteger>): BigInteger
}

private fun part1(input: String): Int {
    val (registersInput, programInput) = input.split("\n\n")
    val registers = registersInput.lines().associate { line ->
        """([ABC]): ([0-9]+)""".toRegex().findAll(line).map {
            it.groupValues[1].first() to it.groupValues[2].toBigInteger()
        }.first()
    }.toMutableMap()

    val program = programInput.lines().first().split(" ")[1].split(",").map { digit ->
        digit.toInt()
    }

    var instructionPointer = 0
    while (instructionPointer + 1 <= program.lastIndex) {
        val instruction = Instruction.entries[program[instructionPointer]]
        val operand = Operand(program[instructionPointer + 1])
        instructionPointer = instruction.execute(registers, operand, instructionPointer)
    }
    println(outputs.joinToString(","))
    return 0
}

private fun part2(input: String): Int {
    val (registersInput, programInput) = input.split("\n\n")
    val initialRegisters = registersInput.lines().associate { line ->
        """([ABC]): ([0-9]+)""".toRegex().findAll(line).map {
            it.groupValues[1].first() to it.groupValues[2].toBigInteger()
        }.first()
    }.toMutableMap()

    val program = programInput.lines().first().split(" ")[1].split(",").map { digit ->
        digit.toInt()
    }

    var registerAInitialValue = (8.toBigInteger().pow(program.size - 1) + 1.toBigInteger())
    outputs.clear()
    outputs.addAll(List(16) { 8 })
    val bitCheck = 15
    val registers = initialRegisters.toMutableMap()
    while (outputs != program) {
        for (i in bitCheck downTo 0) {
            if (outputs[i] != program[i]) {
                registerAInitialValue += 8.0.pow(i- 1).toBigDecimal().toBigInteger()
                break
            }
        }
        registerAInitialValue++
        registers['A'] = registerAInitialValue
        registers['B'] = initialRegisters.getValue('B')
        registers['C'] = initialRegisters.getValue('C')

        if (outputs.size > program.size) {
            println("passed the limit")
            break
        }
        outputs.clear()
        var instructionPointer = 0
        while (instructionPointer + 1 <= program.lastIndex) {
            val instruction = Instruction.entries[program[instructionPointer]]
            val operand = Operand(program[instructionPointer + 1])
            instructionPointer = instruction.execute(registers, operand, instructionPointer)
        }
    }
    println(registerAInitialValue)
    return 0
}

