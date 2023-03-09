import java.io.*

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"
private val firstName = setOf<String>("Alex", "Mordoc", "Sophie", "Tariq")
private val secondName = setOf<String>("Ironfoot", "Fernsworth", "Baggins", "Downstider")

private val menuData = File("src/main/data/tavern-menu-data.txt")
    .readText()
    .split("\n")
private val menuItems = List(menuData.size) { index ->
    val (_, name, _) = menuData[index].split(",")
    name
}

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    narrate(menuItems.joinToString())

    val patrons : MutableSet<String> = mutableSetOf()
    while(patrons.size < 10) {
        patrons += "${firstName.random()} ${secondName.random()}"
    }
    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())
    repeat(3) {
        placeOrder(patrons.random(), menuItems.random())
    }
}

fun placeOrder(patronName: String, menuItemName: String) {
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    narrate("$TAVERN_MASTER hands $patronName a $menuItemName")
}
