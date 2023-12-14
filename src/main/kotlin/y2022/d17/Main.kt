package y2022.d17

import java.io.File

private fun main() {
    val shapes = File("src/main/kotlin/y2022/d17/Shapes.txt").readText(Charsets.UTF_8).split("\n\n").map { block ->
        List(4) { y ->
            List(4) { x ->
                return@List when (val lines = block.lines().size) {
                    1 -> y == 0
                    4 -> x == 0
                    2 -> x < 2 && y < 2
                    3 -> {
                        if (block.lines()[2][0] == '#') {
                            (x == 2 && y < 3) || (y == 0 && x < 3)
                        } else {
                            (y == 0 && x == 1) || (y == 1 && x < 3) || (y == 2 && x == 1)
                        }
                    }

                    else -> false
                }
            }
        }
    }

    val input = File("src/main/kotlin/y2022/d17/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d17/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input, shapes))

    println(part2(input, shapes))
}

private fun part2(input: String, shapes: List<List<List<Boolean>>>): Long {
    val rockLimit = 1000000000000L
    val grid = MutableList(5) { MutableList(7) { '.' } }

    val patterns = mutableMapOf<String, Pair<Int, Long>>()

    var highestGround = 0
    var shapeIndex = 0L
    var shapeX = 2
    var shapeY = 3
    var offsetY = 0L
    var windIndex = 0
    val tops = MutableList(7) { -1 }
    while (true) {
        val currentShape = shapes[shapeIndex.mod(5)]
        when (input[windIndex % input.length]) {
            '>' -> if (isPossible(grid, currentShape, shapeX + 1, shapeY)) shapeX++
            '<' -> if (isPossible(grid, currentShape, shapeX - 1, shapeY)) shapeX--
        }
        windIndex++
        if (isPossible(grid, currentShape, shapeX, shapeY - 1)) shapeY--
        else {
            currentShape.forEachIndexed { y, row ->
                row.forEachIndexed { x, b ->
                    if (b) {
                        grid[y + shapeY][x + shapeX] = '#'
                        highestGround = highestGround.coerceAtLeast(y + shapeY + 1)
                        tops[x + shapeX] = tops[x + shapeX].coerceAtLeast(y + shapeY)
                    }
                }
            }
            shapeIndex++

            val minTop = tops.min()
            tops.forEachIndexed { index, i -> tops[index] = i - minTop }

            shapeX = 2
            shapeY = 3 + highestGround
            if (shapeIndex == rockLimit) break

            val cacheKey = "${windIndex % input.length}|${shapeIndex % 5}|$tops"
            if (patterns.containsKey(cacheKey)) {
                val (patternHighestGround, patternShapes) = patterns[cacheKey]!!
                val patternHeight = highestGround - patternHighestGround
                val patternSize = shapeIndex - patternShapes
                val remaining = rockLimit - shapeIndex
                val repeats = remaining / patternSize
                if (repeats > 0) {
                    offsetY = repeats * patternHeight
                    shapeIndex += repeats * patternSize
                }
            } else {
                patterns[cacheKey] = highestGround to shapeIndex
            }
            if (grid.size < shapeY + 5) {
                grid.addAll(List(10) { MutableList(7) { '.' } })
            }
        }
    }
    return offsetY.plus(highestGround)
}

private fun part1(input: String, shapes: List<List<List<Boolean>>>): Int {
    val rockLimit = 2022
    val grid = MutableList(5) { MutableList(7) { '.' } }

    var highestGround = 0
    var shapeIndex = 0
    var shapeX = 2
    var shapeY = 3

    var windIndex = 0
    while (true) {
        val currentShape = shapes[shapeIndex % 5]
        when (input[windIndex % input.length]) {
            '>' -> if (isPossible(grid, currentShape, shapeX + 1, shapeY)) shapeX++
            '<' -> if (isPossible(grid, currentShape, shapeX - 1, shapeY)) shapeX--
        }
        windIndex++
        if (isPossible(grid, currentShape, shapeX, shapeY - 1)) shapeY--
        else {
            currentShape.forEachIndexed { y, row ->
                row.forEachIndexed { x, b ->
                    if (b) {
                        grid[y + shapeY][x + shapeX] = '#'
                        highestGround = highestGround.coerceAtLeast(y + shapeY + 1)
                    }
                }
            }
            shapeIndex++
            shapeX = 2
            shapeY = 3 + highestGround
            if (shapeIndex == rockLimit) break
            if (grid.size < shapeY + 5) {
                grid.addAll(List(10) { MutableList(7) { '.' } })
            }
        }
    }

    return highestGround
}

private fun isPossible(grid: List<List<Char>>, shape: List<List<Boolean>>, shapeX: Int, shapeY: Int): Boolean {
    shape.forEachIndexed { y, row ->
        row.forEachIndexed { x, b ->
            if (b) {
                if (x + shapeX < 0 || x + shapeX >= 7) return false
                if (y + shapeY < 0) return false
                if (grid[y + shapeY][x + shapeX] != '.') return false
            }
        }
    }
    return true
}
