package battleship.model.board

import battleship.model.board.Column.Companion.values
import battleship.model.board.Row.Companion.values

const val ROW_DIM = 10
private const val ROW_FIRST_NUMBER = 1

/**
 * All [Row] that make up the game.
 * @property number Digit associated with the position, starting from ROW_FIRST_NUMBER
 * @property ordinal Row's index in the board
 * @property values companion object that has all the possible values for [Row]
 */
class Row private constructor(idx: Int) {
    val number = ROW_FIRST_NUMBER + idx
    val ordinal = idx

    companion object {
        val values = List(ROW_DIM) { Row(it) }
    }
}

/**
 * Return a [Row] according to its digit position, null if it does not exist
 */
fun Int.toRowOrNull() = Row.values.elementAtOrNull(this - ROW_FIRST_NUMBER)

/**
 * Return a [Row] according to its index, null if it does not exit
 */
fun Int.indexToRowOrNull() = Row.values.elementAtOrNull(this)

/**
 * Return a [Row] object according to its index
 * @throws IndexOutOfBoundsException
 */
fun Int.indexToRow() = Row.values[this]
