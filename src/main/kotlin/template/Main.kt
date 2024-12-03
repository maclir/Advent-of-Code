package template

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
        "src/main/kotlin/template/Input-test.txt"
//        "src/main/kotlin/template/Input.txt"
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
    input.lines().map { it.toCharArray().toList() }
    input.lines().sumOf { line ->
        """mul\(([0-9]{1,3}),([0-9]{1,3})\)""".toRegex().findAll(line).map {
            it.groupValues[1].toInt() to it.groupValues[2].toInt()
        }.sumOf { (a, b) ->
            a * b
        }
    }
    """([0-9]+)[^:]""".toRegex().findAll(input).map { it.groupValues[1] }.map { it.toInt() }.toSet()
    """[0-9]+""".toRegex().findAll(input).map(MatchResult::value).map { it.toInt() }

    return 0
}

private fun part2(input: String): Int {
    return 0
}