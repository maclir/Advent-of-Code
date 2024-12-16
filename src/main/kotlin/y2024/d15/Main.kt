package y2024.d15

import utilities.*
import java.io.File
import javax.swing.text.Position
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2024/d15/Input-test.txt"
        "src/main/kotlin/y2024/d15/Input.txt"
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
    val (mapInput, moveInput) = input.split("\n\n")
    val map = mapInput.lines().mapIndexed { rowIndex, row ->
        row.toCharArray().toList().mapIndexed { colIndex, c ->
            if (c == '@') {
                currentNode = Node(rowIndex, colIndex)
                '.'
            } else c
        }.toMutableList()
    }

    val moves = moveInput.charGrid().flatten().map {
        when (it) {
            '^' -> BaseDirection.UP
            '>' -> BaseDirection.RIGHT
            '<' -> BaseDirection.LEFT
            else -> BaseDirection.DOWN
        }
    }

    moves.forEach { direction ->
        val nextNode = currentNode.move(direction)
        when (map.atNodeSafe(nextNode)) {
            '.' -> currentNode = nextNode
            'O' -> {
                if (moveBox(map, direction, nextNode)) {
                    currentNode = nextNode
                }
            }

            else -> return@forEach
        }
    }

    var sum = 0
    map.forEachNode { node, c ->
        if (c == 'O') sum += 100 * node.row + node.col
    }

    return sum
}

private fun moveBox(map: List<MutableList<Char>>, direction: BaseDirection, boxNode: Node): Boolean {
    val nextNode = boxNode.move(direction)
    val box = map.atNode(boxNode)
    when (map.atNodeSafe(nextNode)) {
        '.' -> {
            map.setNode(boxNode, '.')
            map.setNode(nextNode, box)
            return true
        }

        'O', '[', ']' -> {
            val moved = moveBox(map, direction, nextNode)
            if (moved) {
                map.setNode(boxNode, '.')
                map.setNode(nextNode, box)
                return true
            }
        }

        else -> return false
    }
    return false
}

private fun part2(input: String): Int {
    lateinit var currentNode: Node
    val (mapInput, moveInput) = input.split("\n\n")
    val map = mapInput.lines().mapIndexed { rowIndex, row ->
        row.replace("#", "##")
            .replace("O", "[]")
            .replace(".", "..")
            .replace("@", "@.")
            .toCharArray().toList().mapIndexed { colIndex, c ->
                if (c == '@') {
                    currentNode = Node(rowIndex, colIndex)
                    '.'
                } else c
            }.toMutableList()
    }

    val moves = moveInput.charGrid().flatten().map {
        when (it) {
            '^' -> BaseDirection.UP
            '>' -> BaseDirection.RIGHT
            '<' -> BaseDirection.LEFT
            else -> BaseDirection.DOWN
        }
    }

    moves.forEach { direction ->
        val nextNode = currentNode.move(direction)
        when (val nextNodeVal = map.atNodeSafe(nextNode)) {
            '.' -> currentNode = nextNode
            '[', ']' -> {
                if (direction.isHorizontal()) {
                    if (moveBox(map, direction, nextNode)) {
                        currentNode = nextNode
                    }
                } else {
                    val box = if (nextNodeVal == '[') nextNode to nextNode.move(BaseDirection.RIGHT)
                    else nextNode.move(BaseDirection.LEFT) to nextNode

                    if (moveBoxesVertically(map, direction, listOf(box))) {
                        currentNode = nextNode
                    }
                }
            }

            else -> return@forEach
        }
    }
    var sum = 0
    map.forEachNode { node, c ->
        if (c == '[') sum += 100 * node.row + node.col
    }

    return sum
}

private fun moveBoxesVertically(
    map: List<MutableList<Char>>,
    direction: BaseDirection,
    boxes: List<Pair<Node, Node>>,
): Boolean {
    val nextBoxes = mutableSetOf<Pair<Node, Node>>()
    boxes.forEach { boxNodes ->
        val nextNodes = listOf(boxNodes.first.move(direction), boxNodes.second.move(direction))
        if (nextNodes.map { map.atNodeSafe(it) }.any { it == '#' }) {
            return false
        }
        nextNodes.filter { map.atNodeSafe(it) == '[' }.forEach {
            nextBoxes.add(it to it.move(BaseDirection.RIGHT))
        }
        nextNodes.filter { map.atNodeSafe(it) == ']' }.forEach {
            nextBoxes.add(it.move(BaseDirection.LEFT) to it)
        }
    }

    if (nextBoxes.isEmpty() || moveBoxesVertically(map, direction, nextBoxes.toList())) {
        boxes.forEach { boxNodes ->
            val nextNodes = boxNodes.first.move(direction) to boxNodes.second.move(direction)
            val box = map.atNode(boxNodes.first) to map.atNode(boxNodes.second)

            map.setNode(boxNodes.first, '.')
            map.setNode(boxNodes.second, '.')
            map.setNode(nextNodes.first, box.first)
            map.setNode(nextNodes.second, box.second)
        }
        return true
    }

    return false
}