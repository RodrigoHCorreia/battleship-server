package battleship.server.model

data class User(
    val uuid: Int, // to be incremented automatically
    val username: String,
    val password: String,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0 //WON GAMES <= PLAYED GAMES, ez clap
) {
    fun getUserView() = UserView(username, gamesPlayed, gamesWon)
}
data class UserView(val username: String, val gamesPlayed: Int, val gamesWon: Int)

