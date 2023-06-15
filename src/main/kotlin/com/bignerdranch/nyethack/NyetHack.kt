package com.bignerdranch.nyethack

import kotlin.system.exitProcess

lateinit var player: Player
fun main() {
    narrate("Welcome to NyetHack")
    val playerName = promptHeroName()
    player = Player(playerName)
//    com.bignerdranch.nyethack.changeNarratorMood()
    Game.play()
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

object Game {
    private val worldMap = listOf(
        listOf(TownSquare(), Tavern(), Room("Back Room")),
        listOf(MonsterRoom("A Long Corridor"), Room("A Generic Room")),
        listOf(MonsterRoom("The Dungeon"))
    )
    private var currentRoom: Room = worldMap[0][0]
    private var currentPosition = Coordinate(0, 0)

    init {
        narrate("Welcome, adventurer")
    }

    fun play() {
        while (true) {
            narrate("${player.name} of ${(player.homeTown)}, ${player.title}, is in ${currentRoom.description()}")
            val mortality = if (player.isImmortal) "an immortal" else "a mortal"
            narrate("${player.name}, $mortality, has ${player.healthPoints} HP")
            currentRoom.enterRoom()
            print("> Enter your command:")
            GameInput(readLine()).processCommand()
        }
    }

    fun move(direction: Direction) {
        val newPosition = direction.updateCoordinate(currentPosition)
        val newRoom = worldMap.getOrNull(newPosition.y)?.getOrNull(newPosition.x)
        if (newRoom != null) {
            narrate("The hero moves ${direction.name}")
            currentPosition = newPosition
            currentRoom = newRoom
        } else {
            narrate("You can't move ${direction.name}")
        }
    }

    fun fight() {
        val monsterRoom = currentRoom as? MonsterRoom
        val currentMonster = monsterRoom?.monster
        if (currentMonster == null) {
            narrate("There's nothing to fight here")
            return
        }
        while (player.healthPoints > 0 && currentMonster.healthPoints > 0) {
            player.attack(currentMonster)
            if (currentMonster.healthPoints > 0) {
                currentMonster.attack(player)
            }
            Thread.sleep(1000)
        }
        if (player.healthPoints <=0 ) {
            narrate("You have benn defeated! Thanks for playing")
            exitProcess(0)
        } else {
            narrate("${currentMonster.name} has been defeated")
            monsterRoom.monster = null
        }
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }
        fun processCommand() = when (command.lowercase()) {
            "fight" -> fight()
            "move" -> {
                val direction = Direction.values().firstOrNull() {
                    it.name.equals(argument, ignoreCase = true)
                }
                if (direction != null) {
                    move(direction)
                } else {
                    narrate("I don't know what direction that is")
                }
            }
            else -> narrate("I'm not sure what you're trying to do")
        }
    }
}

private fun makeYellow(message: String) = "\u001b[33;1m$message\u001b[0m"
