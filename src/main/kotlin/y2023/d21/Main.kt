package y2023.d21

import Direction
import Node
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d21/Input-test.txt"
        "src/main/kotlin/y2023/d21/Input.txt"
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


private fun part1(input: String): Int {
    lateinit var startPosition: Node
    val map = input.lines().mapIndexed { rowIndex, row ->
        val line = row.toCharArray().toList()
        val sCol = line.indexOf('S')
        if (sCol > -1) {
            startPosition = Node(rowIndex, sCol)
        }
        line
    }

    val maxDistance = 64

    val maxRow = map.size - 1
    val maxCol = map[0].size - 1

    val queue = mutableListOf<Pair<Node, Int>>()
    queue.add(startPosition to 0)
    val visited = mutableSetOf<Node>()
    while (queue.isNotEmpty()) {
        val (position, step) = queue.removeFirst()
        if (step > maxDistance) break
        if (position in visited) continue
        if (step % 2 == 0) visited.add(position)
        queue.addAll(Direction.values().mapNotNull { it.move(position, maxRow, maxCol) }.filter { map[it.row][it.col] != '#' }.map { it to step + 1 })
    }

    return visited.size
}


private fun part2(input: String): Long {
    lateinit var startPosition: Node
    val map = input.lines().mapIndexed { rowIndex, row ->
        val line = row.toCharArray().toList()
        val sCol = line.indexOf('S')
        if (sCol > -1) {
            startPosition = Node(rowIndex, sCol)
        }
        line
    }

    val maxDistance = 26501365L

    val maxRow = map.size - 1
    val maxCol = map[0].size - 1

    val queue = mutableListOf<Pair<Node, Long>>()
    queue.add(startPosition to 0)
    val visited = mutableSetOf<Node>()

    var delta = 0L
    var skip = 0L
    val cycle = maxDistance / maxRow

    var lastStep = 0L
    var previousPlots = 0L
    var delta1 = 0L
    var delta2 = 0L
    var plots = 0L

    while (queue.isNotEmpty()) {
        val (position, step) = queue.removeFirst()
        if (position in visited) continue
        if (step % 2L == 1L) visited.add(position)
        if (step % cycle == 66L && step > lastStep) {
            lastStep = step
            if (plots - previousPlots - delta1 == delta2) {
                delta = plots - previousPlots + delta2
                skip = step - 1
                break
            }
            delta2 = (plots - previousPlots) - delta1
            delta1 = plots - previousPlots
            previousPlots = plots
        }
        plots = visited.size.toLong()
        queue.addAll(Direction.values().mapNotNull { it.move(position, maxRow, maxCol) }.filter { map[it.row][it.col] != '#' }.map { it to step + 1 })

    }
    while (skip < maxDistance) {
        skip += cycle
        plots += delta
        delta += delta2
    }
    return plots
}
