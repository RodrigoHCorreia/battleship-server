package battleship.server.domain.game.board


import battleship.server.domain.game.ship.Ship

sealed class Cell(val position: Position) {
    override fun equals(other: Any?): Boolean {
        return (other is Cell) && (position == other.position)
    }
}

class MissCell(position: Position) : Cell(position)

open class ShipCell(val ship: Ship, position: Position) : Cell(position)
open class ShipHit(ship: Ship, position: Position) : ShipCell(ship, position)
class ShipSunk(ship: Ship, position: Position) : ShipHit(ship, position)

