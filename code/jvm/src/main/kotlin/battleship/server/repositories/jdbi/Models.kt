package battleship.server.repositories.jdbi

import battleship.server.domain.UserID
import battleship.server.domain.game.*
import battleship.server.domain.game.board.*
import battleship.server.domain.game.ship.Ship
import battleship.server.services.exceptions.NotImplementedException

const val GAME_STATE_WAITING = "waiting"
const val GAME_STATE_PLANNING = "planning"
const val GAME_STATE_FIGHTING = "fighting"
const val GAME_STATE_FINISHED = "finished"

enum class GameStateModel { WAITING, PLANNING, FIGHTING, FINISHED }

data class ShipDto(
    val type : String,
    val x : Int,
    val y : Int,
    val horizontal : Boolean
)

data class BoardDto(
    val grid : List<String>,
    val ships : List<ShipDto>
)

const val CELL_EMPTY = ' '
const val CELL_SHIP = 'S'
const val CELL_HIT = 'x'
const val CELL_SUNK = 'X'
const val CELL_MISS = 'O'

fun Board.toBoardDto(rules : Rules) : BoardDto
{
    val cols = rules.boardDimension.columnDim
    val rows = rules.boardDimension.rowDim
    val baseGrid = MutableList(rows) { CELL_EMPTY.toString().repeat(cols).toCharArray() }
    grid.entries.forEach {
        val cell = when(it.value) {
            is MissCell -> CELL_MISS
            is ShipSunk -> CELL_SUNK
            is ShipHit  -> CELL_HIT
            is ShipCell -> CELL_SHIP
        }
        baseGrid[it.key.row][it.key.column] = cell
    }

    val ships = fleet.map {
        val direction = when(it.direction) {
            Direction.RIGHT -> true
            Direction.DOWN -> false
        }
        ShipDto(it.type.name, it.head.column, it.head.row, direction)
    }

    return BoardDto(baseGrid.map{String(it)}, ships)
}

fun BoardDto.toBoard(rules : Rules) : Board
{
    // First build ships
    val ships = ships.map {
        val type = rules.shiptypes.first { type -> type.name == it.type }
        val direction = if(it.horizontal) Direction.RIGHT else Direction.DOWN
        Ship(type, Position(it.x, it.y), direction)
    }

    // Then build the cells, first ship cells, then miss cells and damage the ship cells
    val map = HashMap<Position, Cell>()
    ships.forEach { ship ->
        ship.positions.forEach { pos ->
            map[pos] = ShipCell(ship, pos)
        }
    }

    this.grid.forEachIndexed { y, row ->
        row.forEachIndexed { x, elem ->
            val pos = Position(x, y)
            val ship = (map[pos] as? ShipCell)?.ship
            val cell = when(elem) {
                CELL_SUNK -> ShipSunk(ship!!, pos)
                CELL_HIT  -> ShipHit(ship!!, pos)
                CELL_SHIP -> ShipCell(ship!!, pos)
                CELL_MISS -> MissCell(pos)
                else ->  null
            }
            cell?.let { map[pos] = it }
        }
    }

    return Board(map, ships)
}

class GameDBModel(
    val id : Int,
    val ranked : Boolean,
    val state : GameStateModel,
    val rules: Rules,
    val player1 : UserID,
    val player2 : UserID?,
    val turn : EPlayer?,
    val board1: BoardDto?,
    val board2: BoardDto?,
    val winner: EPlayer?
) {
    fun toGame() : Game {
        val newBoard1 = board1?.toBoard(rules)
        val newBoard2 = board2?.toBoard(rules)

        when(state) {
            GameStateModel.WAITING -> {
                return GameWaiting(
                    id = id,
                    ranked = ranked,
                    player1ID = player1,
                    rules = rules
                )
            }
            GameStateModel.PLANNING -> {
                checkNotNull(player2);
                return GameSetup(
                    id = id,
                    ranked = ranked,
                    player1ID = player1,
                    player2ID = player2,
                    rules = rules,
                    board1 = newBoard1,
                    board2 = newBoard2
                )
            }
            GameStateModel.FIGHTING -> {
                checkNotNull(player2)
                checkNotNull(newBoard1)
                checkNotNull(newBoard2)
                checkNotNull(turn)
                return GameFight(
                    id = id,
                    ranked = ranked,
                    player1ID = player1,
                    player2ID = player2,
                    rules = rules,
                    turn = turn,
                    board1 = newBoard1,
                    board2 = newBoard2
                )
            }
            GameStateModel.FINISHED -> {
                checkNotNull(player2)
                checkNotNull(newBoard1)
                checkNotNull(newBoard2)
                checkNotNull(turn)
                checkNotNull(winner)
                return GameFinished(
                    id = id,
                    ranked = ranked,
                    player1ID = player1,
                    player2ID = player2,
                    rules = rules,
                    turn = turn,
                    board1 = newBoard1,
                    board2 = newBoard2,
                    winner = winner
                )
            }
            else -> {
                throw Exception("Invalid game state")
            }
        }
    }
}
