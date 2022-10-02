package battleship.server.model

import com.google.gson.annotations.Expose

const val DIMENSION_COL_DEFAULT = 10
const val DIMENSION_ROW_DEFAULT = 10

data class Game(
    @Expose val lobbyName: String,
    @Expose val creatorName: String,
    val invitedUserName: String?,
    val winnerIsCreator: Boolean?,
    val movesList: List<Move>
)

data class Move( //todo
    val user: String,
    val data: Any
)

val defaultShipTypes = listOf(
    ShipType("Carrier", 5, 1),
    ShipType("Battleship", 4, 1),
    ShipType("Cruiser", 3, 1),
    ShipType("Submarine", 3, 1),
    ShipType("Destroyer", 2, 1)
)

/**
 * All ship types allowed in the game.
 * @property name [ShipType] name.
 * @property size Number of squares occupied.
 * @property quantity Number of ships of this type available.
 *
 */

data class ShipType(
    val name: String,
    val size : Int,
    val quantity: Int,
    val positionSlots: MutableList<Coordinate> = mutableListOf()
) {
    init {
        //check if ship is all horizontal or all vertical
    }
    fun isDestroyed() : Boolean {
        positionSlots.forEach {
            if(it.entity==Entity.SHIP) return false
        }
        return true
    }
}

data class Coordinate(
    val letter: Char, //line
    val number: Byte, //column
    val entity: Entity
)

enum class Entity{
    WATER, SHIP, DAMAGED
}




