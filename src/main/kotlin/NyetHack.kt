var heroName: String = ""
fun main() {
    heroName = promptHeroName()
//    changeNarratorMood()
    narrate("$heroName, ${createTitle(heroName)}, heads to the town square")
    visitTavern()
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

private fun makeYellow(message : String) = "\u001b[33;1m$message\u001b[0m"

private fun createTitle(name : String) : String {
    return when {
        name.all { it.isDigit() } -> "The Identifiable"
        name.none {it.isLetter()} -> "The Witness Protection Member"
        name.count {it.lowercase() in "aeioe"} > 4 -> "The Master of Vowel"
        else -> "The Renowned Hero"
    }
}