package battleship.server.domain.game

import battleship.server.domain.game.ship.ShipType


data class Dimension(val columnDim: Int, val rowDim: Int) {
    init {
        require(columnDim >= 5)
        require(rowDim >= 5)
    }
}

/**
 * Default Ship Types Values in case of no configuration from the user
 */
private val defaultShipTypes = listOf(
    ShipType("Carrier", 5, 1),
    ShipType("Battleship", 4, 1),
    ShipType("Cruiser", 3, 1),
    ShipType("Submarine", 3, 1),
    ShipType("Destroyer", 2, 1)
)

/**
 * Rules of the game
 *
 * @param boardDimension the dimension of the board
 * @param shipTypes the ship types available
 * @param shotsPerTurn the number of shots per turn
 * @param timePerTurn the time per turn
 * @param timePerGame the time per game
 */
class Rules(
    val boardDimension: Dimension = Dimension(10, 10),
    val shiptypes: List<ShipType> = defaultShipTypes,
    //val shotsPerTurn: Int = 1,
    //val timePerTurn: Long = 60000, // 1 minute
    //val timePerGame: Long? = null // null means no time limit
) {

    init {
        //TODO: set limitations to rules here with alot of require()
    }

    companion object {
        /**
         * Default Rules
         */
        val defaultRules = Rules()
    }
}

