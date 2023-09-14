package battleship.server.repositories


interface Transaction {

    val usersRepository: UserRepository
    val gameRepository : GameRepository

    fun rollback()
}