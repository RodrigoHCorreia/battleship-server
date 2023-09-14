package battleship.server.domain.game.board

import Direction
import battleship.server.domain.game.ship.Ship
import battleship.server.domain.game.ship.ShipType

data class Board(
    val grid: Map<Position, Cell> = HashMap(),
    val fleet: List<Ship> = emptyList()
)


enum class PlaceResult { INVALID, PLACED }
enum class ShotResult { INVALID, MISS, HIT, SUNK }

typealias BoardPlace = Pair<Board, PlaceResult>
typealias BoardShot = Triple<Board, ShotResult, ShipType?>

fun Board.checkIfLost()
    = grid.values.filter { it is ShipCell }.all { it is ShipSunk }

fun Board.placeShip(type: ShipType, head: Position, direction: Direction): BoardPlace {
    if (fleet.count { type == it.type } >= type.quantity)
        return BoardPlace(this, PlaceResult.INVALID)

    val newShip = Ship(type, head, direction)
    if(newShip.positions.any { grid.get(it) != null })
        return BoardPlace(this, PlaceResult.INVALID)

    val cells = newShip.positions.map { ShipCell(newShip, it) }
    val newGrid = grid + cells.associateBy { it.position }

    return BoardPlace(Board(newGrid, fleet + newShip), PlaceResult.PLACED)
}

fun Board.makeShot(position: Position): BoardShot {
    return when (val cell = grid[position]) {
        is MissCell, is ShipHit -> {
            BoardShot(this, ShotResult.INVALID, null)
        }
        is ShipCell -> {
            val ship = cell.ship
            val gridAfterShot = grid + (position to ShipHit(ship, position))

            val hasSunk = ship.positions.all { gridAfterShot[it] is ShipHit }
            return if (hasSunk) {
                val gridAfterSunk = grid + ship.positions.map { it to ShipSunk(ship, position) }
                BoardShot(copy(grid = gridAfterSunk), ShotResult.SUNK, ship.type)
            } else {
                BoardShot(copy(grid = gridAfterShot), ShotResult.HIT, null)
            }
        }
        null -> {
            BoardShot(copy(grid = grid + (position to MissCell(position))), ShotResult.MISS, null)
        }
    }
}
