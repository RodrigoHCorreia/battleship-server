package battleship.model.ship

import battleship.model.board.Column.Companion.values
import battleship.model.board.Row.Companion.values
import battleship.model.ship.ShipType.Companion.values

/**
 * All ship types allowed in the game.
 * @property name [ShipType] name.
 * @property squares Number of squares occupied.
 * @property fleetQuantity Number of ships of this type available.
 * @property values companion object that has all the possible values for [ShipType]
 *
 */
class ShipType private constructor(val name: String, val squares: Int, val fleetQuantity: Int) {
    companion object {
        val values = listOf(
            ShipType("Carrier", 5, 1),
            ShipType("Battleship", 4, 2),
            ShipType("Cruiser", 3, 3),
            ShipType("Submarine", 2, 4)
        )
    }
}


/**
 * Returns a [ShipType] according to the string, if string is an integer, return a ship by number of squares
 *      else if it is a string, return the only [ShipType] that starts with the string as prefix
 *      else return null
 */
fun String.toShipTypeOrNull(): ShipType? {
    val first = ShipType.values.firstOrNull { this in it.name || this.toIntOrNull() == it.squares }
    val last = ShipType.values.lastOrNull { this in it.name || this.toIntOrNull() == it.squares }
    return if (last != first) null else first
}

/**
 * Returns a [ShipType] according to the string, if string is an integer, return a ship by number of squares
 *          else if it is a string, return the only [ShipType] that starts with the string as prefix
 * @throws NoSuchElementException
 */
fun String.toShipType(): ShipType = toShipTypeOrNull() ?: throw NoSuchElementException()
