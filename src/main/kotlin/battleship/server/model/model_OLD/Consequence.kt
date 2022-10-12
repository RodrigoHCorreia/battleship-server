package battleship.model

import battleship.model.ShotConsequence.*
import battleship.model.board.Board
import battleship.model.ship.ShipType

/**
 * Keeps the Board and the correspondent consequence of a placement of a ship.
 * @property [Board] battleship game board;
 * @property [PutConsequence] after the placement of a ship.
 */
typealias PutResult = Pair<Board, PutConsequence>

/**
 * Keeps the game and the correspondent consequence of a shot.
 * @property [Game] battleship game;
 * @property [ShotConsequence] after the shot was taken.
 */
typealias GameShot = Triple<GameFight, ShotConsequence, ShipType?>

/**
 * Keeps the game and the correspondent consequence of a shot.
 * @property [Board] battleship game board;
 * @property [ShotConsequence] after the shot was taken.
 * @property [ShipType] ship that was hit or null.
 */
typealias ShotResult = Triple<Board, ShotConsequence, ShipType?>

/**
 * Class that represents the consequence of the shot taken
 * @property MISS shot was missed
 * @property HIT shot was hit
 * @property SUNK shot has sunken a ship
 * @property INVALID shot was invalid
 */
enum class ShotConsequence {
    MISS, HIT, SUNK, INVALID, NOT_YOUR_TURN
}

/**
 * Class that as all the possible outcomes of the put Command.
 */
enum class PutConsequence { NONE, INVALID_SHIP, INVALID_POSITION, INVALID_RANDOM }




