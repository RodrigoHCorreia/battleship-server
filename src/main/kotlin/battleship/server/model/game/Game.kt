package battleship.server.model.game

import ShipType
import battleship.server.model.game.board.Board

/**
 * Describes rules, ships and size of the board for a given game
 */
data class Rules(
    val size : Pair<Int, Int>,
    val types : List<ShipType>,
)

/**
 * Checks if a board abides by the specified rules
 */
fun Rules.isBoardValid(board : Board) : Boolean {
    println("lol do this TODO"); // TODO
    return true;
}

sealed class Game(
    val id : Int,
    val hostID : Int,
    val guestID : Int,
    val rules : Rules
)


