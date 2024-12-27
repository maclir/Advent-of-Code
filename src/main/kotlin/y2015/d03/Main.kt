package y2015.d03

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d03/Input-test.txt"
        "src/main/kotlin/y2015/d03/Input.txt"
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
    var currentNode = Node(0, 0)
    val set = mutableSetOf(currentNode)
    input.toCharArray().map {
        it.toDirection()
    }.forEach {
        currentNode = currentNode.move(it)
        set.add(currentNode)
    }

    return set.size
}

private fun part2(input: String): Int {
    var currentNode = Node(0, 0)
    var currentNode2 = Node(0, 0)
    val set = mutableSetOf(currentNode)
    input.toCharArray().map {
        it.toDirection()
    }.forEachIndexed { index, it ->
        if (index % 2 == 0) {
            currentNode = currentNode.move(it)
            set.add(currentNode)
        } else {
            currentNode2 = currentNode2.move(it)
            set.add(currentNode2)
        }
    }

    return set.size
}