import battleship.server.model.game.board.Coordinates

enum class Direction(val x : Int, val y : Int) {
    RIGHT(1, 0),
    DOWN(0, 1)
}

fun Coordinates.extend(range : Int, dir : Direction) : Coordinates {
    return Coordinates(this.column + dir.x * range, this.row + dir.y * range);
}
