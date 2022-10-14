package battleship.server.model.game.board

import ShipType

data class Board(
    val grid : Map<Coordinates, Cell>
);


enum class ShotResult {
    INVALID, MISS, HIT, SUNK
}


typealias BoardShot = Triple<Board, ShotResult, ShipType?>

fun Board.makeShot(coords : Coordinates) : BoardShot
{
    return when(val cell = grid[coords]) {
        is MissCell, is ShipHit -> {
            BoardShot(this, ShotResult.INVALID, null)
        }
        is ShipCell -> {
            val ship = cell.ship
            val gridAfterShot = grid + (coords to ShipHit(ship))

            //TODO falta aqui implementar mudar para sunk, deve ser facil tho
            val hasSunk = ship.positions.all { gridAfterShot[it] is ShipHit }
            return if (hasSunk) {
                val gridAfterSunk = grid + ship.positions.map { it to ShipSunk(ship) }
                BoardShot(copy(grid = gridAfterSunk), ShotResult.SUNK, ship.type)
            } else {
                BoardShot(copy(grid = gridAfterShot), ShotResult.HIT, ship.type)
            }
        }
        null -> {
            BoardShot(copy(grid = grid + (coords to MissCell())), ShotResult.MISS, null)
        }
    }
}
