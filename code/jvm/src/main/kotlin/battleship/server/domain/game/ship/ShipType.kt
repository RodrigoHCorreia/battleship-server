package battleship.server.domain.game.ship

class ShipType(
    val name: String,
    val squares: Int,
    val quantity: Int
) {
    init {
        require(name.isNotBlank())
        require(squares > 0)
        require(quantity > 0)
    }
}

/**
 * Returns a [ShipType] according to the string
 *      if it is a string, return the only [battleship.server.model.game.ship.ShipType] with the same string name
 *      else return null
 */
fun String.toShipTypeOrNull(types: List<ShipType>): ShipType? {
    return types.firstOrNull { it.name.startsWith(this, true) }
}

/**
 * Returns a [ShipType] according to the string, if string is an integer, return a ship by number of squares
 *          else if it is a string, return the only [battleship.server.model.game.ship.ShipType] that starts with the string as prefix
 * @throws NoSuchElementException
 */
fun String.toShipType(types: List<ShipType>): ShipType = toShipTypeOrNull(types) ?: throw NoSuchElementException()