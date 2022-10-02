package battleship.server.model

data class User(
    val uuid: Int, // to be incremented automatically
    val player: Player,
    val hashedPassword: String,
)

data class Player(
    val username: String, //must be unique on creation
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0
)

