package battleship.model.board

/**
 * Directions available in the game
 */
enum class Direction {
    HORIZONTAL, VERTICAL
}

/**
 * Converts String into a [Direction], null if such direction does not exist
 * @return returns the direction from the first letter of the string
 */
fun String.toDirectionOrNull(): Direction? {
    if (this.isBlank()) return null
    return Direction.values().firstOrNull { this[0].equals(it.name[0], true) }
}

/**
 * Converts String into a [Direction], null if such direction does not exist
 * @return returns the direction from the first letter of the string
 */
fun String.toDirection(): Direction = toDirectionOrNull() ?: error("Invalid direction $this")
