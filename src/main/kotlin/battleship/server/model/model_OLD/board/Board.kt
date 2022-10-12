package battleship.model.board

import battleship.model.PutConsequence
import battleship.model.PutResult
import battleship.model.ShotConsequence
import battleship.model.ShotResult
import battleship.model.ship.Ship
import battleship.model.ship.ShipType
import kotlin.math.max
import kotlin.math.min

typealias Fleet = List<Ship>
typealias Grid = Map<Position, Cell>

/**
 * Board of each player
 * @property fleet [Fleet] of the player, starting as an empty list
 * @property grid [Grid] of the player, starting as an empty map
 */
data class Board(val fleet: Fleet = emptyList(), val grid: Grid = mapOf())

/**
 * Returns the number of [ShipCell] remaining (where a ship that has not been sunken is)
 * @return number of [ShipCell] cells
 */
fun Board.cellsQuantity(): Int {
    return this.fleet.fold(0) { acc, ship -> acc + ship.type.squares }
}

/**
 * [Board] Function that represents if a player has lost
 * @return Boolean
 */
fun Board.lost() = (cellsQuantity() - grid.entries.count { (_, it) -> it is ShipSunk }) <= 0

/**
 * Function that will create the bounds of a ship
 * @param pos [Position] of the head of the ship
 * @param dir [Direction] of the ship
 * @param size size of the ship
 */
fun getField(pos: Position, dir: Direction, size: Int): Set<Position> {
    val extension = Pair(if (dir === Direction.HORIZONTAL) size else 1, if (dir === Direction.VERTICAL) size else 1)
    val topLeft = Position[max(pos.column.ordinal - 1, 0), max(pos.row.ordinal - 1, 0)]
    val bottomRight = Position[min(pos.column.ordinal + extension.first, COLUMN_DIM - 1),
            min(pos.row.ordinal + extension.second, ROW_DIM - 1)]

    return makeRectangle(topLeft, bottomRight)
}


/**
 * [Board] Function that will put a ship
 * @param type [ShipType] of the ship to put
 * @param pos head [Position] of the ship
 * @param dir [Direction] of the ship
 * @return updated [Board]
 * @throws IllegalStateException
 */
fun Board.putShip(type: ShipType, pos: Position, dir: Direction): PutResult {
    if (fleet.count { type == it.type } >= type.fleetQuantity)
        return PutResult(this, PutConsequence.INVALID_SHIP)

    if (dir == Direction.HORIZONTAL) {
        if (pos.column.ordinal + type.squares > COLUMN_DIM) return PutResult(this, PutConsequence.INVALID_POSITION)
    } else {
        if (pos.row.ordinal + type.squares > ROW_DIM) return PutResult(this, PutConsequence.INVALID_POSITION)
    }

    val positions = getPositionsFromLine(pos, dir, type.squares)

    for (ship in fleet) {
        for (currPos in positions) {
            if (currPos in ship.field) {
                return PutResult(this, PutConsequence.INVALID_POSITION)
            }
        }
    }

    val field = getField(pos, dir, type.squares)
    val newShip = Ship(type, pos, dir, positions, field)
    val cells = newShip.positions.map { currPos -> ShipCell(currPos, newShip) }

    val newFleet = fleet + newShip
    val newGrid = grid + cells.associateBy { it.pos }
    val newBoard = Board(newFleet, newGrid)
    return PutResult(newBoard, PutConsequence.NONE)
}

/**
 * [Board] Function that will remove a ship
 * @param pos [Position] to remove the ship from
 * @return updated [Board]
 */
fun Board.removeShip(pos: Position): Board {
    // get cell at position
    val cell = this.grid[pos]
    return if (cell is ShipCell) {
        val ship = cell.ship
        val newFleet = fleet - ship
        val newGrid = grid - ship.positions

        Board(newFleet, newGrid)
    } else {
        this
    }
}

/**
 * [Board] Function that will make a shot.
 * @param pos [Position] from the shot.
 * @return [ShotResult] with the updated [Board] and [ShotConsequence].
 */
fun Board.makeShot(pos: Position): ShotResult {
    return when (val cell = grid[pos]) {
        is MissCell, is ShipHit -> {
            // Ship sunk is inferred as an invalid shot because of its inherited from ShipHit
            ShotResult(this, ShotConsequence.INVALID, null)
        }
        is ShipCell -> {
            val ship = cell.ship
            val gridAfterShot = grid + (pos to ShipHit(pos, ship))

            val hasSunk = ship.positions.all { gridAfterShot[it] is ShipHit }
            // check if ship is sunk
            return if (hasSunk) {
                val gridAfterSunk = grid + ship.positions.map { it to ShipSunk(it, ship) }
                ShotResult(copy(grid = gridAfterSunk), ShotConsequence.SUNK, ship.type)
            } else {
                ShotResult(copy(grid = gridAfterShot), ShotConsequence.HIT, ship.type)
            }
        }
        null -> {
            // Add cell to the grid with MissCell
            return ShotResult(copy(grid = grid + (pos to MissCell(pos))), ShotConsequence.MISS, null)
        }
    }
}

/**
 * Function that calculates all the possible positions on a [Board] that a ship can assume.
 * @receiver Board to get the possible pos.
 * @param size Size of the ship to place.
 * @param dir direction of a [Ship].
 * @return Returns a List of possible positions and directions.
 */
fun Board.getPossiblePositions(size: Int, dir: Direction): List<Pair<Direction, Position>> {
    val allPositions = Position.values

    //remove positions at sides
    val firstStep = if (dir === Direction.HORIZONTAL)
        allPositions.filter { it.column.ordinal <= COLUMN_DIM - size }
    else
        allPositions.filter { it.row.ordinal <= ROW_DIM - size }
    //remove positions occupied by ships
    val secondStep = fleet.fold(firstStep) { acc, ship -> acc - ship.field }
    //remove positions that putting a ship would collide with
    val thirdStep = secondStep.filter { head ->
        getPositionsFromLine(head, dir, size).all {
            !fleet.any { ship -> it in ship.field }
        }
    }

    return thirdStep.map { dir to it }
}

/**
 *
 */
fun Board.putRandomShip(type: ShipType): PutResult {
    val allowSpace = getPossiblePositions(type.squares, Direction.HORIZONTAL) +
            getPossiblePositions(type.squares, Direction.VERTICAL)

    // cancel out if there are no legible positions t place the ship
    if (allowSpace.isEmpty())
        return PutResult(this, PutConsequence.INVALID_RANDOM)

    val randomState = allowSpace.random()
    return putShip(type, randomState.second, randomState.first)
}

/**
 *
 */
fun Board.putAllShips(): PutResult {

    if (fleet.isComplete()) return PutResult(this, PutConsequence.NONE)

    val shipTypes = ShipType.values.filter { shipType -> fleet.count { shipType === it.type } < shipType.fleetQuantity }
    val currType = shipTypes.first()

    val newBoard = putRandomShip(currType)
    if (newBoard.second !== PutConsequence.NONE)
        return PutResult(newBoard.first, newBoard.second)

    return newBoard.first.putAllShips()
}

/**
 * Function that will check it fleet is complete.
 * @return true if the fleet is full else false
 */
fun Fleet.isComplete() = size == ShipType.values.sumOf { it.fleetQuantity }
