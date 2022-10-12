package battleship.model.board

/**
 * All [Position] that make up the game
 * @property column describes position's [Column]
 * @property row describes position's [Row]
 * @property toString to string override function
 * @property values companion object that has all the possible values for [Position]
 */
class Position private constructor(val column: Column, val row: Row) {

    override fun toString() = "${column.letter}${row.number}"

    companion object {
        // private as to only get Positions from the .get() methods
        val values = List(COLUMN_DIM * ROW_DIM) {
            Position((it % COLUMN_DIM).indexToColumn(), (it / COLUMN_DIM).indexToRow())
        }

        /**
         * Getter function for getting a position.
         * @param indexColumn ordinal of the [Column].
         * @param indexRow ordinal of the [Row].
         * @return [Position].
         * @throws IndexOutOfBoundsException
         */
        operator fun get(indexColumn: Int, indexRow: Int): Position {
            if (indexColumn >= COLUMN_DIM || indexRow >= ROW_DIM)
                throw IndexOutOfBoundsException("Exceeded position bounds")
            return values[indexRow * COLUMN_DIM + indexColumn]
        }

        /**
         * Getter function for getting a position.
         * @param column key for the column.
         * @param row key for the row.
         * @return [Position].
         */
        operator fun get(column: Column, row: Row) = get(column.ordinal, row.ordinal)
    }
}

/**
 * Converts String into a [Position], null if such position does not exist
 * @return [Position]
 */
fun String.toPositionOrNull(): Position? {
    if (this.isBlank()) return null
    val left = this[0]
    if (!left.isLetter()) return null
    val right = this.drop(1).toIntOrNull() ?: return null
    val column = left.toColumnOrNull()
    val row = right.toRowOrNull()
    if (column == null || row == null) return null
    return Position[column, row]
}

/**
 * Converts string to a [Position]
 * @return [Position]
 * @throws IllegalStateException
 */
fun String.toPosition(): Position {
    return toPositionOrNull() ?: error("Invalid Position $this")
}

/**
 * Function that will move a [Position] X amount of times
 * @param dir direction to move the [Position]
 * @param amount amount of times to move the [Position]
 * @return [Position] moved
 * @throws IndexOutOfBoundsException if resulting position is invalid
 */
fun Position.movePosition(dir: Direction, amount: Int): Position {
    val x = this.column.ordinal + if (dir === Direction.HORIZONTAL) amount else 0
    val y = this.row.ordinal + if (dir === Direction.VERTICAL) amount else 0
    return Position[x, y]
}

/**
 * Function that calculates all positions form a line, Horizontal or Vertical.
 * @param pos line head position.
 * @param dir direction of a line.
 * @param length size of a line.
 * @return Returns a list with all the positions of a line
 */
fun getPositionsFromLine(pos: Position, dir: Direction, length: Int) = List(length) { pos.movePosition(dir, it) }

/** Returns a list of all positions in a rectangle to the destination position
 * @param topLeft Top left position of the hit box.
 * @param bottomRight Bottom right position of the hit box.
 * @return Returns a Set with the Box positions.
 * @throws IllegalArgumentException if [topLeft] isn't above and to the left of the [bottomRight] position
 */
fun makeRectangle(topLeft: Position, bottomRight: Position): Set<Position> {
    require(topLeft.column.ordinal <= bottomRight.column.ordinal)
    require(topLeft.row.ordinal <= bottomRight.row.ordinal)
    val positions = (topLeft.row.ordinal..bottomRight.row.ordinal).fold(listOf<Position>()) { acc, y ->
        acc + (topLeft.column.ordinal..bottomRight.column.ordinal).fold(listOf()) { acc2, x ->
            acc2 + Position[x, y]
        }
    }
    return positions.toSet()
}