package y2022.d23

import java.io.File

private fun main() {
    val input = File("src/main/kotlin/y2022/d23/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d23/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}


private fun part2(input: String): Int {
    val lineLength = input.lines()[0].length
    val padLength = 100
    val padStart = lineLength + padLength
    val padEnd = padStart + padLength

    val allElves = mutableListOf<Elf>()

    val grid = input.lines().asReversed().plus(List(padLength) { ".".repeat(lineLength) }).asReversed()
        .plus(List(padLength) { ".".repeat(lineLength) }).mapIndexed { row, line ->
            line.padStart(padStart, '.').padEnd(padEnd, '.').mapIndexed { column, char ->
                if (char == '.') null to mutableListOf()
                else {
                    val elf = Elf(row = row, column = column)
                    allElves.add(elf)
                    elf to mutableListOf<Elf>()
                }
            }.toMutableList()
        }

    val directions = mutableListOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)

    var moved = true
    var roundCounter = 0
    while (moved) {
        moved = false
        roundCounter++
        allElves.forEach elves@{ elf ->
            if (elf.adjacentPositions().find { (row, column) -> grid[row][column].first != null } == null) return@elves
            directions.forEach { direction ->
                val (newRow, newColumn) = direction.proposeMove(grid, elf.row, elf.column)
                if (newRow != null && newColumn != null) {
                    grid[newRow][newColumn].second.add(elf)
                    return@elves
                }
            }
        }

        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, (_, proposedMoves) ->
                if (proposedMoves.isEmpty()) return@forEachIndexed
                if (proposedMoves.size > 1) {
                    proposedMoves.clear()
                    return@forEachIndexed
                }
                val elf = proposedMoves.removeFirst()
                grid[elf.row][elf.column] = null to grid[elf.row][elf.column].second
                elf.row = rowIndex
                elf.column = columnIndex
                grid[rowIndex][columnIndex] = elf to proposedMoves
                moved = true
            }
        }

        val directionHead = directions.removeAt(0)
        directions.add(directionHead)
    }

    return roundCounter
}

private fun part1(input: String): Int {
    val lineLength = input.lines()[0].length
    val padLength = 10
    val padStart = lineLength + padLength
    val padEnd = padStart + padLength

    val allElves = mutableListOf<Elf>()

    val grid = input.lines().asReversed().plus(List(padLength) { ".".repeat(lineLength) }).asReversed()
        .plus(List(padLength) { ".".repeat(lineLength) }).mapIndexed { row, line ->
            line.padStart(padStart, '.').padEnd(padEnd, '.').mapIndexed { column, char ->
                if (char == '.') null to mutableListOf()
                else {
                    val elf = Elf(row = row, column = column)
                    allElves.add(elf)
                    elf to mutableListOf<Elf>()
                }
            }.toMutableList()
        }

    val directions = mutableListOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)

    repeat(10) {
        allElves.forEach elves@{ elf ->
            if (elf.adjacentPositions().find { (row, column) -> grid[row][column].first != null } == null) return@elves
            directions.forEach { direction ->
                val (newRow, newColumn) = direction.proposeMove(grid, elf.row, elf.column)
                if (newRow != null && newColumn != null) {
                    grid[newRow][newColumn].second.add(elf)
                    return@elves
                }
            }
        }

        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, (_, proposedMoves) ->
                if (proposedMoves.isEmpty()) return@forEachIndexed
                if (proposedMoves.size > 1) {
                    proposedMoves.clear()
                    return@forEachIndexed
                }
                val elf = proposedMoves.removeFirst()
                grid[elf.row][elf.column] = null to grid[elf.row][elf.column].second
                elf.row = rowIndex
                elf.column = columnIndex
                grid[rowIndex][columnIndex] = elf to proposedMoves
            }
        }

        val directionHead = directions.removeAt(0)
        directions.add(directionHead)
    }

    val minRow = allElves.minOf { it.row }
    val maxRow = allElves.maxOf { it.row }
    val minColumn = allElves.minOf { it.column }
    val maxColumn = allElves.maxOf { it.column }

    return grid.subList(minRow, maxRow + 1).sumOf { row ->
        row.subList(minColumn, maxColumn + 1).count { it.first == null }
    }
}

private data class Elf(
    var row: Int,
    var column: Int,
) {
  fun adjacentPositions() = listOf(
        row to column + 1,
        row to column - 1,
        row + 1 to column,
        row + 1 to column + 1,
        row + 1 to column - 1,
        row - 1 to column,
        row - 1 to column + 1,
        row - 1 to column - 1,
    )
}

private enum class Direction {
    NORTH {
        override fun proposeMove(
            grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int
        ): Pair<Int?, Int?> {
            if (!adjacentEmpty(grid, elfRow, elfColumn)) return null to null
            return elfRow - 1 to elfColumn
        }

        override fun adjacentEmpty(grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int): Boolean {
            return grid[elfRow - 1][elfColumn - 1].first == null && grid[elfRow - 1][elfColumn].first == null && grid[elfRow - 1][elfColumn + 1].first == null
        }
    },
    SOUTH {
        override fun proposeMove(
            grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int
        ): Pair<Int?, Int?> {
            if (!adjacentEmpty(grid, elfRow, elfColumn)) return null to null
            return elfRow + 1 to elfColumn
        }

        override fun adjacentEmpty(grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int): Boolean {
            return grid[elfRow + 1][elfColumn - 1].first == null && grid[elfRow + 1][elfColumn].first == null && grid[elfRow + 1][elfColumn + 1].first == null
        }
    },
    WEST {
        override fun proposeMove(
            grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int
        ): Pair<Int?, Int?> {
            if (!adjacentEmpty(grid, elfRow, elfColumn)) return null to null
            return elfRow to elfColumn - 1
        }

        override fun adjacentEmpty(grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int): Boolean {
            return grid[elfRow - 1][elfColumn - 1].first == null && grid[elfRow][elfColumn - 1].first == null && grid[elfRow + 1][elfColumn - 1].first == null
        }
    },
    EAST {
        override fun proposeMove(
            grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int
        ): Pair<Int?, Int?> {
            if (!adjacentEmpty(grid, elfRow, elfColumn)) return null to null
            return elfRow to elfColumn + 1
        }

        override fun adjacentEmpty(grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int): Boolean {
            return grid[elfRow - 1][elfColumn + 1].first == null && grid[elfRow][elfColumn + 1].first == null && grid[elfRow + 1][elfColumn + 1].first == null
        }
    };

    abstract fun proposeMove(grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int): Pair<Int?, Int?>
    abstract fun adjacentEmpty(grid: List<List<Pair<Elf?, List<Elf>>>>, elfRow: Int, elfColumn: Int): Boolean
}