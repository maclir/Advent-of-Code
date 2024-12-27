package y2015.d05

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d05/Input-test.txt"
        "src/main/kotlin/y2015/d05/Input.txt"
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
    return input.lines()
        .filter {
            it.count { "aeiou".contains(it) } >= 3
        }
        .filter {
            it.windowed(2).any { it[0] == it[1]}
        }
        .filter { line -> listOf("ab", "cd", "pq", "xy").all { !line.contains(it) } }
        .count()
}

private fun part2(input: String): Int {
    return input.lines()
        .filter { line ->
            line.windowed(2).any { line.contains(""".*$it.*$it.*""".toRegex())}
        }
        .filter {
            it.windowed(3).any { it[0] == it[2]}
        }
        .count()
}