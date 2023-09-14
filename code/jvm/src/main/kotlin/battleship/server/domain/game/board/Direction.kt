import battleship.server.domain.game.board.Position

enum class Direction(val x: Int, val y: Int) {
    RIGHT(1, 0),
    DOWN(0, 1)

}

fun Position.extend(range: Int, dir: Direction): Position {
    return Position(this.column + dir.x * range, this.row + dir.y * range)
}

