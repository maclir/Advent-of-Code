package y2022.d7

import java.io.File

private data class Directory(
    val parent: Directory?,
    val name: String,
    var size: Int = 0,
    val childrenDirs: MutableMap<String, Directory>,
    val childrenFiles: MutableMap<String, Int>,
)

private fun main() {
    val input = File("src/main/kotlin/y2022/d7/Input.txt").readLines(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: List<String>): Int {
    val dirs = mutableMapOf<String, Directory>()

    lateinit var root: Directory
    lateinit var current: Directory

    input.forEach {
        if (it.startsWith("$ cd /")) {
            val name = "/"
            current = Directory(
                parent = null,
                name = name,
                childrenDirs = mutableMapOf(),
                childrenFiles = mutableMapOf(),
            )
            root = current
            dirs[name] = current
        } else if (it.startsWith("$ cd ..")) {
            current = current.parent!!
        } else if (it.startsWith("$ cd ")) {
            val parent = current
            val name = "${parent.name}/${it.removePrefix("$ cd ")}"
            if (dirs.containsKey(name)) {
                current = dirs[name]!!
            } else {
                current = Directory(
                    parent = parent,
                    name = name,
                    childrenDirs = mutableMapOf(),
                    childrenFiles = mutableMapOf(),
                )

                parent.childrenDirs[name] = current
                dirs[name] = current
            }
        } else if (it.startsWith("$ ls")) {

        } else if (it.startsWith("dir ")) {

        } else {
            val (size, name) = it.split(" ")
            current.childrenFiles[name] = size.toInt()
        }
    }
    fixSizes(root)

    val total = 70000000
    val canUse = total - 30000000
    val used = root.size
    val need = root.size - canUse
    return dirs.filter { it.value.size >= need }.minOf { it.value.size }
}

private fun part1(input: List<String>): Int {
    val dirs = mutableMapOf<String, Directory>()

    lateinit var root: Directory
    lateinit var current: Directory

    input.forEach {
        if (it.startsWith("$ cd /")) {
            val name = "/"
            current = Directory(
                parent = null,
                name = name,
                childrenDirs = mutableMapOf(),
                childrenFiles = mutableMapOf(),
            )
            root = current
            dirs[name] = current
        } else if (it.startsWith("$ cd ..")) {
            current = current.parent!!
        } else if (it.startsWith("$ cd ")) {
            val parent = current
            val name = "${parent.name}/${it.removePrefix("$ cd ")}"
            if (dirs.containsKey(name)) {
                current = dirs[name]!!
            } else {
                current = Directory(
                    parent = parent,
                    name = name,
                    childrenDirs = mutableMapOf(),
                    childrenFiles = mutableMapOf(),
                )

                parent.childrenDirs[name] = current
                dirs[name] = current
            }
        } else if (it.startsWith("$ ls")) {

        } else if (it.startsWith("dir ")) {

        } else {
            val (size, name) = it.split(" ")
            current.childrenFiles[name] = size.toInt()
        }
    }
    fixSizes(root)

    return dirs.filter { it.value.size <= 100000 }.values.sumOf { it.size }
}

private fun fixSizes(root: Directory): Int {
    var size = root.childrenFiles.values.sum()
    root.childrenDirs.forEach {
        size += fixSizes(it.value)
    }
    root.size = size
    return size
}