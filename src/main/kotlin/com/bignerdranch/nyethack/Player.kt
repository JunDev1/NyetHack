package com.bignerdranch.nyethack

import java.io.File

class Player(
    initialName: String,
    val homeTown : String = "Neversummer",
    var healthPoints : Int,
    val isImmortal : Boolean
) {
    var name = initialName
        get() = field.replaceFirstChar { it.uppercase() }
        private set
    val title : String
        get() = when {
            name.all { it.isDigit() } -> "The Identifiable"
            name.none {it.isLetter()} -> "The Witness Protection Member"
            name.count {it.lowercase() in "aeioe"} > 4 -> "The Master of Vowel"
            else -> "The Renowned Hero"
        }

    val prophecy by lazy {
        narrate("$name embarks on an arduous quest to locate a fortune teller")
        Thread.sleep(3000)
        narrate("The fortune teller bestows a prophecy upon $name")
        "An intrepid hero from $homeTown shall some day " + listOf(
            "form an unlikely bond between two warring factions",
            "take possession of an otherworldly blade",
            "bring the gift of creation back to the world",
            "best rhw world-eater"
        ).random()
    }

    init {
        require(healthPoints > 0) {"HP must be greater then zero"}
        require(name.isNotBlank()) {"Player must be have a name"}
    }
    constructor(name : String) : this (
        initialName = name,
        healthPoints = 100,
        isImmortal = false
    ) {
        if (name.equals("Jason", ignoreCase = true)) {
            healthPoints = 500
        }
    }
    fun castFireball(numFireballs: Int = 2) {
        narrate("A glass of Fireball springs into existence (x${numFireballs})")
    }

    fun changedName(newName : String) {
        narrate("$name legally changes their name to $newName")
        name = newName
    }
    fun prophesize() {
        narrate("$name thinks about their future")
        narrate("A fortune teller told Madrigal, \"$prophecy\"")
    }
    companion object {
        private const val SAVE_FILE_NAME = "player.dat"
        fun fromSaveFile() = Player(File(SAVE_FILE_NAME).readBytes().toString())
    }
}