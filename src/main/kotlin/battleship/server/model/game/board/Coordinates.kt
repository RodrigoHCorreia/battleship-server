package battleship.server.model.game.board

class Coordinates(
    val column : Int,
    val row : Int
) {
    init {
        require(column > 0)
        require(row > 0)
    }
}
