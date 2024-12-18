package y2024.d18

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d18/Input-test.txt"
        "src/main/kotlin/y2024/d18/Input.txt"
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
//    val (fallingBytes, gridLastIndex) = 12 to 6
    val (fallingBytes, gridLastIndex) = 1024 to 70
    val map = List(gridLastIndex + 1) { MutableList(gridLastIndex + 1) { '.' } }
    input.intLines(",").map { (a, b) -> Node(a, b) }.subList(0, fallingBytes).forEach { node ->
        map.setNode(node, '#')
    }
    return Node(0, 0).shortestPath(map, Node(gridLastIndex, gridLastIndex)) { nodeValue -> nodeValue == '#' }
}

private fun part2(input: String): Int {
//    val gridLastIndex = 6
    val gridLastIndex = 70
    val map = List(gridLastIndex + 1) { MutableList(gridLastIndex + 1) { '.' } }
    input.intLines(",").map { (a, b) -> Node(a, b) }.forEach { node ->
        map.setNode(node, '#')
        if (Node(0, 0).shortestPath(map, Node(gridLastIndex, gridLastIndex)) { nodeValue -> nodeValue == '#' } == -1) {
            println("${node.row},${node.col}")
            return 0
        }
    }
    return -1
}