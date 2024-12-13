package y2024.d13

import java.io.File
import kotlin.math.min
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d13/Input-test.txt"
        "src/main/kotlin/y2024/d13/Input.txt"
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

private data class Pos(
    val row: Long,
    val col: Long,
)
private data class Machine(
    val a: Pos,
    val b: Pos,
    val prize: Pos,
)

private operator fun Pos.plus(l: Long) = Pos(row + l, col + l)
private operator fun Pos.minus(other: Pos) = Pos(row - other.row, col - other.col)
private operator fun Pos.times(i: Int) = Pos(row * i, col * i)
private operator fun Pos.div(l: Long) = Pos(row / l, col / l)
private operator fun Pos.rem(l: Long) = Pos(row % l, col % l)

private fun part1(input: String): Int {
    val machines = input.split("\n\n").map { machine ->
        val nodes = machine.split("\n").map { row ->
            val groupValues = """([0-9]+).{4}([0-9]+)""".toRegex().findAll(row).first().groupValues
            Pos(groupValues[1].toLong(), groupValues[2].toLong())
        }
        Machine(nodes[0], nodes[1], nodes[2])
    }

    return machines.sumOf { machine ->
        var minimumCost: Int? = null
        for (bTokens in 100 downTo 0) {
            val bRemainder = machine.prize - (machine.b * bTokens)
            for (aTokens in 100 downTo 0) {
                val remainder = bRemainder - (machine.a * aTokens)
                if (remainder != Pos(0, 0)) continue

                val newCost = aTokens * 3 + bTokens
                if (minimumCost == null) minimumCost = newCost
                else minimumCost = min(minimumCost, newCost)
            }
        }
        minimumCost ?: 0
    }
}

private fun part2(input: String): Long {
    val machines = input.split("\n\n").map { machine ->
        val nodes = machine.split("\n").map { row ->
            val groupValues = """([0-9]+).{4}([0-9]+)""".toRegex().findAll(row).first().groupValues
            Pos(groupValues[1].toLong(), groupValues[2].toLong())
        }
        Machine(nodes[0], nodes[1], nodes[2] + 10000000000000)
    }

    return machines.sumOf { machine ->
        val a = (machine.prize.row*machine.b.col - machine.b.row*machine.prize.col) / (machine.a.row*machine.b.col - machine.b.row*machine.a.col)
        val b = (machine.prize.row*machine.a.col - machine.a.row*machine.prize.col) / (machine.b.row*machine.a.col - machine.a.row*machine.b.col)

        val aR = (machine.prize.row*machine.b.col - machine.b.row*machine.prize.col) % (machine.a.row*machine.b.col - machine.b.row*machine.a.col)
        val bR = (machine.prize.row*machine.a.col - machine.a.row*machine.prize.col) % (machine.b.row*machine.a.col - machine.a.row*machine.b.col)

        if (aR == 0L && bR == 0L) a * 3 + b
        else 0L
    }
}
