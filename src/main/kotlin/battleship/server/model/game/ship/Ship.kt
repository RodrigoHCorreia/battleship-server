package battleship.server.model.ship

import Direction
import ShipType
import battleship.server.model.game.board.Coordinates
import extend

class Ship(
    val type : ShipType,
    val head : Coordinates,
    val direction : Direction,
) {
    val positions : List<Coordinates>
        get() = (0 until type.size).map {
            head.extend(it, direction)
        }
}
