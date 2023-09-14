package battleship.server.domain.game.board

data class Position(
    val column: Int,
    val row: Int
) {
    init {
        require(column >= 0)
        require(row >= 0)
    }
}
