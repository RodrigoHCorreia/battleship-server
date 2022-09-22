package battleship.server.model

const val DIMENSION_COL_DEFAULT = 10;
const val DIMENSION_ROW_DEFAULT = 10;


val defaultShipTypes = listOf(
    ShipType("Carrier", 5, 1),
    ShipType("Destroyer", 5, 2),
    ShipType("Frigate", 5, 3),
    ShipType("Carrier", 5, 4)
)

data class Game(
    val shipTypes = defaultShipTypes
)




