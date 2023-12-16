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
    val map = input.lines().map { it.toCharArray().toList() }
    Regex("([0-9]+)[^:]").findAll(input).map { it.groupValues[1] }.map { it.toInt() }.toSet()
    Regex("[0-9]+").findAll(input).map(MatchResult::value).map { it.toInt() }

    return map.sumOf { row ->
        row.size
    }
}

private fun part2(input: String): Int {
    return 0
}