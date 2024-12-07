package y2024.d07

import utilities.Node
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d07/Input-test.txt"
        "src/main/kotlin/y2024/d07/Input.txt"
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

private fun part1(input: String): Long {
    val equations = input.lines().map { line ->
        """[0-9]+""".toRegex().findAll(line).map(MatchResult::value).map { it.toLong() }.toList()
    }

    return equations.sumOf { equation ->
        val result = equation.first()
        val numbers = equation.drop(1)
        if (testEquation(result, numbers, 0)) result
        else 0
    }
}

private fun testEquation(result: Long, numbers: List<Long>, acc: Long): Boolean {
    if (acc > result) return false
    if (numbers.isEmpty()) return result == acc

    val nextNumber = numbers.first()
    val nextNumbers = numbers.drop(1)
    if (acc == 0L) return testEquation(result, nextNumbers, nextNumber)
    else return testEquation(result, nextNumbers, acc + nextNumber)
            || testEquation(result, nextNumbers, acc * nextNumber)
}

private fun part2(input: String): Long {
    val equations = input.lines().map { line ->
        """[0-9]+""".toRegex().findAll(line).map(MatchResult::value).map { it.toLong() }.toList()
    }

    return equations.sumOf { equation ->
        val result = equation.first()
        val numbers = equation.drop(1)
        if (testEquationExtended(result, numbers, 0)) result
        else 0
    }
}

private fun testEquationExtended(result: Long, numbers: List<Long>, acc: Long): Boolean {
    if (acc > result) return false
    if (numbers.isEmpty()) return result == acc

    val nextNumber = numbers.first()
    val nextNumbers = numbers.drop(1)
    if (acc == 0L) return testEquationExtended(result, nextNumbers, nextNumber)
    else {
        var pow = 10
        while (nextNumber >= pow) pow *= 10
        val concatenated = acc * pow + nextNumber

        return testEquationExtended(result, nextNumbers, acc + nextNumber)
                || testEquationExtended(result, nextNumbers, acc * nextNumber)
                || testEquationExtended(result, nextNumbers, concatenated)
    }
}