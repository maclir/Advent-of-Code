package y2024.d14

import utilities.Node
import utilities.intLines
import utilities.longLines
import java.io.File
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d14/Input-test.txt"
        "src/main/kotlin/y2024/d14/Input.txt"
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

private data class Robot(
    var p: Node,
    val rowSpeed: Int,
    val colSpeed: Int,
)

private fun part1(input: String): Int {
    val robots = input.lines().map { line ->
        """p=([0-9]+),([0-9]+) v=([\-0-9]+),([\-0-9]+)""".toRegex().find(line)!!.groupValues
            .drop(1).map {
                it.toInt()
            }
    }.map {
        Robot(
            Node(it[1], it[0]),
            it[3],
            it[2],
        )
    }

//    val map = List(7) { List(11) {} }
    val map = List(103) { List(101) {} }

    repeat(100) {
        robots.forEach { robot ->
            robot.p = robot.p.warpInMapMove(map, robot.rowSpeed, robot.colSpeed)
        }
    }

    val quadrantCounts = MutableList(4) { 0 }
    val middleRow = (map.size - 1) / 2
    val middleCol = (map[0].size - 1) / 2
    robots.forEach { robot ->
        when {
            robot.p.row < middleRow && robot.p.col < middleCol -> quadrantCounts[0]++
            robot.p.row < middleRow && robot.p.col > middleCol -> quadrantCounts[1]++
            robot.p.row > middleRow && robot.p.col < middleCol -> quadrantCounts[2]++
            robot.p.row > middleRow && robot.p.col > middleCol -> quadrantCounts[3]++
        }
    }
    var safetyFactor = 1
    quadrantCounts.forEach {
        safetyFactor *= it
    }
    return safetyFactor
}

private fun part2(input: String): Int {
    val robots = input.lines().map { line ->
        """p=([0-9]+),([0-9]+) v=([\-0-9]+),([\-0-9]+)""".toRegex().find(line)!!.groupValues
            .drop(1).map {
                it.toInt()
            }
    }.map {
        Robot(
            Node(it[1], it[0]),
            it[3],
            it[2],
        )
    }

    val map = List(103) { List(101) {} }

    var i = 0
    while(true) {
        robots.forEach { robot ->
            robot.p = robot.p.warpInMapMove(map, robot.rowSpeed, robot.colSpeed)
        }
        i++
        if ((i - 63) % 103 != 0 && (i - 82) % 101 != 0) continue
        var frame = "Round: ${i}\n"
        map.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                frame += if (robots.any { it.p == Node(rowIndex, colIndex) }) '#'
                else ' '
            }
            frame += '\n'
        }
        println(frame)
        Thread.sleep(300)
    }
}