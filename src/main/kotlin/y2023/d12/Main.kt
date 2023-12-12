package y2023.d12

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
        "src/main/kotlin/y2023/d12/Input-test.txt"
//        "src/main/kotlin/y2023/d12/Input.txt"
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

data class Line(val line: String, val damageCounts: List<Int>)

private fun Line.replaceSpring(index: Int, char: Char) =
    Line(line.replaceRange(index, index + 1, char.toString()), damageCounts)

private fun Line.hasPlaceholder() = line.contains('?')
private fun Line.validPlaceholderReplacedOptions(index: Int) =
    listOf(replaceSpring(index, '#'), replaceSpring(index, '.')).filter { it.isValid() != false }

private val invalidCache = mutableSetOf<String>()
private val validCache = mutableSetOf<String>()

private fun Line.isValid(): Boolean? {
    if (invalidCache.contains("$line|${damageCounts.firstOrNull()}")) {
//        println("CACHE HIT")
        return false
    }

    if (validCache.contains("$line|$damageCounts")) {
//        println("CACHE HIT VALID")
        return true
    }

    if (damageCounts.isEmpty()) {
        val valid = !line.contains('#')
        if (valid) validCache.add("$line|$damageCounts")
        else invalidCache.add("$line|${damageCounts.firstOrNull()}")
        return valid
    }

    val pattern = if (damageCounts.size > 1) "^([.]*?[#]{${damageCounts.first()}}[.]{1}).*"
    else "^([.]*?[#]{${damageCounts.first()}}).*"

    val matches = Regex(pattern).find(line)
    if (matches == null) {
        val newPattern = if (damageCounts.size > 1) "^([.?]*?[#?]{${damageCounts.first()}}[.?]{1}).*"
        else "^([.?]*?[#?]{${damageCounts.first()}}).*"

        val newMatches = Regex(newPattern).find(line)
        if (newMatches != null) return null
        invalidCache.add("$line|${damageCounts.firstOrNull()}")
        return false
    }

    val valid = Line(line.removeRange(matches.groups[1]!!.range), damageCounts.drop(1)).isValid()
    if (valid == true) validCache.add("$line|$damageCounts")
    else if (valid == false) invalidCache.add("$line|${damageCounts.firstOrNull()}")

    return valid
}

fun part1(input: String): Int {
    val lines = input.lines().map { line ->
        val (springs, damageCounts) = line.split(" ")
        Line(springs, damageCounts.split(",").map { it.toInt() })
    }.toMutableList()

    return lines.sumOf { line ->
        fun dive(newLine: Line): Int {
            if (!newLine.hasPlaceholder()) {
                return if (newLine.isValid() == true) 1
                else 0
            }
            val placeholderIndex = newLine.line.indexOf('?')
            if (placeholderIndex == -1) return 0
            val validOptions = newLine.validPlaceholderReplacedOptions(placeholderIndex)
            var count = 0
            for (valid in validOptions) {
                count += dive(valid)
            }
            return count
        }

        val t = dive(line)
        println(line)
        println(t)
        t
    }
}

fun part2(input: String): Int {
    val lines = input.lines().map { line ->
        val (springs, damageCounts) = line.split(" ")
        val unfoldedDamageCounts = List(5) { damageCounts }.joinToString(",")
        Line(List(5) { springs }.joinToString("?"), unfoldedDamageCounts.split(",").map { it.toInt() })
    }.toMutableList()

    return lines.sumOf { line ->
        fun dive(newLine: Line): Int {
            if (!newLine.hasPlaceholder()) {
                return if (newLine.isValid() == true) 1
                else 0
            }
            val placeholderIndex = newLine.line.indexOf('?')
            if (placeholderIndex == -1) return 0
            val validOptions = newLine.validPlaceholderReplacedOptions(placeholderIndex)
            var count = 0
            for (valid in validOptions) {
                count += dive(valid)
            }
            return count
        }

        val t = dive(line)
        println(line)
        println(t)
        t
    }
}