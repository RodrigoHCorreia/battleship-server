package battleship.server.repositories.jdbi

import battleship.server.repositories.GameRepository
import battleship.server.repositories.Transaction
import battleship.server.repositories.TransactionManager
import battleship.server.repositories.UserRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val usersRepository: UserRepository by lazy { JDBIUserRepository(handle) }
    override val gameRepository: GameRepository by lazy { JDBIGameRepository(handle) }

    override fun rollback() {
        handle.rollback()
    }
}



