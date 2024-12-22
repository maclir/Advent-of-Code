package y2024.d21

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d21/Input-test.txt"
        "src/main/kotlin/y2024/d21/Input.txt"
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
    val keypad = listOf(
        listOf('7', '8', '9'),
        listOf('4', '5', '6'),
        listOf('1', '2', '3'),
        listOf('#', '0', 'A'),
    )

    val directionPad = listOf(
        listOf('#', '^', 'A'),
        listOf('<', 'v', '>'),
    )

    val shortestKeypadPathDirections = mutableMapOf<Node, Map<Node, List<List<BaseDirection>>>>()
    keypad.forEachNode { node, c ->
        if (c == '#') return@forEachNode
        shortestKeypadPathDirections[node] = node.shortestPathsDirections(keypad) { it == '#' }
    }

    val shortestDirectionPadPathDirections = mutableMapOf<Node, Map<Node, List<List<BaseDirection>>>>()
    directionPad.forEachNode { node, c ->
        if (c == '#') return@forEachNode
        shortestDirectionPadPathDirections[node] = node.shortestPathsDirections(keypad) { it == '#' }
    }

    val directionPadNodes = mutableMapOf<Char, Node>()
    directionPad.forEachNode { node, c ->
        directionPadNodes[c] = node
    }


    return input.lines().sumOf { code ->
        val codeNodes = code.map { c ->
            keypad.findNode { it == c }
        }
        var currentKeypadNode = Node(3, 2)
        var currentDirectionPadNode = directionPadNodes.getValue('A')
        var currentDirectionPad2Node = directionPadNodes.getValue('A')
        val expanded = codeNodes.map { targetNode ->
            val possiblePaths = shortestKeypadPathDirections.getValue(currentKeypadNode).getValue(targetNode).map {
                val withA = mutableListOf<BaseDirection?>()
                withA.addAll(it)
                withA.add(null)
                withA
            }
            currentKeypadNode = targetNode
            possiblePaths.toMutableList()
        }.map { directionListOption ->
            directionListOption.map { directionList ->
                directionList.map { targetDirection ->
                    val targetNode = directionPadNodes.getValue(targetDirection.toChar())
                    val possiblePaths =
                        shortestDirectionPadPathDirections.getValue(currentDirectionPadNode)
                            .getValue(targetNode).map {
                                val withA = mutableListOf<BaseDirection?>()
                                withA.addAll(it)
                                withA.add(null)
                                withA
                            }.ifEmpty {
                                listOf(listOf(null))
                            }
                    currentDirectionPadNode = targetNode
                    possiblePaths.toMutableList()
                }
            }
        }.map { directionListOption ->
            directionListOption.map { directionList ->
                directionList.map { directionList2Option ->
                    directionList2Option.map { directionList2 ->
                        directionList2.map { targetDirection ->
                            val targetNode = directionPadNodes.getValue(targetDirection.toChar())
                            val possiblePaths =
                                shortestDirectionPadPathDirections.getValue(currentDirectionPad2Node)
                                    .getValue(targetNode).map {
                                        val withA = mutableListOf<BaseDirection?>()
                                        withA.addAll(it)
                                        withA.add(null)
                                        withA
                                    }.ifEmpty {
                                        listOf(listOf(null))
                                    }
                            currentDirectionPad2Node = targetNode
                            possiblePaths.first()
                        }.flatten()
                    }.minBy { it.size }
                }.flatten()
            }.minBy { it.size }
        }.flatten()

        expanded.size * code.substring(0, 3).toInt()
    }
}

private fun BaseDirection?.toChar() = when (this) {
    BaseDirection.UP -> '^'
    BaseDirection.DOWN -> 'v'
    BaseDirection.RIGHT -> '>'
    BaseDirection.LEFT -> '<'
    null -> 'A'
}

private fun part2(input: String): Int {
    return 0
}
