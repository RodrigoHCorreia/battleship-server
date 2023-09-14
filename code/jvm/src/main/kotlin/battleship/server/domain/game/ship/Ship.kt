package battleship.server.domain.game.ship

import Direction
import battleship.server.domain.game.board.Position
import extend

class Ship(
    val type: ShipType,
    val head: Position,
    val direction: Direction,
) {
    val positions: List<Position>
        get() = (0 until type.squares).map {
            head.extend(it, direction)
        }
}
