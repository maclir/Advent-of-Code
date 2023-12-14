package y2023.d03

import java.io.File
import kotlin.system.measureTimeMillis

private fun main() {
    val input = File(
//        "src/main/kotlin/y2023/d03/Input-test.txt"
        "src/main/kotlin/y2023/d03/Input.txt"
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

private fun part1(input: String): Int {
    val grid = input.split("\n").map { line ->
        line.toCharArray().toList()
    }

    var sum = 0
    for (row in grid.indices) {
        var currentNumber = 0
        var currentNumberStartIndex = -1
        var currentNumberEndIndex = -1
        for (col in grid[0].indices) {
            if (grid[row][col].isDigit()) {
                if (currentNumberStartIndex == -1) currentNumberStartIndex  = col
                currentNumber = currentNumber * 10 + grid[row][col].digitToInt()
            } else if (currentNumberStartIndex != -1) {
                currentNumberEndIndex = col - 1
            }
            if (col == grid[0].size - 1 && currentNumberStartIndex != -1) {
                currentNumberEndIndex = col
            }
            if (currentNumberEndIndex != -1) {
                checkLoop@ for (i in (row - 1).coerceAtLeast(0)..(row + 1).coerceAtMost(grid.size - 1)) {
                    for (j in (currentNumberStartIndex - 1).coerceAtLeast(0) .. (currentNumberEndIndex + 1).coerceAtMost(grid[0].size - 1)) {
                        if (!grid[i][j].isDigit() && grid[i][j] != '.') {
                            sum += currentNumber
                            break@checkLoop
                        }
                    }
                }
                currentNumberStartIndex = -1
                currentNumberEndIndex = -1
                currentNumber = 0
            }
        }
    }
    return sum
}

private fun part2(input: String): Int {
    val grid = input.split("\n").map { line ->
        line.toCharArray().toList()
    }

    var sum = 0
    val newGrid = Array(grid.size){arrayOfNulls<Int>(grid[0].size)}
    for (row in grid.indices) {
        var currentNumber = 0
        var currentNumberStartIndex = -1
        var currentNumberEndIndex = -1
        for (col in grid[0].indices) {
            if (grid[row][col].isDigit()) {
                if (currentNumberStartIndex == -1) currentNumberStartIndex  = col
                currentNumber = currentNumber * 10 + grid[row][col].digitToInt()
            } else if (currentNumberStartIndex != -1) {
                currentNumberEndIndex = col - 1
            }
            if (col == grid[0].size - 1 && currentNumberStartIndex != -1) {
                currentNumberEndIndex = col
            }
            if (currentNumberEndIndex != -1) {
                for (j in (currentNumberStartIndex).coerceAtLeast(0) .. (currentNumberEndIndex).coerceAtMost(grid[0].size - 1)) {
                    newGrid[row][j] = currentNumber
                }

                currentNumberStartIndex = -1
                currentNumberEndIndex = -1
                currentNumber = 0
            }
        }
    }
    for (row in newGrid.indices) {
        for (col in newGrid[0].indices) {
            if (grid[row][col] == '*') {
                val numbers = arrayListOf<Int>()
              fun addNumbers(number: Int?): Boolean {
                    if (number != null) numbers.add(number)
                    return number != null
                }
                addNumbers(newGrid[row][col - 1])
                addNumbers(newGrid[row][col + 1])

                if (!addNumbers(newGrid[row - 1][col])) {
                    addNumbers(newGrid[row - 1][col - 1])
                    addNumbers(newGrid[row - 1][col + 1])
                }

                if (!addNumbers(newGrid[row + 1][col])) {
                    addNumbers(newGrid[row + 1][col - 1])
                    addNumbers(newGrid[row + 1][col + 1])
                }
                if (numbers.size == 2) {
                    sum += numbers[0] * numbers[1]
                }
            }
        }
    }
    return sum
}