package y2022.d12

import java.io.File

fun main() {
    val input = File("src/main/kotlin/y2022/d12/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d12/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

fun part2(input: String): Int {
    var eRow = 0
    var eCol = 0
    var eFound = false
    val grid = input.split("\n").map { line ->
        val chars = line.toCharArray().toList()
        if (!eFound) {
            val index = chars.indexOf('E')
            if (index != -1) {
                eCol = index
                eFound = true
            } else {
                eRow++
            }
        }
        chars
    }


    var minDistance = Int.MAX_VALUE
    grid.onEachIndexed { row, chars ->
        chars.onEachIndexed { col, char ->
            if (char == 'a') {
                val distanceGrid = List(grid.size) { MutableList<Int?>(grid[0].size) { null } }
                distanceGrid[row][col] = 0
                populateMinSteps(grid, distanceGrid, row, col, 1)

                val distance = distanceGrid[eRow][eCol] ?: Int.MAX_VALUE
                if (distance < minDistance) {
                    minDistance = distance
                }
            }
        }
    }

    return minDistance
}

fun part1(input: String): Int {
    var sRow = 0
    var sCol = 0
    var sFound = false
    var eRow = 0
    var eCol = 0
    var eFound = false
    val grid = input.split("\n").map { line ->
        val chars = line.toCharArray().toMutableList()
        if (!sFound) {
            val index = chars.indexOf('S')
            if (index != -1) {
                sCol = index
                sFound = true
            } else {
                sRow++
            }
        }
        if (!eFound) {
            val index = chars.indexOf('E')
            if (index != -1) {
                eCol = index
                eFound = true
            } else {
                eRow++
            }
        }
        chars
    }

    val distanceGrid = List(grid.size) { MutableList<Int?>(grid[0].size) { null } }
    distanceGrid[sRow][sCol] = 0
    populateMinSteps(grid, distanceGrid, sRow, sCol, 1)

    return distanceGrid[eRow][eCol] ?: 0
}

val dirs = listOf(
    { r: Int, c: Int -> r + 1 to c },
    { r: Int, c: Int -> r - 1 to c },
    { r: Int, c: Int -> r to c + 1 },
    { r: Int, c: Int -> r to c - 1 },
)

fun populateMinSteps(
    grid: List<List<Char>>,
    distanceGrid: List<MutableList<Int?>>,
    row: Int,
    col: Int,
    distance: Int
) {
    val currentChar = grid.getChar(row, col)!!

    dirs.forEach {
        val (newRow, newCol) = it(row, col)
        val newChar = grid.getChar(newRow, newCol)
        val existingDistance = distanceGrid.getOrNull(newRow)?.getOrNull(newCol)
        if (newChar == null || newChar > currentChar + 1) return@forEach

        if (existingDistance == null || distance < existingDistance) {
            distanceGrid[newRow][newCol] = distance
            populateMinSteps(grid, distanceGrid, newRow, newCol, distance + 1)
        }
    }
}

private fun List<List<Char>>.getChar(r: Int, c: Int): Char? = when (val char = getOrNull(r)?.getOrNull(c)) {
    null -> null
    'S' -> 'a'
    'E' -> 'z'
    else -> char
}
