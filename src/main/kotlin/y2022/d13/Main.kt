package y2022.d13

import java.io.File

private fun main() {
    val input = File("src/main/kotlin/y2022/d13/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d13/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: String): Int {
    val divider1 = ListItem(listOf(ListItem(listOf(IntItem(2)))))
    val divider2 = ListItem(listOf(ListItem(listOf(IntItem(6)))))
    val all = input.replace("\n\n", "\n").split("\n").map { line ->
        line.toItems()[0] as ListItem
    }.toMutableList()
    all.addAll(listOf(divider1, divider2))

    var decoder = 1
     all.sortedWith { o1, o2 -> if (checkOrder(o2, o1) == true) 1 else -1 }
        .forEachIndexed { index, list ->
            if (list == divider1 || list == divider2) decoder *= (index + 1)
        }
    return decoder
}

private fun part1(input: String): Int {
    val all = input.split("\n\n").map { pair ->
        pair.split("\n").map { line ->
            line.toItems()[0] as ListItem
        }
    }

    return all.mapIndexed { index, (first, second) ->
        if (checkOrder(first, second) == true) index + 1
        else 0
    }.sum()
}

private fun checkOrder(leftListItem: ListItem, rightListItem: ListItem): Boolean? {
    val left = leftListItem.list
    val right = rightListItem.list
    right.forEachIndexed { index, rightItem ->
        if (index >= left.size) {
            return true
        }
        val leftItem = left[index]
        when {
            leftItem is IntItem && rightItem is IntItem -> {
                when {
                    leftItem.item < rightItem.item -> return true
                    leftItem.item > rightItem.item -> return false
                }
            }

            leftItem is ListItem && rightItem is ListItem -> {
                val res = checkOrder(leftItem, rightItem)
                if (res != null) return res
            }

            leftItem is IntItem && rightItem is ListItem -> {
                val res = checkOrder(leftItem.asList(), rightItem)
                if (res != null) return res
            }

            leftItem is ListItem && rightItem is IntItem -> {
                val res = checkOrder(leftItem, rightItem.asList())
                if (res != null) return res
            }
        }
    }

    if (right.size < left.size) {
        return false
    }

    return null
}

private fun String.toItems(): List<Item> {
    val items = mutableListOf<Item>()
    var intStr = ""
    var skipBrackets = 0

    this.forEachIndexed { index, char ->
        if (skipBrackets > 0) {
            when (char) {
                '[' -> skipBrackets++
                ']' -> skipBrackets--
            }
        } else {
            when (char) {
                '[' -> {
                    items.add(ListItem(this.drop(index + 1).toItems()))
                    skipBrackets++
                }

                ']' -> {
                    if (intStr.isNotEmpty()) items.add(IntItem(intStr.toInt()))
                    return items
                }

                ',' -> {
                    if (intStr.isNotEmpty()) items.add(IntItem(intStr.toInt()))
                    intStr = ""
                }

                else -> intStr += char
            }
        }
    }
    return items
}

sealed class Item
private data class IntItem(val item: Int) : Item() {
    override fun toString(): String {
        return item.toString()
    }

  fun asList(): ListItem {
        return ListItem(listOf(this))
    }
}

private data class ListItem(val list: List<Item>) : Item() {
    override fun toString(): String {
        return list.toString().replace(" ", "")
    }
}
