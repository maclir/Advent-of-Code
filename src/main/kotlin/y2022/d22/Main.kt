package y2022.d22

import java.io.File

fun main() {
    val input = File("src/main/kotlin/y2022/d22/Input-fixed.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d22/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d22/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
//    fixPart2Cube(input)
}

fun fixPart2Cube(input: String) {
    val face = input.lines().map { line -> line.toCharArray().toMutableList() }.toMutableList()
    for (x in 0..49) {
        println()
        for (y in 49 downTo 0) {
            print(face[y][x])
        }
    }
}

fun part2(input: String): Int {
    val gridText = input.split("\n\n")[0].lines().toMutableList()
    gridText.add(0, " ")
    gridText.add(" ")
    val grid = gridText.map { line ->
        val chars = line.toCharArray().toMutableList()
        chars.add(0, ' ')
        chars.add(' ')
        chars
    }

    val maxX = grid.maxOf { it.size }
    grid.forEach { if (it.size < maxX) it.addAll(List(maxX - it.size) { ' ' }) }

    val directions = mutableListOf<Instruction>()
    "[0-9]+|L|R".toRegex().findAll(input.split("\n\n")[1]).map(MatchResult::value).forEach {
        val steps = it.toIntOrNull()
        if (steps != null) directions.add(Walk(steps))
        else directions.add(Turn(it[0]))
    }

    var currentRow = 1
    var currentColumn = grid[1].indexOfFirst { it == '.' }
    var currentDirection = '>'

    directions.forEach { instruction ->
        when (instruction) {
            is Turn -> {
                when (currentDirection) {
                    '>' -> currentDirection = if (instruction.direction == 'R') 'v' else '^'
                    '<' -> currentDirection = if (instruction.direction == 'R') '^' else 'v'
                    'v' -> currentDirection = if (instruction.direction == 'R') '<' else '>'
                    '^' -> currentDirection = if (instruction.direction == 'R') '>' else '<'
                }
            }

            is Walk -> {
                repeat(instruction.steps) {
                    var (newDirection, newRow, newColumn) = moveCube(grid, currentDirection, currentRow, currentColumn)
                    if (grid[newRow][newColumn] == '#') return@forEach
                    if (grid[newRow][newColumn] == '.') {
                        currentRow = newRow
                        currentColumn = newColumn
                        currentDirection = newDirection
                    }
                }
            }
        }
    }

    currentColumn -= 50
    println("row: $currentRow, column: $currentColumn, direction: $currentDirection")

    val directionValue = when (currentDirection) {
        '>' -> 0
        'v' -> 1
        '<' -> 2
        '^' -> 3
        else -> -1
    }
    return 1000 * currentRow + 4 * currentColumn + directionValue
}

fun moveCube(grid: List<MutableList<Char>>, direction: Char, row: Int, column: Int): Triple<Char, Int, Int> {
    var newDirection = direction
    val cubeDimensionSize = (grid.size - 2) / 3
    var (newRow, newColumn) = move(direction, row, column)

    if (grid[newRow][newColumn] == ' ') {
        when (direction) {
            '>' -> when {
                newColumn == cubeDimensionSize * 3 + 1 && newRow <= cubeDimensionSize -> {
//                r1c13 r12c16
//                r2c13 r11c16
//                r3c13 r10c16
//                r4c13 r9c16
                    newDirection = '<'
                    newRow = cubeDimensionSize * 3 - (newRow - 1)
                    newColumn = cubeDimensionSize * 4
                }

                newColumn == cubeDimensionSize * 3 + 1 && newRow > cubeDimensionSize -> {
//                r5c13 r9c16
//                r6c13 r9c15
//                r7c13 r9c14
//                r8c13 r9c13
                    newDirection = 'v'
                    newColumn = cubeDimensionSize * 5 - (newRow - 1)
                    newRow = cubeDimensionSize * 2 + 1
                }

                newColumn == cubeDimensionSize * 4 + 1 -> {
//                r9c17  r4c12
//                r10c17 r3c12
//                r11c17 r2c12
//                r12c17 r1c12
                    newDirection = '<'
                    newRow = cubeDimensionSize * 3 - (newRow - 1)
                    newColumn = cubeDimensionSize * 3
                }
            }

            '<' -> when {
                newColumn == 0 -> {
//                r5c0 r12c16
//                r6c0 r12c15
//                r7c0 r12c14
//                r8c0 r12c13
                    newDirection = '^'
                    newColumn = cubeDimensionSize * 5 - (newRow - 1)
                    newRow = cubeDimensionSize * 3
                }

                newColumn == cubeDimensionSize * 2 && newRow <= cubeDimensionSize -> {
//                r1c8 r5c5
//                r2c8 r5c6
//                r3c8 r5c7
//                r4c8 r5c8
                    newDirection = 'v'
                    newColumn = newRow + cubeDimensionSize
                    newRow = cubeDimensionSize + 1
                }

                newColumn == cubeDimensionSize * 2 && newRow > cubeDimensionSize -> {
//                r9c8  r8c8
//                r10c8 r8c7
//                r11c8 r8c6
//                r12c8 r8c5
                    newDirection = '^'
                    newColumn = cubeDimensionSize * 4 - (newRow - 1)
                    newRow = cubeDimensionSize * 2
                }
            }

            'v' -> when {
                newRow == cubeDimensionSize * 2 + 1 && newColumn <= cubeDimensionSize -> {
//                r9c1 r12c12
//                r9c2 r12c11
//                r9c3 r12c10
//                r9c4 r12c9
                    newDirection = '^'
                    newRow = cubeDimensionSize * 3
                    newColumn = cubeDimensionSize * 3 - (newColumn - 1)
                }

                newRow == cubeDimensionSize * 2 + 1 && newColumn > cubeDimensionSize -> {
//                r9c5 r12c9
//                r9c6 r11c9
//                r9c7 r10c9
//                r9c8 r9c9
                    newDirection = '>'
                    newRow = cubeDimensionSize * 4 - (newColumn - 1)
                    newColumn = cubeDimensionSize * 2 + 1
                }

                newRow == cubeDimensionSize * 3 + 1 && newColumn <= cubeDimensionSize * 3 -> {
//                r13c9  r8c4
//                r13c10 r8c3
//                r13c11 r8c2
//                r13c12 r8c1
                    newDirection = '^'
                    newRow = cubeDimensionSize * 2
                    newColumn = cubeDimensionSize * 3 - (newColumn - 1)
                }

                newRow == cubeDimensionSize * 3 + 1 && newColumn > cubeDimensionSize * 3 -> {
//                r13c13 r8c1
//                r13c14 r7c1
//                r13c15 r6c1
//                r13c16 r5c1
                    newDirection = '>'
                    newRow = cubeDimensionSize * 5 - (newColumn - 1)
                    newColumn = 1
                }
            }

            '^' -> when {
                newRow == 0 -> {
//                r0c9  r5c4
//                r0c10 r5c3
//                r0c11 r5c2
//                r0c12 r5c1
                    newDirection = 'v'
                    newRow = cubeDimensionSize + 1
                    newColumn = cubeDimensionSize * 3 - (newColumn - 1)
                }

                newRow == cubeDimensionSize && newColumn <= cubeDimensionSize -> {
//                r4c1 r1c12
//                r4c2 r1c11
//                r4c3 r1c10
//                r4c4 r1c09
                    newDirection = 'v'
                    newRow = 1
                    newColumn = cubeDimensionSize * 3 - (newColumn - 1)
                }

                newRow == cubeDimensionSize && newColumn > cubeDimensionSize -> {
//                r4c5 r1c9
//                r4c6 r2c9
//                r4c7 r3c9
//                r4c8 r4c9
                    newDirection = '>'
                    newRow = newColumn - cubeDimensionSize
                    newColumn = cubeDimensionSize * 2 + 1
                }

                newRow == cubeDimensionSize * 2 -> {
//                r8c13 r8c12
//                r8c14 r7c12
//                r8c15 r6c12
//                r8c16 r5c12
                    newDirection = '<'
                    newRow = cubeDimensionSize * 5 - (newColumn - 1)
                    newColumn = cubeDimensionSize * 3
                }
            }
        }
    }
    return Triple(newDirection, newRow, newColumn)
}

fun part1(input: String): Int {
    val gridText = input.split("\n\n")[0].lines().toMutableList()
    gridText.add(0, " ")
    gridText.add(" ")
    val grid = gridText.map { line ->
        val chars = line.toCharArray().toMutableList()
        chars.add(0, ' ')
        chars.add(' ')
        chars
    }

    val maxX = grid.maxOf { it.size }
    grid.forEach { if (it.size < maxX) it.addAll(List(maxX - it.size) { ' ' }) }

    val directions = mutableListOf<Instruction>()
    "[0-9]+|L|R".toRegex().findAll(input.split("\n\n")[1]).map(MatchResult::value).forEach {
        val steps = it.toIntOrNull()
        if (steps != null) directions.add(Walk(steps))
        else directions.add(Turn(it[0]))
    }

    var currentRow = 1
    var currentColumn = grid[1].indexOfFirst { it == '.' }
    var currentDirection = '>'

    directions.forEach { instruction ->
        when (instruction) {
            is Turn -> {
                when (currentDirection) {
                    '>' -> currentDirection = if (instruction.direction == 'R') 'v' else '^'
                    '<' -> currentDirection = if (instruction.direction == 'R') '^' else 'v'
                    'v' -> currentDirection = if (instruction.direction == 'R') '<' else '>'
                    '^' -> currentDirection = if (instruction.direction == 'R') '>' else '<'
                }
            }

            is Walk -> {
                repeat(instruction.steps) {
                    var (newRow, newColumn) = move(currentDirection, currentRow, currentColumn)
                    if (grid[newRow][newColumn] == ' ') {
                        var p = newRow to newColumn
                        do {
                            p = when (currentDirection) {
                                '>' -> move('<', p.first, p.second)
                                '<' -> move('>', p.first, p.second)
                                'v' -> move('^', p.first, p.second)
                                '^' -> move('v', p.first, p.second)
                                else -> p.first to p.second
                            }
                        } while (grid[p.first][p.second] != ' ')
                        p = move(currentDirection, p.first, p.second)
                        newRow = p.first
                        newColumn = p.second
                    }
                    if (grid[newRow][newColumn] == '#') return@forEach
                    if (grid[newRow][newColumn] == '.') {
                        currentRow = newRow
                        currentColumn = newColumn
                    }
                }
            }
        }
    }

    val directionValue = when (currentDirection) {
        '>' -> 0
        'v' -> 1
        '<' -> 2
        '^' -> 3
        else -> -1
    }
    return 1000 * currentRow + 4 * currentColumn + directionValue
}

fun move(direction: Char, row: Int, column: Int) = when (direction) {
    '>' -> row to column + 1
    '<' -> row to column - 1
    'v' -> row + 1 to column
    '^' -> row - 1 to column
    else -> row to column
}


sealed class Instruction
data class Walk(val steps: Int) : Instruction()
data class Turn(val direction: Char) : Instruction()