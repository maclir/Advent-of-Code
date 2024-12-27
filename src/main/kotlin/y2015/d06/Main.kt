package y2015.d06

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import java.math.BigInteger
import kotlin.math.min
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d06/Input-test.txt"
        "src/main/kotlin/y2015/d06/Input.txt"
    ).readText(Charsets.UTF_8)

    println("${
        measureTimeMillis {
            println(
                "part1: ${part1(input)}"
            )
        }
    } ms\n")

    println("${
        measureTimeMillis {
            println(
                "part2: ${part2(input)}"
            )

        }
    } ms")
}

/*
turn off 301,3 through 808,453
turn on 351,678 through 951,908
toggle 720,196 through 897,994
 */
private data class Instruction (
    val type: Type,
    val start: Node,
    val end: Node,
) {
    fun execute(map: List<MutableList<Boolean>>) {
        for (row in start.row..end.row) {
            for (col in start.col..end.col) {
                map[row][col] = when(type) {
                    Type.ON -> true
                    Type.OFF -> false
                    Type.TOGGLE -> !map[row][col]
                }
            }
        }
    }

    fun execute2(map: List<MutableList<BigInteger>>) {
        for (row in start.row..end.row) {
            for (col in start.col..end.col) {
                map[row][col] = when(type) {
                    Type.ON -> map[row][col] + 1.toBigInteger()
                    Type.OFF -> (map[row][col] - 1.toBigInteger()).coerceAtLeast(0.toBigInteger())
                    Type.TOGGLE -> map[row][col] + 2.toBigInteger()
                }
            }
        }
    }

    enum class Type {
        ON,
        OFF,
        TOGGLE
    }
}
private fun part1(input: String): Int {
    val grid = List(1000) {
        MutableList(1000) {
            false
        }
    }

    input.lines().map { line ->
        val groupValues = """(.*) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)""".toRegex().find(line)!!.groupValues
        Instruction(
            when(groupValues[1]) {
                "turn off" -> Instruction.Type.OFF
                "turn on" -> Instruction.Type.ON
                "toggle" -> Instruction.Type.TOGGLE
                else -> throw Exception("Unknown type")
            },
            Node(groupValues[2].toInt(), groupValues[3].toInt()),
            Node(groupValues[4].toInt(), groupValues[5].toInt()),
        )
    }.forEach {
        it.execute(grid)
    }

    return grid.sumOf { it.count { it } }
}

private fun part2(input: String): BigInteger {
    val grid = List(1000) {
        MutableList(1000) {
            0.toBigInteger()
        }
    }

    input.lines().map { line ->
        val groupValues = """(.*) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)""".toRegex().find(line)!!.groupValues
        Instruction(
            when(groupValues[1]) {
                "turn off" -> Instruction.Type.OFF
                "turn on" -> Instruction.Type.ON
                "toggle" -> Instruction.Type.TOGGLE
                else -> throw Exception("Unknown type")
            },
            Node(groupValues[2].toInt(), groupValues[3].toInt()),
            Node(groupValues[4].toInt(), groupValues[5].toInt()),
        )
    }.forEach {
        it.execute2(grid)
    }

    return grid.sumOf { it.sumOf { it } }
}
