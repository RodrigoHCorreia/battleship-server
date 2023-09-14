package battleship.server.domain.game

/**
 * Used to differentiate between players in a Game
 */
enum class EPlayer {
    PLAYER1, PLAYER2;

    fun other() = if (this === PLAYER1) PLAYER2 else PLAYER1

    fun <T> choose(item1: T, item2: T) =
        if (this === PLAYER1) item1 else item2
}