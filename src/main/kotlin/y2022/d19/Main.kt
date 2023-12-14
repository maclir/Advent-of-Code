package y2022.d19

import java.io.File

private fun main() {
    val input = File("src/main/kotlin/y2022/d19/Input.txt").readText(Charsets.UTF_8)
//    val input = File("src/main/kotlin/y2022/d19/Input-test.txt").readText(Charsets.UTF_8)
//    println(part1(input))
    println(part2(input))
}

private fun part2(input: String): Int {
    val blueprints = input.lines().map { line ->
        line.dropLast(1).split(".").mapIndexed { index, subLine ->
            val botCostInput =
                Regex("[0-9]+\\s\\w+").findAll(subLine).map(MatchResult::value).associate {
                    val parts = it.split(" ")
                    parts[1] to parts[0].toInt()
                }

            BotCost(
                name = when (index) {
                    0 -> "ore"
                    1 -> "clay"
                    2 -> "obsidian"
                    3 -> "geode"
                    else -> "INVALID"
                },
                ore = botCostInput.getOrDefault("ore", 0),
                clay = botCostInput.getOrDefault("clay", 0),
                obsidian = botCostInput.getOrDefault("obsidian", 0)
            )
        }.reversed().plus(BotCost(name = null))
    }.take(3)
    var sum = 1
    blueprints.forEach { blueprint ->
        cache.clear()

        val depot = Depot()
        val maxes = mapOf(
            "ore" to blueprint.maxOf { it.ore },
            "clay" to blueprint.maxOf { it.clay },
            "obsidian" to blueprint.maxOf { it.obsidian },
        )
        val result = findMaxGeodes(minutesLeft = 32, depot = depot, blueprint = blueprint, maxes = maxes)
        println("result: $result")
        sum *= result
    }

    return sum
}

private fun part1(input: String): Int {
    val blueprints = input.lines().map { line ->
        line.dropLast(1).split(".").mapIndexed { index, subLine ->
            val botCostInput =
                Regex("[0-9]+\\s\\w+").findAll(subLine).map(MatchResult::value).associate {
                    val parts = it.split(" ")
                    parts[1] to parts[0].toInt()
                }

            BotCost(
                name = when (index) {
                    0 -> "ore"
                    1 -> "clay"
                    2 -> "obsidian"
                    3 -> "geode"
                    else -> "INVALID"
                },
                ore = botCostInput.getOrDefault("ore", 0),
                clay = botCostInput.getOrDefault("clay", 0),
                obsidian = botCostInput.getOrDefault("obsidian", 0)
            )
        }.plus(BotCost(name = null))
    }
    var sum = 0
    blueprints.forEachIndexed { index, blueprint ->
        cache.clear()

        val depot = Depot()
        val maxes = mapOf(
            "ore" to blueprint.maxOf { it.ore },
            "clay" to blueprint.maxOf { it.clay },
            "obsidian" to blueprint.maxOf { it.obsidian },
        )
        val result = findMaxGeodes(minutesLeft = 24, depot = depot, blueprint = blueprint, maxes = maxes)
        sum += (index + 1) * result
    }

    return sum
}

val cache = mutableMapOf<String, Int>()

private fun findMaxGeodes(
    minutesLeft: Int,
    depot: Depot,
    blueprint: List<BotCost>,
    maxes: Map<String, Int>,
): Int {
    for (i in minutesLeft until 32) {
        if (cache.getOrDefault("$i|geode", 0) > depot.bots.getOrDefault("geode", 0)) return depot.geode

    }
    cache["$minutesLeft|geode"] = depot.bots.getOrDefault("geode", 0)

    val key = "$minutesLeft|$depot"
    if (cache.containsKey(key)) return cache[key]!!

    val newMinutesLeft = minutesLeft - 1
    if (newMinutesLeft < 0) return depot.geode

    var max = 0
    blueprint.forEach { botCost ->
        if ((depot.bots[botCost.name] ?: 0) >= (maxes[botCost.name] ?: Int.MAX_VALUE)) return@forEach
        val newDepot = depot.build(botCost) ?: return@forEach
        newDepot.produceAll()
        max = max.coerceAtLeast(findMaxGeodes(newMinutesLeft, newDepot, blueprint, maxes))
    }
    cache[key] = max

    return max
}

private data class Depot(
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geode: Int = 0,
    val bots: MutableMap<String, Int> = mutableMapOf("ore" to 1),
    var inProduction: String? = null,
) {
  fun build(botCost: BotCost): Depot? {
        val newOre = ore - botCost.ore
        val newClay = clay - botCost.clay
        val newObsidian = obsidian - botCost.obsidian
        if (newOre < 0 || newClay < 0 || newObsidian < 0) return null
        return Depot(
            ore = newOre,
            clay = newClay,
            obsidian = newObsidian,
            geode = geode,
            bots = bots.toMutableMap(),
            inProduction = botCost.name,
        )
    }

  fun produceAll() {
        bots.forEach { (botName, quantity) ->
            when (botName) {
                "ore" -> ore += quantity
                "clay" -> clay += quantity
                "obsidian" -> obsidian += quantity
                "geode" -> geode += quantity
            }
        }
        if (inProduction != null) {
            bots[inProduction!!] = bots.getOrDefault(inProduction, 0) + 1
            inProduction = null
        }
    }

    override fun toString(): String {
        return "$ore|$clay|$obsidian|$geode|$bots"
    }
}

private data class BotCost(
    val name: String?,
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
) {
    override fun toString(): String {
        return "$name|$ore|$clay|$obsidian"
    }
}
