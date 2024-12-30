package y2015.d09

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
        "src/main/kotlin/y2015/d09/Input-test.txt"
//        "src/main/kotlin/y2015/d09/Input.txt"
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

private fun part1(input: String): Int {
    val (aInput, bInput) = input.split("\n\n")
    input.intLines()
    input.longLines()
    input.charGrid()
    input.lines().map { it.toCharArray().toList() }
    lateinit var currentNode: Node
    input.lines().mapIndexed { rowIndex, row ->
        row.toCharArray().toList().mapIndexed { colIndex, c ->
            if (c == '^') {
                currentNode = Node(rowIndex, colIndex)
                '.'
            } else c
        }
    }
    input.lines().sumOf { line ->
        """mul\(([0-9]{1,3}),([0-9]{1,3})\)""".toRegex().findAll(line).map {
            it.groupValues[1].toInt() to it.groupValues[2].toInt()
        }.sumOf { (a, b) ->
            a * b
        }
    }
    input.lines().map { line ->
        """p=([0-9]+),([0-9]+) v=([\-0-9]+),([\-0-9]+)""".toRegex().find(line)!!.groupValues
            .drop(1).map {
                it.toInt()
            }
    }
    input.lines().forEach { line ->
        """[0-9]+""".toRegex().findAll(line).map(MatchResult::value).map { it.toInt() }
    }

    return 0
}

private fun part2(input: String): Int {
    return 0
}