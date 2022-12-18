package y2022.d18

import java.io.File

fun main() {
//    val input = File("src/main/kotlin/y2022/d18/Input.txt").readText(Charsets.UTF_8)
    val input = File("src/main/kotlin/y2022/d18/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

fun part2(input: String): Int {
    val cubes = input.lines().map { line ->
        val (x, y, z) = Regex("[0-9]+").findAll(line).map(MatchResult::value).map { it.toInt() }.toList()
        Cube(x, y, z)
    }.toMutableSet()

    val airCubes = mutableSetOf<Cube>()

    for (x in cubes.minOf { it.x } - 1..cubes.maxOf { it.x } + 1) {
        for (y in cubes.minOf { it.y } - 1..cubes.maxOf { it.y } + 1) {
            for (z in cubes.minOf { it.z } - 1..cubes.maxOf { it.z } + 1) {
                airCubes.add(Cube(x, y, z))
            }
        }
    }

    airCubes.removeAll(cubes)

    val outerAirCubes = mutableSetOf(
        airCubes.minBy { it.x },
    )

    var previousSize: Int
    do {
        previousSize = airCubes.size
        airCubes.forEach outer@{ airCube ->
            if (outerAirCubes.contains(airCube)) return@outer
            airCube.adjacentCubes().forEach { adjacent ->
                if (outerAirCubes.contains(adjacent)) {
                    outerAirCubes.add(airCube)
                    return@outer
                }
            }
        }
        airCubes.removeAll(outerAirCubes)
    } while (previousSize != airCubes.size)

    var surface = 0
    cubes.forEach { cube ->
        cube.adjacentCubes().forEach { adjacentCube ->
            if (outerAirCubes.contains(adjacentCube)) surface++
        }
    }

    return surface
}

fun part1(input: String): Int {
    val cubes = input.lines().map { line ->
        val (x, y, z) = Regex("[0-9]+").findAll(line).map(MatchResult::value).map { it.toInt() }.toList()
        Cube(x, y, z)
    }.toSet()

    var surface = 0
    cubes.forEach { cube ->
        cube.adjacentCubes().forEach { adjacentCube ->
            if (!cubes.contains(adjacentCube)) surface++
        }
    }
    return surface
}

data class Cube(val x: Int, val y: Int, val z: Int) {
    fun adjacentCubes(): List<Cube> {
        return listOf(
            Cube(x + 1, y, z),
            Cube(x - 1, y, z),
            Cube(x, y + 1, z),
            Cube(x, y - 1, z),
            Cube(x, y, z + 1),
            Cube(x, y, z - 1),
        )
    }
}