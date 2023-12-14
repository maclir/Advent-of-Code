package y2022.d8

import java.io.File

private fun main() {
    val input = File("src/main/kotlin/y2022/d8/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d8/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: String): Int {
    val lines = input.lines()

    val treesH = Array(lines.size) { IntArray(lines.size) }

    for (i in lines.indices) {
        val line = lines[i]
        for (k in line.indices) {
            treesH[i][k] = line[k].digitToInt()
        }
    }

    data class Tree(
        val top: Int,
        val bottom: Int,
        val left: Int,
        val right: Int,
    ) {
      fun score() = top * bottom * left * right
    }

    val trees = mutableListOf<Tree>()

    for (i in treesH.indices) {
        for (k in treesH[i].indices) {
            var top = 0
            for (z in k - 1 downTo 0) {
                top++
                if (treesH[i][k] <= treesH[i][z]) {
                    break
                }
            }
            var bottom = 0
            for (z in k + 1 until treesH[i].size) {
                bottom++
                if (treesH[i][k] <= treesH[i][z]) {
                    break
                }
            }
            var left = 0
            for (z in i - 1 downTo 0) {
                left++
                if (treesH[i][k] <= treesH[z][k]) {
                    break
                }
            }
            var right = 0
            for (z in i + 1 until treesH.size) {
                right++
                if (treesH[i][k] <= treesH[z][k]) {
                    break
                }
            }
            trees.add(Tree(top, bottom, left, right))
        }
    }

    return trees.maxBy { it.score() }.score()
}

private fun part1(input: String): Int {
    val lines = input.lines()

    val trees = Array(lines.size) { IntArray(lines.size) }
    val treesB = Array(lines.size) { BooleanArray(lines.size) }

    for (i in lines.indices) {
        val line = lines[i]
        for (k in line.indices) {
            trees[i][k] = line[k].digitToInt()
        }
    }

    var maxC = IntArray(trees.size) { -1 }
    for (i in trees.indices) {
        var max = -1
        for (k in trees[i].indices) {
            if (trees[i][k] > maxC[k]) {
                treesB[i][k] = true
                maxC[k] = trees[i][k]
            }
            if (trees[i][k] > max) {
                treesB[i][k] = true
                max = trees[i][k]
            }
        }
    }

    maxC = IntArray(trees.size) { -1 }
    for (i in trees.size - 1 downTo 0) {
        var max = -1
        for (k in trees[i].size - 1 downTo 0) {
            if (trees[i][k] > maxC[k]) {
                treesB[i][k] = true
                maxC[k] = trees[i][k]
            }
            if (trees[i][k] > max) {
                treesB[i][k] = true
                max = trees[i][k]
            }
        }
    }

    var total = 0
    for (i in treesB.indices) {
        for (k in treesB[i].indices) {
            if (treesB[i][k]) {
                total++
            }
        }
    }
    return total
}
