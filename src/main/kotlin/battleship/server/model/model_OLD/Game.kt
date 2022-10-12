package battleship.model

import battleship.model.PlayError.*
import battleship.model.board.*
import battleship.model.ship.ShipType
import battleship.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * available play errors
 * @property NONE no error associated;
 * @property INVALID_SHOT invalid shot taken;
 * @property INVALID_TURN invalid turn;
 * @property GAME_OVER game over.
 */
enum class PlayError {
    NONE, INVALID_SHOT, INVALID_TURN, GAME_OVER
}


/**
 * Central object that represents the battleship game
 * @property playerBoard [Board] of the first player
 */
sealed class Game(val playerBoard: Board)

class GameSetup(playerBoard: Board) : Game(playerBoard)

class GameFight(
    playerBoard: Board,
    val enemyBoard: Board,
    val name: String,
    val player: Player,
    val turn: Player = Player.A
) : Game(playerBoard)

/**
 * [Game] Function that will start the game if it meets the requirements
 * @param gameName name of the game
 * @param st storage to use
 * @return updated [Game]
 */
suspend fun GameSetup.startGame(gameName: String, st: Storage): GameFight {
    val player = st.start(gameName, playerBoard)
    val gameFight = GameFight(playerBoard, Board(), name = gameName, player = Player.A)
    return if (player == Player.B) {
        val gameFromDB = st.load(gameFight)
        val newGame = GameFight(
            enemyBoard = gameFromDB.playerBoard,
            playerBoard = playerBoard,
            name = gameName,
            player = Player.B
        )
        newGame.also { st.store(it) }
    } else {
        gameFight
    }
}

/**
 * Keeps the game and the correspondent consequence of a put.
 * @property [Game] battleship game;
 * @property [PutConsequence] after the put was done.
 */
typealias GamePut = Pair<GameSetup, PutConsequence>


/**
 * [Game] Function that will put a ship if it's a valid command and ship
 * @param type [ShipType] of the ship to put
 * @param pos head [Position] of the ship
 * @param dir [Direction] of the ship
 * @return updated [GamePut]
 */
fun GameSetup.putShip(type: ShipType, pos: Position, dir: Direction): GamePut {

    val result = playerBoard.putShip(type, pos, dir)
    return GamePut(GameSetup(result.first), result.second)
}

/**
 * Function that puts a random ship in the [Board].
 * @receiver Game to alter.
 * @param type Type of ship.
 * @return Returns a game and it's consequence.
 */
fun GameSetup.putRandomShip(type: ShipType): GamePut {
    val result = playerBoard.putRandomShip(type)
    return GamePut(GameSetup(result.first), result.second)
}


/**
 * [GameSetup] Function that will add all ships if it's a valid command
 * @return updated [Game]
 */
fun GameSetup.putAllShips(): GamePut {
    val result = playerBoard.putAllShips()
    return GamePut(GameSetup(result.first), result.second)
}


/**
 * [GameSetup] Function that will remove a ship if it's a valid command and ship
 * @param pos [Position] to remove the ship from
 * @return updated [Game]
 */
fun GameSetup.removeShip(pos: Position): GameSetup {
    return GameSetup(playerBoard.removeShip(pos))
}

/**
 *[Game] Function that will empty the player board
 * @return updated [Game]
 */
fun GameSetup.removeAll(): GameSetup {
    return createEmptyGame()
}


/**
 *[Game] Function that will create the initial game
 * @return created game
 */
fun createEmptyGame() = GameSetup(Board())

/**
 * [Game] Function that will make a shot if it's a valid state and a valid shot
 * @param pos [Position] from the shot
 * @return [GameShot] with the updated [Game] and [ShotConsequence] associated
 */
fun GameFight.makeShot(pos: Position, st: Storage, scope: CoroutineScope): GameShot {

    if (isNotYourTurn) {
        return GameShot(this, ShotConsequence.NOT_YOUR_TURN, null)
    }

    val boardResult = enemyBoard.makeShot(pos)

    if (boardResult.second === ShotConsequence.INVALID)
        return GameShot(this, ShotConsequence.INVALID, null)

    val newTurn = if (boardResult.second === ShotConsequence.MISS)
        turn.other()
    else
        turn

    val newGame = GameFight(playerBoard, boardResult.first, name, player, newTurn)

    scope.launch { st.store(newGame) }

    return GameShot(newGame, boardResult.second, boardResult.third)
}

val GameFight.isYourTurn get() = turn === player
val GameFight.isNotYourTurn get() = !isYourTurn

val Game.hasStarted get() = this !is GameSetup
val Game.hasNotStarted get() = !hasStarted

val GameFight.winner : Player?
    get() {
        return if(playerBoard.lost())
            player.other()
        else if(enemyBoard.fleet.isNotEmpty() && enemyBoard.lost())
            player
        else
            null
    }
