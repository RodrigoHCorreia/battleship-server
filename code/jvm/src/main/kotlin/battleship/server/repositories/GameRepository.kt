package battleship.server.repositories

import battleship.server.domain.game.Game
import battleship.server.domain.game.Rules

interface GameRepository {

    fun create(hostID: Int, rules: Rules, ranked: Boolean) : Int
    fun getByID(gameID : Int) : Game?
    fun update(game : Game)
    fun getAvailableGame(uuid: Int) : Game?

    /**
     * @param uuid used to check what games the user is participating in
     * @return returns a list of Game IDs where uuid is participating and are active
     */
    fun getActiveGames(uuid: Int): List<Int>

}
