package y2023.d09

import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.time.times

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d09/Input-test.txt"
        "src/main/kotlin/y2023/d09/Input.txt"
    ).readText(Charsets.UTF_8)

    println("${
        measureTimeMillis {
            println(
//                part1(input)
                part2(input)
            )

        }
    } ms")
}

private fun part1(input: String): Int {
    val lines = input.lines().map { line ->
        line.split(" ").map { it.toInt() }
    }

    return lines.sumOf { line ->
        val calculations = mutableListOf<MutableList<Int>>()
        var currentLine = line.toMutableList()
        while (true) {
            calculations.add(currentLine)
            val nextLine = mutableListOf<Int>()
            currentLine.forEachIndexed { index, number ->
                if (index != currentLine.size - 1) {
                    nextLine.add(currentLine[index + 1] - number)
                }
            }
            currentLine = nextLine
            if (currentLine.all { it == 0 }) break
        }

        var variable = 0
        calculations.reversed().fold(variable) { _, ints ->
            variable += ints.last()
            variable
        }
    }
}

private fun part2(input: String): Int {
    val lines = input.lines().map { line ->
        line.split(" ").map { it.toInt() }
    }

    return lines.sumOf { line ->
        val calculations = mutableListOf<MutableList<Int>>()
        var currentLine = line.toMutableList()
        while (true) {
            calculations.add(currentLine)
            val nextLine = mutableListOf<Int>()
            currentLine.forEachIndexed { index, number ->
                if (index != currentLine.size - 1) {
                    nextLine.add(currentLine[index + 1] - number)
                }
            }
            currentLine = nextLine
            if (currentLine.all { it == 0 }) break
        }

        var variable = 0
        calculations.reversed().fold(variable) { _, ints ->
            variable = ints.first() - variable
            variable
        }
    }
}
