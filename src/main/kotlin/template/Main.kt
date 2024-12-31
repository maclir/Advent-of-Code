package template

import utilities.*
import java.io.File

private fun main() {
    val input = File(
        "src/main/kotlin/template/Input-test.txt"
//        "src/main/kotlin/template/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun(::part1, input)
    printRun(::part2, input)
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
    input.lines().map { line ->
        val matches = """(.*) to (.*) = ([0-9]+)""".toRegex().find(line)!!
        println(
            setOf(matches.groupValues[1], matches.groupValues[2], matches.groupValues[3].toInt()),
        )
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