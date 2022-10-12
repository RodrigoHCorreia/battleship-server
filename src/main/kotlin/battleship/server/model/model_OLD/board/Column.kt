package battleship.model.board

const val COLUMN_DIM = 10
private const val COLUMN_FIRST_LETTER = 'A'

/**
 * All [Column] that make up the game.
 * @property letter Character associated with the position, starting from [COLUMN_FIRST_LETTER]
 * @property ordinal Column's index in the board
 * @property values companion object that has all the possible values for [Column]
 */
class Column private constructor(idx: Int) {
    val letter = COLUMN_FIRST_LETTER + idx
    val ordinal = idx

    companion object {
        val values = List(COLUMN_DIM) { Column(it) }
    }
}

/**
 * Return a [Column] according to its letter position, null if it does not exist
 */
fun Char.toColumnOrNull() = Column.values.elementAtOrNull(this.uppercaseChar() - COLUMN_FIRST_LETTER)

/**
 * Return a [Column] according to its index, null if it does not exit
 */
fun Int.indexToColumnOrNull() = Column.values.elementAtOrNull(this)

/**
 * Return a [Column] object according to its index
 * @throws IndexOutOfBoundsException
 */
fun Int.indexToColumn() = Column.values[this]
