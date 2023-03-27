import java.io.File

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"
private val firstNames = setOf<String>("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf<String>("Ironfoot", "Fernsworth", "Baggins", "Downstider")

private val menuData = File("src/main/data/tavern-menu-data.txt")
    .readText()
    .split("\n")
private val menuItems = List(menuData.size) { index ->
    val (_, name, _) = menuData[index].split(",")
    name
}
private val menuItemPrices: Map<String, Double> = List(menuData.size) {index ->
    val (_, name, price) = menuData[index].split(",")
    name to price.toDouble()
}.toMap()
private val menuItemTypes: Map<String, String> = List(menuData.size) {index ->
    val (type, name, _) = menuData[index].split(",")
    name to type
}.toMap()

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    narrate(menuItems.joinToString())

    val patrons: MutableSet<String> = mutableSetOf()
    val patronGold = mutableMapOf(TAVERN_MASTER to 86.99, heroName to 4.50)
    while (patrons.size < 5) {
        val patronName = "${firstNames.random()} ${lastNames.random()}"
        patrons += patronName
        patronGold += patronName to 6.00
    }
    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())
    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }
    displayPatronBalances(patronGold)
}

fun placeOrder(patronName: String, menuItemName: String, patronGold: MutableMap<String, Double>) {
    val itemPrice = menuItemPrices.getValue(menuItemName)
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        val action = when(menuItemTypes[menuItemName]) {
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
    narrate("$heroName intuitively knows how much money each patron has")
    patronGold.forEach {patron, balance ->
    narrate("$patron has ${"%.2f".format(balance)} gold")}
}
