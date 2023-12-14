package y2022.d10

import java.io.File
import kotlin.math.abs

private fun main() {
    val input = File("src/main/kotlin/y2022/d10/Input.txt").readLines(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d10/Input-test.txt").readLines(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: List<String>): Int {
    var lineCounter = 0
    var registerX = 1
    val screen = Array(6) { CharArray(40) }
    var cycleW = 0
    for (cycle in 1..240) {
        val pixel = (cycle - 1) % 40
        val pixelRow = (cycle - 1) / 40
        screen[pixelRow][pixel] = if (abs(pixel - registerX) <= 1) '#'
        else '.'

        if (input[lineCounter].startsWith("addx")) {
            val (_, qn) = input[lineCounter].split(" ")
            if (cycleW == 1) {
                registerX += qn.toInt()
                cycleW = 0
            } else {
                cycleW++
            }
        }

        if (cycleW == 0) {
            lineCounter++
        }
    }

    screen.forEach { row ->
        row.forEach { p ->
            print(p)
        }
        println()
    }
    return 0
}

private fun part1(input: List<String>): Int {
    var lineCounter = 0
    var registerX = 1
    val signals = mutableMapOf<Int, Int>()
    var cycleW = 0
    for (cycle in 1..220) {
        signals[cycle] = registerX
        if (input[lineCounter].startsWith("addx")) {
            val (_, qn) = input[lineCounter].split(" ")
            if (cycleW == 1) {
                registerX += qn.toInt()
                cycleW = 0
            } else {
                cycleW++
            }
        }

        if (cycleW == 0) {
            lineCounter++
        }
    }

    return signals.filter { (it.key - 20) % 40 == 0 }.entries.fold(0) { acc, entry -> acc + entry.value * entry.key }
}
