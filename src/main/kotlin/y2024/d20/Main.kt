package y2024.d20

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d20/Input-test.txt"
        "src/main/kotlin/y2024/d20/Input.txt"
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
    lateinit var currentNode: Node
    val map = input.lines().mapIndexed { rowIndex, row ->
        row.toCharArray().toList().mapIndexed { colIndex, c ->
            if (c == 'S') {
                currentNode = Node(rowIndex, colIndex)
                '.'
            } else if (c == 'E') '.'
            else c
        }
    }

    val shortestPaths = currentNode.shortestPath(map) { nodeValue -> nodeValue == '#' }
    val validCheats = mutableSetOf<Pair<Node, Node>>()
    map.forEachNode { cheatStartNode, c ->
        if (c == '.') {
            BaseDirection.entries.map { cheatStartNode.move(it) }
                .flatMap { item -> BaseDirection.entries.map { item.move(it) } }
                .filter { cheatEndNode ->
                    map.atNodeSafe(cheatEndNode) == '.'
                }
                .filter { cheatEndNode ->
                    shortestPaths.getValue(cheatEndNode) - shortestPaths.getValue(cheatStartNode) > 100
                }
                .forEach { cheatEndNode ->
                    if (map.atNodeSafe(cheatEndNode) == '.') {
                        validCheats.add(cheatStartNode to cheatEndNode)
                    }
                }
        }
    }

    return validCheats.size
}

private fun part2(input: String): Int {
    lateinit var currentNode: Node
    val map = input.lines().mapIndexed { rowIndex, row ->
        row.toCharArray().toList().mapIndexed { colIndex, c ->
            if (c == 'S') {
                currentNode = Node(rowIndex, colIndex)
                '.'
            } else if (c == 'E') {
                '.'
            } else c
        }
    }

    val shortestPaths = currentNode.shortestPath(map) { nodeValue -> nodeValue == '#' }
    val validCheats = mutableSetOf<Pair<Node, Node>>()

    shortestPaths.keys.forEach { cheatStartNode ->
        var cheatEndNodes = setOf(cheatStartNode)
        for (n in 1..20) {
            cheatEndNodes = cheatEndNodes.flatMap { item -> BaseDirection.entries.map { item.move(it) } }.filter { it.isInMap(map) }.toSet()
            validCheats.addAll(
                cheatEndNodes.filter { cheatEndNode ->
                    map.atNode(cheatEndNode) == '.'
                }.filter { cheatEndNode ->
                    val save = shortestPaths.getValue(cheatEndNode) - shortestPaths.getValue(cheatStartNode) - n
                    save >= 100
                }.map { cheatEndNode ->
                    cheatStartNode to cheatEndNode
                }
            )
        }

    }

    return validCheats.size
}
