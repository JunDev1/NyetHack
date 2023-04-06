package com.bignerdranch.nyethack

lateinit var player : Player
fun main() {
    narrate("Welcome to NyetHack")
    val playerName = promptHeroName()
    player = Player(playerName)
//    com.bignerdranch.nyethack.changeNarratorMood()
    player.prophesize()
    val mortality = if (player.isImmortal) "an immortal" else "a mortal"
    narrate("${player.name}, ${(player.title)}, heads to the town square")
    narrate("${player.name}, $mortality, has ${player.healthPoints} HP")
    visitTavern()
    player.castFireball()
    player.prophesize()
}

fun promptHeroName(): String {
    narrate("A hero enters the town of Kronstadt. What is their name?") {
        makeYellow(it)
    }
    /* val input = readLine()
    require(input != null && input.isNotEmpty()) {
        "The hero must have a name."
    }
    return input */
    println("Madrigal")
    return "Madrigal"
}

private fun makeYellow(message: String) = "\u001b[33;1m$message\u001b[0m"
