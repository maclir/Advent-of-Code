package y2022.d14

import java.io.File

private fun main() {
    val input = File("src/main/kotlin/y2022/d14/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d14/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: String): Int {
    var floor = 0
    val widthAdjust = 120
    input.split("\n").forEach { line ->
        line.split(" ").filter { part -> part != "->" }.forEach { coord ->
            floor = coord.split(",")[1].toInt().coerceAtLeast(floor)
        }
    }
    floor += 2
    println(floor)
    val map = MutableList(floor + 1) { MutableList(550 + widthAdjust) { '.' } }
    input.split("\n").forEach { line ->
        var pX = 0
        var pY = 0
        line.split(" ").filter { part -> part != "->" }.map { coord ->
            coord.split(",").mapIndexed { index, s ->
                if (index == 0) s.toInt()
                else s.toInt()
            }
        }.forEachIndexed { index, (x, y) ->
            if (index > 0) {
                loopUnknownDir(y, pY) { i ->
                    loopUnknownDir(x, pX) { k ->
                        map[i][k] = '#'
                    }
                }
            }
            pX = x
            pY = y
        }
    }
    map[floor] = MutableList(550 + widthAdjust) { '#' }

    var count = 0
    val xStart = 500
    val yStart = 0

    map[yStart][xStart] = '+'
    while (true) {
        var x = xStart
        var y = yStart
        while (y < floor) {
            when {
                map[y + 1][x] == '.' -> y++
                map[y + 1][x - 1] == '.' -> {
                    y++
                    x--
                }

                map[y + 1][x + 1] == '.' -> {
                    y++
                    x++
                }

                else -> {
                    map[y][x] = 'O'
                    break
                }
            }
        }

        count++
        if (y == floor || map[yStart][xStart] == 'O') {
            break
        }
    }
    map.forEach {
        it.forEach {
            print("$it")
        }
        println()
    }
    return count
}

private fun part1(input: String): Int {
    val widthAdjust = -480
    val map = List(170) { MutableList(550 + widthAdjust) { '.' } }
    input.split("\n").forEach { line ->
        var pX = 0
        var pY = 0
        line.split(" ").filter { part -> part != "->" }.map { coord ->
            coord.split(",").mapIndexed { index, s ->
                if (index == 0) s.toInt() + widthAdjust
                else s.toInt()
            }
        }.forEachIndexed { index, (x, y) ->
            if (index > 0) {
                loopUnknownDir(y, pY) { i ->
                    loopUnknownDir(x, pX) { k ->
                        map[i][k] = '#'
                    }
                }
            }
            pX = x
            pY = y
        }
    }

    var count = 0
    val xStart = 500 + widthAdjust
    val yStart = 0
    map[yStart][xStart] = '+'
    while (true) {
        var x = xStart
        var y = yStart
        while (y < 169) {
            when {
                map[y + 1][x] == '.' -> y++
                map[y + 1][x - 1] == '.' -> {
                    y++
                    x--
                }

                map[y + 1][x + 1] == '.' -> {
                    y++
                    x++
                }

                else -> {
                    map[y][x] = 'O'
                    break
                }
            }
        }

        if (y == 169) {
            break
        }
        count++
    }
    map.forEach {
        it.forEach {
            print("$it ")
        }
        println()
    }
    return count
}

private fun loopUnknownDir(a: Int, b: Int, f: (i: Int) -> Unit) {
    if (a < b) {
        for (i in a..b) {
            f(i)
        }
    } else {
        for (i in b..a) {
            f(i)
        }
    }
}