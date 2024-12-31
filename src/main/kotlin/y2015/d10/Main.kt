package y2015.d10

import java.io.File
import kotlin.time.measureTimedValue

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d10/Input-test.txt"
        "src/main/kotlin/y2015/d10/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun("part1") { part1(input) }
    printRun("part2") { part2(input) }
}

private fun printRun(functionName: String, function: () -> Int) {
    val (result, duration) = measureTimedValue {
        function()
    }

    println("$functionName: ${result}\n${duration.inWholeMilliseconds} ms")
    if (duration.inWholeSeconds > 0) {
        println("${duration.inWholeSeconds} s")
    }
    if (duration.inWholeMinutes > 0) {
        println("${duration.inWholeMinutes} m")
    }
    println()
}

private fun part1(input: String): Int {
    var output = input
    repeat(40) {
        output = lookAndSay(output)
    }
    return output.length
}

private fun part2(input: String): Int {
    var output = input
    repeat(50) {
        output = lookAndSay(output)
    }
    return output.length
}

private fun lookAndSay(input: String): String {
    var loopOutput = ""
    var index = 0
    while (index <= input.lastIndex) {
        val digit = input[index]
        var toIndex = index
        while (toIndex < input.lastIndex && input[toIndex + 1] == digit) toIndex++
        loopOutput += "${toIndex - index + 1}$digit"
        index = toIndex + 1
    }

    return loopOutput
}