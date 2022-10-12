package battleship.model.ship

import battleship.model.board.Direction
import battleship.model.board.Position


/**
 * Ship class in the game.
 * @property type type of the [Ship]
 * @property head head position of the [Ship]
 * @property dir direction of the [Ship]
 * @property positions list of positions of the [Ship].
 *
 */
data class Ship(
    val type: ShipType,
    val head: Position,
    val dir: Direction,
    val positions: List<Position>,
    val field: Set<Position>
)
