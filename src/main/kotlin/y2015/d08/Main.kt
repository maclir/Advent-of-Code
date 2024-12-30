package y2015.d08

import utilities.Node
import utilities.charGrid
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d08/Input-test.txt"
        "src/main/kotlin/y2015/d08/Input.txt"
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
        .map { line ->
            line.replace("""\s+""".toRegex(), "")
        }
        .map { line ->
            val output = line
                .removePrefix("\"")
                .removeSuffix("\"")
                .replace("""\\""","""\""")
                .replace("""\"""",""""""")
                .replace("""\\x[a-fA-F0-9]{2}""".toRegex(), "x")
            line to output
        }
        .sumOf { (line, output) ->
            line.length - output.length
        }
}

private fun part2(input: String): Int {
    return input.lines()
        .map { line ->
            line.replace("""\s+""".toRegex(), "")
        }
        .map { line ->
            val output =
                """"""".plus(
                    line
                    .replace("""\""", """\\""")
                    .replace(""""""", """\"""")
                )
                .plus(""""""")
            line to output
        }
        .sumOf { (line, output) ->
            println(line)
            println(output)
            println()
            output.length - line.length
        }
}