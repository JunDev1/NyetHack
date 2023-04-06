package com.bignerdranch.nyethack

import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"
private val firstNames = setOf<String>("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf<String>("Ironfoot", "Fernsworth", "Baggins", "Downstider")

private val menuData = File("src/main/data/tavern-menu-data.txt")
    .readText()
    .split("\n")
    .map { it.split(",") }

private val menuItems = menuData.map { (_, name, _) -> name }

private val menuItemPrices = menuData.associate { (_, name, price) ->
    name to price.toDouble()
}
private val menuItemTypes = menuData.associate { (type, name, _) ->
    name to type
}

fun visitTavern() {
    narrate("${player.name} enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    narrate(menuItems.joinToString())

    val patrons: MutableSet<String> = firstNames.shuffled().zip(lastNames.shuffled()) { firstName, lastname ->
        "$firstName $lastname"
    }.toMutableSet()
    val patronGold = mutableMapOf(TAVERN_MASTER to 86.99, player.name to 4.50, *patrons.map {
        it to 6.00
    }.toTypedArray())

    narrate("${player.name} sees several patrons in the tavern:")
    narrate(patrons.joinToString())

    val itemDay = patrons.flatMap { getFavoriteMenuItem(it) }.random()
    narrate("The item of the day is the $itemDay")
    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }
    displayPatronBalances(patronGold)

    patrons.filter { patron -> patronGold.getOrDefault(patron, 0.0) < 4.0 }.also { departingPatrons ->
        patrons -= departingPatrons
        patronGold -= departingPatrons
    }.forEach { patron ->
        narrate("${player.name} sees $patron departing the tavern")
    }
    narrate("There are still some patrons in the Tavern")
    narrate(patrons.joinToString())
}

private fun getFavoriteMenuItem(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemTypes[menuItem]?.contains("dessert") == true
        }

        else -> {
            menuItems.shuffled().take(Random.nextInt(1..2))
        }
    }
}

private fun placeOrder(patronName: String, menuItemName: String, patronGold: MutableMap<String, Double>) {
    val itemPrice = menuItemPrices.getValue(menuItemName)
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        val action = when (menuItemTypes[menuItemName]) {
            "shandy", "elixir" -> "pours"
            "meal" -> "serves"
            else -> "hands"
        }
        narrate("$TAVERN_NAME $action $patronName a $menuItemName")
        narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
        patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
    } else {
        narrate("$TAVERN_MASTER says, \"You need more caoin for $itemPrice\"")
    }
}

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("${player.name} intuitively knows how much money each patron has")
    patronGold.forEach { patron, balance ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}
