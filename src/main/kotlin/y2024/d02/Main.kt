package y2024.d02

import intLines
import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d02/Input-test.txt"
        "src/main/kotlin/y2024/d02/Input.txt"
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
    val reports = input.intLines()
    return reports.count { report ->
        val direction = report[0] > report[1]
        report.forEachIndexed { index, i ->
            if (index == 0) return@forEachIndexed

            if (direction) {
                if (report[index - 1] - i < 1 || report[index - 1] - i > 3) {
                    return@count false
                }
            } else {
                if (i - report[index - 1] < 1 || i - report[index - 1] > 3) {
                    return@count false
                }
            }
        }
        true
    }
}

private fun part2(input: String): Int {
    val reports = input.intLines()
    return reports.count { report ->
        var directionCount = 0
        report.forEachIndexed { index, i ->
            if (index == 0) return@forEachIndexed
            if (report[index - 1] > i) {
                directionCount++
            }
        }
        val direction = directionCount > 2
        if (direction) checkList(report.reversed(), false)
        else checkList(report, false)
    }
}

private fun checkList(report: List<Int>, skipIndex: Boolean): Boolean {
    report.forEachIndexed { index, i ->
        if (index == 0) return@forEachIndexed
        if (report[index - 1] < i && abs(report[index - 1] - i) >= 1 && abs(report[index - 1] - i) <= 3) {
            return@forEachIndexed
        } else {
            return !skipIndex && (checkList(
                report.toMutableList().filterIndexed { filterIndex, _ -> filterIndex != index }, true
            ) || checkList(
                report.toMutableList().filterIndexed { filterIndex, _ -> filterIndex != index - 1 }, true
            ))
        }
    }
    return true
}