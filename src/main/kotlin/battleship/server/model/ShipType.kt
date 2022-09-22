package battleship.server.model

/**
 * All ship types allowed in the game.
 * @property name [ShipType] name.
 * @property size Number of squares occupied.
 * @property quantity Number of ships of this type available.
 *
 */
class ShipType(
    val name: String,
    val size : Int,
    val quantity: Int
    )


