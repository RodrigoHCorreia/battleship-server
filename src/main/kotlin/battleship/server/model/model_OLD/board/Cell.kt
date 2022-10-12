package battleship.model.board

import battleship.model.ship.Ship

/**
 * Represents an empty cell
 * @property pos cell's position in the board
 */
sealed class Cell(val pos: Position)

/**
 * Represents a failed shot
 * @property pos cell's position in the board
 */
class MissCell(pos: Position) : Cell(pos)

/**
 * Represents a ship cell
 * @property pos cell's position in the board
 * @property ship associated ship to the [ShipCell]
 */
open class ShipCell(pos: Position, val ship: Ship) : Cell(pos)

/**
 * Represents a ship hit cell
 * @property pos cell's position in the board
 * @property ship associated ship to the [ShipHit]
 */
open class ShipHit(pos: Position, ship: Ship) : ShipCell(pos, ship)

/**
 * Represents a ship sunk cell
 * @property pos cell's position in the board
 * @property ship associated ship to the [ShipSunk]
 */
class ShipSunk(pos: Position, ship: Ship) : ShipHit(pos, ship)
