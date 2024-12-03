package y2024.d03

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d03/Input-test.txt"
        "src/main/kotlin/y2024/d03/Input.txt"
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

private fun part1(input: String): Int {
    return input.lines().sumOf { line ->
        Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)").findAll(line).map {
            it.groupValues[1].toInt() to it.groupValues[2].toInt()
        }.sumOf { (a, b) ->
            a * b
        }
    }
}

private fun part2(input: String): Int {
    return input.lines().joinToString("").split("don't()").sumOf{ dontPart ->
        val doParts = dontPart.split("do()").drop(1)
        doParts.sumOf { doPart ->
            Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)").findAll(doPart).sumOf {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
        }
    }
}