package y2022.d24

import leastCommonMultiple
import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2022/d24/Input-test.txt"
        "src/main/kotlin/y2022/d24/Input.txt"
    ).readText(Charsets.UTF_8)

    println("${
        measureTimeMillis {
            println(
//                part1(input)
                part2(input)
            )
        }
    } ms")
}


private fun part2(input: String): Int {
    val allWinds = mutableListOf<Wind>()
    val grid = input.lines().map { line ->
        line.toCharArray().map<MutableList<Wind>?> {
            when (it) {
                '.' -> mutableListOf()
                '#' -> null
                '<' -> {
                    val wind = Wind(Direction.LEFT)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                '>' -> {
                    val wind = Wind(Direction.RIGHT)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                '^' -> {
                    val wind = Wind(Direction.UP)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                'v' -> {
                    val wind = Wind(Direction.DOWN)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                else -> null
            }
        }
    }

    val windCache = mutableMapOf<Int, List<List<List<Wind>?>>>()
    val windRepeatCycle = (grid.size - 2).leastCommonMultiple(grid.first().size - 2)
    for (key in 0 until windRepeatCycle) {
        grid.forEachIndexed { rowIndex, gridRow ->
            gridRow.forEachIndexed { columnIndex, _ ->
                moveWinds(grid, rowIndex, columnIndex)
            }
        }
        allWinds.forEach { wind -> wind.moved = false }
        windCache[key] = grid.map { it.map { it?.toList() } }
    }

    val startRow = 0
    val startColumn = 1
    val endRow = grid.size - 1
    val endColumn = grid.first().size - 2
    var step = getMinimumSteps(windCache, windRepeatCycle, startRow, startColumn, endRow, endColumn)
    step = getMinimumSteps(windCache, windRepeatCycle, endRow, endColumn, startRow, startColumn, step)
    return getMinimumSteps(windCache, windRepeatCycle, startRow, startColumn, endRow, endColumn, step)
}

private fun part1(input: String): Int {
    val allWinds = mutableListOf<Wind>()
    val grid = input.lines().map { line ->
        line.toCharArray().map<MutableList<Wind>?> {
            when (it) {
                '.' -> mutableListOf()
                '#' -> null
                '<' -> {
                    val wind = Wind(Direction.LEFT)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                '>' -> {
                    val wind = Wind(Direction.RIGHT)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                '^' -> {
                    val wind = Wind(Direction.UP)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                'v' -> {
                    val wind = Wind(Direction.DOWN)
                    allWinds.add(wind)
                    mutableListOf(wind)
                }

                else -> null
            }
        }
    }

    val windCache = mutableMapOf<Int, List<List<List<Wind>?>>>()
    val windRepeatCycle = (grid.size - 2).leastCommonMultiple(grid.first().size - 2)
    for (key in 0 until windRepeatCycle) {
        grid.forEachIndexed { rowIndex, gridRow ->
            gridRow.forEachIndexed { columnIndex, _ ->
                moveWinds(grid, rowIndex, columnIndex)
            }
        }
        allWinds.forEach { wind -> wind.moved = false }
        windCache[key] = grid.map { it.map { it?.toList() } }
    }

    val startRow = 0
    val startColumn = 1
    val endRow = grid.size - 1
    val endColumn = grid.first().size - 2
    return getMinimumSteps(windCache, windRepeatCycle, startRow, startColumn, endRow, endColumn)
}

private fun getMinimumSteps(
    windCache: Map<Int, List<List<List<Wind>?>>>,
    windRepeatCycle: Int,
    startRow: Int,
    startColumn: Int,
    endRow: Int,
    endColumn: Int,
    statStep: Int = 0
): Int {
    val queue = mutableListOf(startRow to startColumn)
    val cache = mutableSetOf<String>()
    var step = statStep
    while (true) {
        val windCacheKey = step % windRepeatCycle
        val grid = windCache[step % windRepeatCycle]!!

        queue.toList().forEach { (row, column) ->
            queue.removeAt(0)
            Direction.values().forEach { direction ->
                val (newRow, newColumn) = direction.getNewPosition(row, column)
                if (newRow < 0 || newRow >= grid.size) return@forEach
                val cell = grid[newRow][newColumn] ?: return@forEach
                if (cell.isNotEmpty()) return@forEach

                if (newRow == endRow && newColumn == endColumn) {
                    return step + 1
                }
                val cacheKey = "$newRow|$newColumn|$windCacheKey"
                if (!cache.contains(cacheKey)) {
                    cache.add(cacheKey)
                    queue.add(newRow to newColumn)
                }
            }
        }
        step++
    }
}


private fun moveWinds(grid: List<List<MutableList<Wind>?>>, row: Int, column: Int) {
    val winds = grid[row][column] ?: return
    winds.removeIf { wind ->
        if (wind.moved) return@removeIf false
        wind.moved = true

        val (newRow, newColumn) = wind.direction.getNewPosition(row, column)
        if (newRow < 0 || newRow >= grid.size) {
            throw Exception("Wind not allowed here")
        }

        var cell = grid[newRow][newColumn]
        if (cell == null) {
            val (warpedRow, warpedColumn) = wind.direction.opposite()
                .getNewPosition(row, column, rowStep = grid.size - 3, columnStep = grid.first().size - 3)
            cell = grid[warpedRow][warpedColumn]
        }
        cell!!.add(wind)
        true
    }
}

private data class Wind(
    val direction: Direction,
    var moved: Boolean = false,
)

private enum class Direction {
    UP {
        override fun getNewPosition(row: Int, column: Int, rowStep: Int, columnStep: Int) = row - rowStep to column
        override fun opposite() = DOWN
    },
    DOWN {
        override fun getNewPosition(row: Int, column: Int, rowStep: Int, columnStep: Int) = row + rowStep to column
        override fun opposite() = UP
    },
    RIGHT {
        override fun getNewPosition(row: Int, column: Int, rowStep: Int, columnStep: Int) = row to column + columnStep
        override fun opposite() = LEFT
    },
    LEFT {
        override fun getNewPosition(row: Int, column: Int, rowStep: Int, columnStep: Int) = row to column - columnStep
        override fun opposite() = RIGHT
    },
    STILL {
        override fun getNewPosition(row: Int, column: Int, rowStep: Int, columnStep: Int) = row to column
        override fun opposite() = STILL
    };

    abstract fun getNewPosition(row: Int, column: Int, rowStep: Int = 1, columnStep: Int = 1): Pair<Int, Int>
    abstract fun opposite(): Direction
}