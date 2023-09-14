package battleship.server.domain.game

import battleship.server.domain.game.ship.ShipType
import battleship.server.domain.UserID
import battleship.server.domain.game.board.*

sealed class Game(
    val id: Int,
    val ranked : Boolean,
    val player1ID: UserID,
    val rules: Rules
) {
    open fun getPlayerOrNull(userID : UserID) =
        EPlayer.PLAYER1.takeIf { player1ID == userID }
}

open class GameWaiting(
    id: Int,
    ranked : Boolean,
    player1ID: UserID,
    rules: Rules = Rules.defaultRules,
) : Game(id, ranked, player1ID, rules)

open class GameSetup (
    id: Int,
    ranked : Boolean,
    player1ID: UserID,
    rules: Rules = Rules.defaultRules,
    val player2ID: UserID,
    open val board1: Board? = null,
    open val board2: Board? = null
) : GameWaiting(id, ranked, player1ID, rules)
{
    open fun getBoard(player : EPlayer) = player.choose(board1, board2)
    override fun getPlayerOrNull(userID : UserID) =
        when(userID) {
            player1ID -> EPlayer.PLAYER1
            player2ID -> EPlayer.PLAYER2
            else -> null
        }
}

open class GameFight(
    id: Int,
    ranked : Boolean,
    player1ID: UserID,
    rules: Rules = Rules.defaultRules,
    player2ID: UserID,
    override val board1: Board,
    override val board2: Board,
    val turn: EPlayer,
) : GameSetup(id, ranked, player1ID, rules, player2ID) {
    fun isYourTurn(player: EPlayer) = player === turn
    override fun getBoard(player : EPlayer) = player.choose(board1, board2)
}

class GameFinished(
    id: Int,
    ranked : Boolean,
    player1ID: UserID,
    rules: Rules = Rules.defaultRules,
    player2ID: UserID,
    board1: Board,
    board2: Board,
    turn: EPlayer,
    val winner : EPlayer
) : GameFight(id, ranked, player1ID, rules, player2ID, board1, board2, turn)

typealias GameShot = Triple<GameFight, ShotResult, ShipType?>

fun GameSetup.placeBoard(player : EPlayer, board : Board) : Game {
    // if the other player has already placed a board, then putting our board will complete the game and make it fight
    val board1 = player.choose(board, board1);
    val board2 = player.choose(board2, board);
    return if(board1 != null && board2 != null) // if both boards are set, we can start the game fight
    {
        GameFight(
            id = id,
            ranked = ranked,
            player1ID = player1ID,
            rules = rules,
            player2ID = player2ID,
            board1 = board1,
            board2 = board2,
            turn = EPlayer.PLAYER1
        )
    } else {
        GameSetup(
            id = id,
            ranked = ranked,
            player1ID = player1ID,
            rules = rules,
            player2ID = player2ID,
            board1 = board1,
            board2 = board2,
        )
    }
}

fun GameFight.makeShot(player: EPlayer, position: Position): GameShot {
    if (!isYourTurn(player))
        return GameShot(this, ShotResult.INVALID, null)

    val enemyBoard = getBoard(player.other())
    val result = enemyBoard.makeShot(position)

    if (result.second === ShotResult.INVALID)
        return GameShot(this, ShotResult.INVALID, null)

    val newTurn = if (result.second === ShotResult.MISS)
        turn.other()
    else
        turn

    val newPlayer1Board = player.choose(board1, result.first)
    val newPlayer2Board = player.choose(result.first, board1)
    val newGame = if(result.first.checkIfLost()) {
        GameFinished(
            id = id,
            ranked = ranked,
            player1ID = player1ID,
            rules = rules,
            player2ID = player2ID,
            board1 = newPlayer1Board,
            board2 = newPlayer2Board,
            turn = newTurn,
            winner = player
        )
    } else {
        GameFight(
            id = id,
            ranked = ranked,
            player1ID = player1ID,
            rules = rules,
            player2ID = player2ID,
            board1 = newPlayer1Board,
            board2 = newPlayer2Board,
            turn = newTurn
        )
    }
    return GameShot(newGame, result.second, result.third)
}



