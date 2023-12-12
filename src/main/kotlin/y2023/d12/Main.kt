package y2023.d12

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d12/Input-test.txt"
        "src/main/kotlin/y2023/d12/Input.txt"
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

//private val cache = mutableSetOf<String>()
data class InnerLine(val line: String)  {
    override fun equals(other: Any?): Boolean {
        if (other !is InnerLine) return false
//        if (cache.contains("$line|${other.line}")) return true
        for (index in line.indices) {
            if (line[index] != '?' && other.line[index] != '?' && line[index] != other.line[index]) return false
        }
//        cache.add("$line|${other.line}")
//        cache.add("${other.line}|$line")
        return true
    }

    override fun hashCode(): Int {
        return line.hashCode()
    }

}
data class Line(val line: InnerLine, val damageCounts: List<Int>)

private fun Line.getPossibleLinesCount(): Int {
    var allPossibleCount = 0
    fun dive(diveLine: InnerLine, diveDamageCounts: List<Int>, startIndex: Int) {
        var index = startIndex
        while (true) {
            if (index + diveDamageCounts.sum() + diveDamageCounts.count() - 1 > diveLine.line.length) return
            if (InnerLine(diveLine.line.substring(0, index)) != InnerLine(line.line.substring(0, index))) return
            if (InnerLine(diveLine.line.substring(index, diveLine.line.length).replace('.', '?')) != InnerLine(line.line.substring(index, diveLine.line.length))) return

            val endIndex = index + diveDamageCounts.first()
            val newDiveLine =
                InnerLine(diveLine.line.replaceRange(index, endIndex, CharArray(diveDamageCounts.first()) { '#' }.joinToString("")))

            if (diveDamageCounts.size > 1) {
                dive(newDiveLine, diveDamageCounts.drop(1), endIndex + 1)
            } else {
                if (line == newDiveLine) allPossibleCount++
            }
            index++
        }
    }
    dive(InnerLine(CharArray(line.line.length) { '.' }.joinToString("")), damageCounts, 0)
    return allPossibleCount
}

fun part1(input: String): Int {
    return input.lines().map { line ->
        val (springs, damageCounts) = line.split(" ")
        Line(InnerLine(springs), damageCounts.split(",").map { it.toInt() })
    }.sumOf { line ->
        val count = line.getPossibleLinesCount()
        println("$line: $count")
        count
    }
}

fun part2(input: String): Long {
    return input.lines().map { line ->
        val (springs, damageCounts) = line.split(" ")
        val unfoldedDamageCounts = List(5) { damageCounts }.joinToString(",")
        Line(InnerLine(List(5) { springs }.joinToString("?")), unfoldedDamageCounts.split(",").map { it.toInt() })
    }.sumOf { line ->
        val count = line.getPossibleLinesCount().toLong()
        println("$line: $count")
        count
    }
}