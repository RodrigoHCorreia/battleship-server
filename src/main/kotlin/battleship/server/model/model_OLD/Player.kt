package battleship.model

/**
 * Players available in the game
 * @property id of the [Player]
 * @property other returns the other player
 */
enum class Player(private val id: Char) {
    A('A'), B('B');

    fun other() = if (this === A) B else A
}

/**
 * Helper function to decide which item to choose depending on the player
 * @property player
 * @property playerAItem the item to be returned in case [player] is A
 * @property playerBItem the item to be returned in case [player] is B
 */
fun <T> chooseUponPlayer(player: Player, playerAItem: T, playerBItem: T) =
    if (player === Player.A) playerAItem else playerBItem