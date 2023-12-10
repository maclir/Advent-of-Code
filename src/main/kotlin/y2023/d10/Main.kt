package y2023.d10

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d10/Input-test.txt"
        "src/main/kotlin/y2023/d10/Input.txt"
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

enum class Pipe {
    S {
        override fun connectableDirections() = listOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
        override fun toString() = "S"
    },
    I {
        override fun connectableDirections() = listOf(Direction.NORTH, Direction.SOUTH)
        override fun toString() = "|"
    },
    STRAIGHT {
        override fun connectableDirections() = listOf(Direction.WEST, Direction.EAST)
        override fun toString() = "-"
    },
    L {
        override fun connectableDirections() = listOf(Direction.SOUTH, Direction.WEST)
        override fun toString() = "L"
    },
    J {
        override fun connectableDirections() = listOf(Direction.SOUTH, Direction.EAST)
        override fun toString() = "J"
    },
    SEVEN7 {
        override fun connectableDirections() = listOf(Direction.NORTH, Direction.EAST)
        override fun toString() = "7"
    },
    F {
        override fun connectableDirections() = listOf(Direction.NORTH, Direction.WEST)
        override fun toString() = "F"
    },
    DOT {
        override fun connectableDirections() = emptyList<Direction>()
        override fun toString() = "."
    };

    abstract fun connectableDirections(): List<Direction>
}

data class Position(val row: Int, val column: Int) {
    fun move(direction: Direction) = when (direction) {
        Direction.NORTH -> Position(row - 1, column)
        Direction.SOUTH -> Position(row + 1, column)
        Direction.WEST -> Position(row, column - 1)
        Direction.EAST -> Position(row, column + 1)
    }


}

enum class Direction { NORTH, SOUTH, WEST, EAST }

fun List<List<Pipe>>.next(currentPosition: Position, direction: Direction): Pipe? {
    val nextPosition = currentPosition.move(direction)
    return if (nextPosition.row < 0 || nextPosition.column < 0 || nextPosition.row >= this.size || nextPosition.column >= this[0].size) null
    else this[nextPosition.row][nextPosition.column]
}

fun List<List<Pipe>>.get(position: Position): Pipe {
    return this[position.row][position.column]
}

fun Array<Array<Pipe>>.get(position: Position): Pipe {
    return this[position.row][position.column]
}

fun part1(input: String): Int {
    var currentPosition = Position(0, 0)
    val grid = input.lines().mapIndexed { row, line ->
        val chars = line.toCharArray().map {
            when (it) {
                'S' -> Pipe.S
                '|' -> Pipe.I
                '-' -> Pipe.STRAIGHT
                'L' -> Pipe.L
                'J' -> Pipe.J
                '7' -> Pipe.SEVEN7
                'F' -> Pipe.F
                else -> Pipe.DOT
            }
        }
        val index = chars.indexOf(Pipe.S)
        if (index != -1) {
            currentPosition = Position(row, index)
        }
        chars
    }

    var currentChar = Pipe.S
    var currentMoveDirection = Direction.NORTH
    var step = 0
    do {
        step++
        currentMoveDirection = when (currentChar) {
            Pipe.S -> Direction.values().first {
                val next = grid.next(currentPosition, it)
                next != null && next != Pipe.DOT && next.connectableDirections().contains(it)
            }

            Pipe.I -> currentMoveDirection

            Pipe.STRAIGHT -> currentMoveDirection

            Pipe.L -> if (currentMoveDirection == Direction.SOUTH) Direction.EAST
            else Direction.NORTH

            Pipe.J -> if (currentMoveDirection == Direction.SOUTH) Direction.WEST
            else Direction.NORTH

            Pipe.SEVEN7 -> if (currentMoveDirection == Direction.NORTH) Direction.WEST
            else Direction.SOUTH

            Pipe.F -> if (currentMoveDirection == Direction.NORTH) Direction.EAST
            else Direction.SOUTH

            Pipe.DOT -> currentMoveDirection
        }
        currentPosition = currentPosition.move(currentMoveDirection)
        currentChar = grid.get(currentPosition)
    } while (currentChar != Pipe.S)

    return step / 2
}

fun part2(input: String): Int {
    var currentPosition = Position(0, 0)
    val grid = input.lines().mapIndexed { row, line ->
        val chars = line.toCharArray().map {
            when (it) {
                'S' -> Pipe.S
                '|' -> Pipe.I
                '-' -> Pipe.STRAIGHT
                'L' -> Pipe.L
                'J' -> Pipe.J
                '7' -> Pipe.SEVEN7
                'F' -> Pipe.F
                else -> Pipe.DOT
            }
        }
        val index = chars.indexOf(Pipe.S)
        if (index != -1) {
            currentPosition = Position(row, index)
        }
        chars
    }

    val parsedGrid = Array(grid.size) { Array(grid[0].size) { Pipe.DOT } }
    parsedGrid[currentPosition.row][currentPosition.column] = Pipe.S
    var currentChar = Pipe.S
    var currentMoveDirection = Direction.NORTH
    var step = 0
    do {
        step++
        currentMoveDirection = when (currentChar) {
            Pipe.S -> Direction.values().first {
                val next = grid.next(currentPosition, it)
                next != null && next != Pipe.DOT && next.connectableDirections().contains(it)
            }

            Pipe.I -> currentMoveDirection

            Pipe.STRAIGHT -> currentMoveDirection

            Pipe.L -> if (currentMoveDirection == Direction.SOUTH) Direction.EAST
            else Direction.NORTH

            Pipe.J -> if (currentMoveDirection == Direction.SOUTH) Direction.WEST
            else Direction.NORTH

            Pipe.SEVEN7 -> if (currentMoveDirection == Direction.NORTH) Direction.WEST
            else Direction.SOUTH

            Pipe.F -> if (currentMoveDirection == Direction.NORTH) Direction.EAST
            else Direction.SOUTH

            Pipe.DOT -> currentMoveDirection
        }
        currentPosition = currentPosition.move(currentMoveDirection)
        currentChar = grid.get(currentPosition)
        parsedGrid[currentPosition.row][currentPosition.column] = currentChar
    } while (currentChar != Pipe.S)

    var count = 0
    parsedGrid.forEachIndexed { rowIndex, row ->
        row.forEachIndexed rowLoop@{ columnIndex, node ->
            if (node != Pipe.DOT) return@rowLoop

            var leftCount = 0
            var previousIncomingVerticalDirection: Direction? = null
            for (i in columnIndex - 1 downTo 0) {
                if (parsedGrid[rowIndex][i] == Pipe.DOT) {
                    previousIncomingVerticalDirection = null
                    continue
                }

                when (parsedGrid[rowIndex][i]) {
                    Pipe.I -> leftCount++

                    Pipe.L -> {
                        when (previousIncomingVerticalDirection) {
                            Direction.NORTH -> {
                                leftCount++
                                previousIncomingVerticalDirection = null
                            }

                            Direction.SOUTH -> {
                                previousIncomingVerticalDirection = null
                            }

                            else -> {}
                        }
                    }

                    Pipe.J -> previousIncomingVerticalDirection = Direction.SOUTH
                    Pipe.SEVEN7 -> previousIncomingVerticalDirection = Direction.NORTH

                    Pipe.F -> {
                        when (previousIncomingVerticalDirection) {
                            Direction.NORTH -> previousIncomingVerticalDirection = null

                            Direction.SOUTH -> {
                                leftCount++
                                previousIncomingVerticalDirection = null
                            }

                            else -> {}
                        }
                    }

                    else -> {}
                }
            }
            if (leftCount % 2 == 1) {
                count++
            }
        }
    }

    return count
}
