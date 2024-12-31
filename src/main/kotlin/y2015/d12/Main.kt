package y2015.d12

import utilities.*
import java.io.File

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d12/Input-test.txt"
        "src/main/kotlin/y2015/d12/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun(::part1, input)
    printRun(::part2, input)
}

private fun part1(input: String): Int {
    return """(-?[0-9]+)""".toRegex().findAll(input).map {
        it.groupValues[1].toInt()
    }.sum()
}

private fun part2(input: String): Int {
    val omitLines = mutableListOf<Int>()
    val lines = input.lines()

    lines.forEachIndexed{ index, line ->
        if (line.contains("red")) {
            var arrayClosingCount = 0
            var objectClosingCount = 0
            var fromIndex = 0
            for (innerIndex in index - 1 downTo 0) {
                if (lines[innerIndex].contains(']')) arrayClosingCount++
                if (lines[innerIndex].contains('[')) {
                    if (arrayClosingCount > 0) arrayClosingCount--
                    else return@forEachIndexed
                }

                if (lines[innerIndex].contains('}')) objectClosingCount++
                if (lines[innerIndex].contains('{')) {
                    if (objectClosingCount > 0) objectClosingCount--
                    else {
                        fromIndex = innerIndex
                        break
                    }
                }
            }

            objectClosingCount = 0
            for (innerIndex in index + 1 .. lines.lastIndex) {
                if (lines[innerIndex].contains('{')) objectClosingCount++
                if (lines[innerIndex].contains('}')) {
                    if (objectClosingCount > 0) objectClosingCount--
                    else {
                        omitLines.addAll(fromIndex..innerIndex)
                        break
                    }
                }
            }
        }
    }
    val newInput = lines.toMutableList().filterIndexed { index, _ -> !omitLines.contains(index) }.joinToString("\n")

    return """(-?[0-9]+)""".toRegex().findAll(newInput).map {
        it.groupValues[1].toInt()
    }.sum()
}