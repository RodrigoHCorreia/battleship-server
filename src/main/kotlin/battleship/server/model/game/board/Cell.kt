package battleship.server.model.game.board


import battleship.server.model.ship.Ship

sealed class Cell(val coords : Coordinates) {
    override fun equals(other: Any?): Boolean {
        return (other is Cell) && (coords == other.coords);
    }
}

class MissCell(coords : Coordinates) : Cell(coords);

open class ShipCell(val ship : Ship, coords : Coordinates) : Cell(coords)
open class ShipHit(ship : Ship, coords : Coordinates) : ShipCell(ship, coords);
class ShipSunk(ship : Ship, coords : Coordinates) : ShipHit(ship, coords);

