package y2024.d21

import utilities.*
import java.io.File
import java.math.BigInteger
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
        shortestDirectionPadPathDirections[node] = node.shortestPathsDirections(directionPad) { it == '#' }
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

private fun part2(input: String): BigInteger {
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

    val shortestKeypadPathDirections = mutableMapOf<Node, Map<Node, List<List<BaseDirection?>>>>()
    keypad.forEachNode { node, c ->
        if (c == '#') return@forEachNode
        shortestKeypadPathDirections[node] = node.shortestPathsDirections(keypad) { it == '#' }.mapValues {
            it.value.map {
                val withA = mutableListOf<BaseDirection?>()
                withA.addAll(it)
                withA.add(null)
                withA
            }
        }
    }

    val shortestDirectionPadPathDirections = mutableMapOf<Node, Map<Node, List<List<BaseDirection?>>>>()
    directionPad.forEachNode { node, c ->
        if (c == '#') return@forEachNode
        shortestDirectionPadPathDirections[node] = node.shortestPathsDirections(directionPad) { it == '#' }.mapValues {
            it.value.map {
                val withA = mutableListOf<BaseDirection?>()
                withA.addAll(it)
                withA.add(null)
                withA
            }.ifEmpty {
                listOf(listOf(null))
            }
        }
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
        val expandedLength = codeNodes.map { targetNode ->
            val possiblePaths = shortestKeypadPathDirections.getValue(currentKeypadNode).getValue(targetNode)
            currentKeypadNode = targetNode
            possiblePaths
        }.sumOf { codeNodeDirectionOptions ->
            codeNodeDirectionOptions.minOfOrNull { directionList ->
                var currentNode = directionPadNodes.getValue('A')
                directionList.sumOf { targetDirection ->
                    val targetNode = directionPadNodes.getValue(targetDirection.toChar())
                    val innerExpandedLength = getExpandedLength(
                        currentNode,
                        targetNode,
                        25,
                        directionPadNodes,
                        shortestDirectionPadPathDirections
                    )
                    currentNode = targetNode
                    innerExpandedLength
                }
            } ?: throw Exception()
        }
        expandedLength * code.substring(0, 3).toBigInteger()
    }
}

private val cache = mutableMapOf<Triple<Node, Node, Int>, BigInteger>()
private fun getExpandedLength(
    currentNode: Node,
    targetNode: Node,
    depth: Int,
    directionPadNodes: Map<Char, Node>,
    shortestPaths: Map<Node, Map<Node, List<List<BaseDirection?>>>>
): BigInteger {
    val cacheKey = Triple(currentNode, targetNode, depth)
    if (cache.contains(cacheKey)) return cache.getValue(cacheKey)

    val possiblePaths = shortestPaths.getValue(currentNode).getValue(targetNode)
    val returnValue = if (depth == 1) {
        possiblePaths.first().size.toBigInteger()
    } else {
        possiblePaths.minOfOrNull { possiblePath ->
            var cNode = directionPadNodes.getValue('A')
            possiblePath.sumOf {
                val tNode = directionPadNodes.getValue(it.toChar())
                val nextDepthScore =
                    getExpandedLength(cNode, tNode, depth - 1, directionPadNodes, shortestPaths)
                cNode = tNode
                nextDepthScore
            }
        } ?: throw Exception()
    }
    cache[cacheKey] = returnValue
    return returnValue
}