package y2024.d12

import utilities.*
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d12/Input-test.txt"
        "src/main/kotlin/y2024/d12/Input.txt"
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

private data class Fence(
    val name: Char,
    val nodes: MutableSet<Node> = mutableSetOf(),
    val edgeNodes: MutableSet<Pair<Node, BaseDirection>> = mutableSetOf(),
    var area: Int = 0,
    var perimeter: Int = 0,
)

private fun part1(input: String): Int {
    val grid = input.charGrid()
    val fences = mutableListOf<Fence>()

    grid.forEachNode { node, c ->
        if (fences.any { it.nodes.contains(node) }) return@forEachNode
        val fence = Fence(c)
        getFence(grid, node, fence)
        fences.add(fence)
    }

    return fences.sumOf { fence ->
        fence.area * fence.perimeter
    }
}

private fun getFence(grid: List<List<Char>>, currentNode: Node, fence: Fence) {
    if (fence.nodes.contains(currentNode)) return
    fence.nodes.add(currentNode)
    fence.area++
    fence.perimeter += currentNode.adjacent().filter { nextNode ->
        grid.atNodeSafe(nextNode) != grid.atNode(currentNode)
    }.size

    currentNode.adjacent().filter { nextNode ->
        !fence.nodes.contains(nextNode)
    }.filter { nextNode ->
        grid.atNodeSafe(nextNode) == grid.atNode(currentNode)
    }.forEach { nextNode ->
        getFence(grid, nextNode, fence)
    }
}

private fun part2(input: String): Int {
    val grid = input.charGrid()
    val fences = mutableListOf<Fence>()

    grid.forEachNode { node, c ->
        if (fences.any { it.nodes.contains(node) }) return@forEachNode
        val fence = Fence(c)
        getFence2(grid, node, fence)
        fences.add(fence)
    }

    fences.forEach { fence ->
        var changed = true
        while (changed) {
            changed = fence.edgeNodes.removeIf { edgeNode ->
                edgeNode.first.adjacent().filter { fence.edgeNodes.contains(it to edgeNode.second) }.size  == 1
            }
        }
    }


    return fences.sumOf { fence ->
        fence.area * fence.edgeNodes.size
    }
}

private fun getFence2(grid: List<List<Char>>, currentNode: Node, fence: Fence) {
    if (fence.nodes.contains(currentNode)) return
    fence.nodes.add(currentNode)
    val c = grid.atNode(currentNode)

    fence.area++

    BaseDirection.entries.forEach { direction ->
        if (grid.atNodeSafe(currentNode.move(direction)) != c) fence.edgeNodes.add(currentNode to direction)
    }

    currentNode.adjacent().filter { nextNode ->
        !fence.nodes.contains(nextNode)
    }.filter { nextNode ->
        grid.atNodeSafe(nextNode) == c
    }.forEach { nextNode ->
        getFence2(grid, nextNode, fence)
    }
}