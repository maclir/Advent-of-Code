package y2023.d13

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d13/Input-test.txt"
        "src/main/kotlin/y2023/d13/Input.txt"
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

fun String.transpose(): String {
    val lines = lines()
    val rows = lines.size
    val columns = lines().first().length
    val transposed = Array(columns) { CharArray(rows) }
    for (row in 0 until rows) {
        for (column in 0 until columns) {
            transposed[column][row] = lines[row][column]
        }
    }
    return transposed.joinToString("\n") { it.joinToString() }
}

fun getReflectionIndex(pattern: String): List<Int> {
    val rows = pattern.lines()
    val result = mutableListOf<Int>()
    outer@ for (index in 1 until rows.size) {
        for (i in 0 until index) {
            if (index + i > rows.size - 1) break
            if (rows[index - i - 1] != rows[index + i]) continue@outer
        }

        result.add(index)
    }
    return result
}

fun part1(input: String): Int {
    var sum = 0
    input.split("\n\n").forEach { pattern ->
        run breaking@{
            val rowIndex = getReflectionIndex(pattern)
            if (rowIndex.isNotEmpty()) {
                sum += 100 * rowIndex.first()
                return@breaking
            }

            val columnIndex = getReflectionIndex(pattern.transpose())
            if (columnIndex.isNotEmpty()) {
                sum += columnIndex.first()
                return@breaking
            }
        }
    }

    return sum
}

fun part2(input: String): Int {
    var sum = 0
    input.split("\n\n").forEach { originalPattern ->
        val originalRowIndex = getReflectionIndex(originalPattern).firstOrNull()
        val originalColumnIndex = getReflectionIndex(originalPattern.transpose()).firstOrNull()
        val lines = originalPattern.lines()
        run breaking@{
            for (x in lines.indices) {
                for (y in lines[0].indices) {
                    val newLine =
                        lines[x].toCharArray().toMutableList().apply { set(y, if (lines[x][y] == '.') '#' else '.') }
                            .joinToString("")
                    val pattern = lines.toMutableList().apply { set(x, newLine) }.joinToString("\n")

                    val rowIndex = getReflectionIndex(pattern).filter { it != originalRowIndex }
                    if (rowIndex.isNotEmpty()) {
                        sum += 100 * rowIndex.first()
                        return@breaking
                    }

                    val columnIndex = getReflectionIndex(pattern.transpose()).filter { it != originalColumnIndex }
                    if (columnIndex.isNotEmpty()) {
                        sum += columnIndex.first()
                        return@breaking
                    }
                }
            }
        }
    }

    return sum
}