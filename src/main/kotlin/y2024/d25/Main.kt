package y2024.d25

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d25/Input-test.txt"
        "src/main/kotlin/y2024/d25/Input.txt"
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
    val rawSections = input.split("\n\n")
    val (locks, keys) = rawSections
        .map { it.charGrid() }
        .map {
            it.transpose().map {
                if (it[0] == '#') it.count { it == '.' }
                else -it.count { it == '#' }
            }
        }
        .partition {
            it.first() > 0
        }

    return locks.sumOf { lock ->
        keys.count { key ->
            for (index in lock.indices) {
                if (lock[index] < -key[index]) return@count false
            }
            return@count true
        }
    }
}

private fun part2(input: String): Int {
    return 0
}