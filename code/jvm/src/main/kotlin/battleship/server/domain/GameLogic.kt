package battleship.server.domain

import battleship.server.domain.game.*
import battleship.server.domain.game.board.*
import battleship.server.services.BoardProposal
import battleship.server.services.GameInfo
import battleship.server.services.GameService
import battleship.server.services.exceptions.BadRequestException
import battleship.server.services.exceptions.ForbiddenException
import battleship.server.services.exceptions.NotFoundException
import battleship.server.services.toState
import org.springframework.stereotype.Component

data class BoardInfo(
    val grid : List<String>
)

const val CELL_EMPTY = ' '
const val CELL_MISS = 'O'
const val CELL_SUNK = 'X'
const val CELL_HIT = 'x'
const val CELL_SHIP = 'N'

@Component
class GameLogic {

    fun getGameInfo(game : Game, clientID : UserID) : GameInfo
    {
        val canShoot = if(game is GameFight) game.turn == game.getPlayerOrNull(clientID) else false
        return GameInfo(
            id = game.id,
            ranked = game.ranked,
            opponent = getOpponent(game, clientID),
            game.toState(),
            canShoot
        )
    }

    fun getOpponent(game : Game, userID: UserID) : UserID? {
        if(game !is GameSetup) return null
        return when(userID) {
            game.player1ID -> game.player2ID
            game.player2ID -> game.player1ID
            else -> null
        }
    }

    fun getBoard(game : Game, userID: UserID, opponent : Boolean) : BoardInfo
    {
        if (game !is GameSetup) throw BadRequestException("Game must past the waiting phase")

        val player = game.getPlayerOrNull(userID) ?: throw ForbiddenException("You are not participating in this game.")

        if (opponent && game !is GameFight) throw BadRequestException("No peeking the enemy board before starting!")

        val targetPlayer = if(opponent) player.other() else player
        val board = game.getBoard(targetPlayer) ?: throw NotFoundException("No existing board in game ${game.id}")

        val dim = game.rules.boardDimension
        val grid = List(dim.rowDim) { CELL_EMPTY.toString().repeat(dim.columnDim).toCharArray() }
        board.grid.entries.forEach {
            val pos = it.key
            val char : Char = when(it.value) {
                is MissCell -> CELL_MISS
                is ShipSunk -> CELL_SUNK
                is ShipHit  -> CELL_HIT
                is ShipCell -> if(opponent) CELL_EMPTY else CELL_SHIP
            }
            grid[pos.row][pos.column] = char
        }
        return BoardInfo( grid.map{ String(it) } )
    }


    fun doPlace(game : Game, userID : UserID, placement : BoardProposal) : Pair<Game, PlaceResult>
    {
        if(game !is GameSetup || game is GameFight) throw BadRequestException("Game must be in the planning phase")
        val player = game.getPlayerOrNull(userID) ?: throw ForbiddenException("You must be participating in this game")

        // First Build Board
        val board = placement.placement.fold(Board()) { board, shipP ->
            // TODO, Improve all of this function, insuring quantity of ships later on too
            val shipType = game.rules.shiptypes.firstOrNull { it.name == shipP.type }
                ?: return game to PlaceResult.INVALID

            val res = board.placeShip(
                type = shipType,
                head = shipP.head,
                direction = shipP.direction
            )

            if (res.second == PlaceResult.INVALID) return game to PlaceResult.INVALID

            return@fold res.first
        }

        // Check if board has correct number of ship types in the game
        game.rules.shiptypes

        //Then place the board on the game and return the game
        return game.placeBoard(player, board) to PlaceResult.PLACED
    }

    fun doShoot(game : Game, userID : UserID, target : Position) : Triple<Game, ShotResult, String?>
    {
        if(game !is GameFight || game is GameFinished) throw BadRequestException("Game must be in the fighting phase")

        val player = game.getPlayerOrNull(userID) ?: throw ForbiddenException("You must be participating in this game")
        if(target.column !in 0 until game.rules.boardDimension.columnDim ||
           target.row !in 0 until game.rules.boardDimension.rowDim)
            throw BadRequestException("Position must be in board dimensions!")

        val res = game.makeShot(player, target)

        return Triple(res.first, res.second, res.third?.name)
    }

}
