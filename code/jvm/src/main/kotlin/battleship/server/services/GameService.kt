package battleship.server.services

import Direction
import battleship.server.domain.BoardInfo
import battleship.server.domain.GameLogic
import battleship.server.domain.UserID
import battleship.server.domain.game.*
import battleship.server.domain.game.board.*
import battleship.server.repositories.Transaction
import battleship.server.repositories.TransactionManager
import battleship.server.services.exceptions.NotFoundException
import org.springframework.stereotype.Component


enum class  GameState { WAITING, PLANNING, FIGHTING }

fun Game.toState() = when(this) {
    is GameFight -> GameState.FIGHTING
    is GameSetup -> GameState.PLANNING
    is GameWaiting -> GameState.WAITING
}

data class GameInfo (
    val id : Int,
    val ranked : Boolean,
    val opponent : UserID?,
    val state : GameState,
    val canShoot : Boolean
);


data class ShipProposal(
    val type: String,
    val head: Position,
    val direction: Direction
)

data class BoardProposal(
    val placement: List<ShipProposal>
)


@Component
class GameService(
    private val transactionManager: TransactionManager,
    private val gameLogic: GameLogic
) {
    private fun Transaction.getGame(gameID : Int)
        = gameRepository.getByID(gameID) ?: throw NotFoundException("Game with id $gameID does not exist!")


    fun getActiveGames(userID : UserID) : List<Int> {
        return transactionManager.run {
            it.gameRepository.getActiveGames(userID.id)
        }
    }

    fun getGame(userID : UserID, gameID : Int) : GameInfo {
        val game = transactionManager.run {
            it.getGame(gameID)
        }

        return gameLogic.getGameInfo(game, userID)
    }


    fun doMatch(userID : UserID) : GameInfo {
        return transactionManager.run {
            val availableGame = it.gameRepository.getAvailableGame(userID.id)
            if(availableGame == null) {
                val gameID = it.gameRepository.create(userID.id, Rules.defaultRules, true)
                return@run GameInfo(gameID, true, null, GameState.WAITING, false)
            } else {
                val newGame = GameSetup(
                    id = availableGame.id,
                    ranked = availableGame.ranked,
                    player1ID = availableGame.player1ID,
                    player2ID = userID,
                    rules = availableGame.rules
                )
                it.gameRepository.update(newGame)
                return@run gameLogic.getGameInfo(newGame, userID)
            }
        }
    }

    fun place(gameID: Int, userID: UserID, proposal: BoardProposal) : PlaceResult
    {
        return transactionManager.run { transaction ->
            val game = transaction.getGame(gameID)
            val res = gameLogic.doPlace(game, userID, proposal)
            if(res.second == PlaceResult.PLACED)
                transaction.gameRepository.update(res.first)

            return@run res.second
        }
    }


    // Inside Game Object
    fun shoot(gameID : Int, userID : UserID, target : Position) : Pair<ShotResult, String?> {
        return transactionManager.run { transaction ->
            val game = transaction.getGame(gameID)

            val res = gameLogic.doShoot(game, userID, target)

            if(res.second != ShotResult.INVALID)
                transaction.gameRepository.update(res.first)

            val resGame = res.first
            if(game.ranked && resGame is GameFinished) {
                //TODO update elos
                val user1 = transaction.usersRepository.getUserById(resGame.player1ID)
                val user2 = transaction.usersRepository.getUserById(resGame.player2ID)


            }

            return@run res.second to res.third
        }
    }

    fun getBoard(gameID: Int, userID: UserID, opponent: Boolean): BoardInfo {
        val game = transactionManager.run { it.getGame(gameID) }
        return gameLogic.getBoard(game, userID, opponent)
    }
}