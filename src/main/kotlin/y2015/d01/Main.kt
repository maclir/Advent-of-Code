package y2015.d01

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d01/Input-test.txt"
        "src/main/kotlin/y2015/d01/Input.txt"
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
    var floor = 0
    input.toCharArray().forEach {
        when (it) {
            '(' -> floor++
            ')' -> floor--
        }
    }

    return floor
}

private fun part2(input: String): Int {
    var floor = 0
    input.toCharArray().forEachIndexed { index, c ->
        when (c) {
            '(' -> floor++
            ')' -> floor--
        }
        if (floor == -1) return index + 1
    }

    return 0
}