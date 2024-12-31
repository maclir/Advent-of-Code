package y2015.d11

import utilities.*
import java.io.File

private fun main() {
    val input = File(
//        "src/main/kotlin/y2015/d11/Input-test.txt"
        "src/main/kotlin/y2015/d11/Input.txt"
    ).readText(Charsets.UTF_8)

    printRun(::part1, input)
    printRun(::part2, input)
}

private fun part1(input: String): String {
    var newPassword =  input
    while(!newPassword.isValidPassword()) newPassword = newPassword.increment()
    return newPassword
}

private fun part2(input: String): String {
    var newPassword = part1(input).increment()
    while(!newPassword.isValidPassword()) newPassword = newPassword.increment()
    return newPassword
}

private fun String.increment() : String {
    var add = true
    var output = ""
    for (c in this.reversed()) {
        output += if(add) {
            if (c == 'z') 'a' else {
                add = false
                c + 1
            }
        } else c
    }
    if (add) output += 'a'
    return output.reversed()
}

private fun String.isValidPassword(): Boolean {
    val password = this
    if (password.length < 8) return false
    if (!password.windowed(3).any { it[0] + 1 == it[1] && it[1] + 1 == it[2] }) return false
    if (listOf('i', 'o', 'l').any { password.contains(it) }) return false

    var pairC: Char? = null
    password.forEachIndexed { index, c ->
        if (password.getOrNull(index + 1) == c) {
            when {
                pairC == null -> pairC = c
                pairC != c -> return true
            }
        }
    }
    return false
}