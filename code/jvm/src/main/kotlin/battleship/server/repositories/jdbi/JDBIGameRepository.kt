package battleship.server.repositories.jdbi

import battleship.server.domain.game.*
import battleship.server.domain.game.board.Board
import battleship.server.repositories.GameRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.statement.Update
import org.postgresql.util.PGobject

private const val QUERY_NEW_GAME = "insert into GAME (ranked, player1, state, rules) values (:ranked, :hostID, :state, :rules);"

private const val QUERY_GET_GAME = "select * from GAME where id = :gameID;"
private const val QUERY_GET_WAITING_GAME = "select * from GAME where state = :state AND player1 != :uuid;"
private const val QUERY_GET_ACTIVE_GAME_LIST = "select id from GAME where player1 = :uuid OR player2 = :uuid;"

private const val UDPATE_GAME = "update GAME set state = :state, player2 = :player2, turn = :turn, winner = :winner, board1 = :board1, board2 = :board2 where id = :id;"

class JDBIGameRepository(private val handle : Handle) : GameRepository {
    override fun create(hostID: Int, rules: Rules, ranked: Boolean) : Int {
        return handle.createUpdate(QUERY_NEW_GAME)
            .bind("ranked", ranked)
            .bind("hostID", hostID)
            .bind("state", GameStateModel.WAITING)
            .bindRules("rules", rules)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()
    }

    override fun getByID(gameID: Int): Game? {
        return handle.createQuery(QUERY_GET_GAME)
            .bind("gameID", gameID)
            .mapTo<GameDBModel>()
            .firstOrNull()?.toGame()

    }

    override fun update(game: Game) {
        val state = when(game) {
            is GameFinished -> GameStateModel.FINISHED
            is GameFight    -> GameStateModel.FIGHTING
            is GameSetup    -> GameStateModel.PLANNING
            is GameWaiting  -> GameStateModel.WAITING
        }
        
        val player2 = (game as? GameSetup)?.player2ID

        val turn = (game as? GameFight)?.turn

        val board1 = (game as? GameSetup)?.board1
        val board2 = (game as? GameSetup)?.board2
        val winner = (game as? GameFinished)?.winner

        handle.createUpdate(UDPATE_GAME)
            .bind("id", game.id)
            .bind("state", state)
            .bind("player2", player2?.id)
            .bind("turn", turn)
            .bind("winner", winner)
            .bindBoard("board1", board1, game.rules)
            .bindBoard("board2", board2, game.rules)
            .execute()
    }

    override fun getAvailableGame(uuid: Int) : Game? {
        return handle.createQuery(QUERY_GET_WAITING_GAME)
            .bind("state", GameStateModel.WAITING)
            .bind("uuid", uuid)
            .mapTo<GameDBModel>()
            .firstOrNull()?.toGame()
    }

    override fun getActiveGames(uuid: Int): List<Int> {
        return handle.createQuery(QUERY_GET_ACTIVE_GAME_LIST)
            .bind("uuid", uuid)
            .mapTo<Int>()
            .list()
    }

    companion object {
        private val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

        private fun Update.bindRules(name: String, rules : Rules) = run {
            bind(name,
                PGobject().apply {
                    type = "jsonb"
                    value = serializeToJson(rules)
                }
            )
        }
        private fun Update.bindBoard(name: String, board : Board?, rules : Rules) = run {
            bind(name,
                board?.let { board ->
                    PGobject().apply {
                        type = "jsonb"
                        value = serializeBoard(board, rules)
                    }
                }
            )
        }

        private fun serializeBoard(content: Board, rules : Rules): String {
            return serializeToJson(content.toBoardDto(rules))
        }
        private fun serializeToJson(content: Any?): String = objectMapper.writeValueAsString(content)

        fun deserializeRulesFromJson(json: String): Rules = objectMapper.readValue(json, Rules::class.java)
        fun deserializeBoardFromJson(json: String): BoardDto? {
            return objectMapper.readValue(json, BoardDto::class.java)
        }

    }
}

