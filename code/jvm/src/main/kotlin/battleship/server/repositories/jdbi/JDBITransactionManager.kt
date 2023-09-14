package battleship.server.repositories.jdbi

import battleship.server.repositories.GameRepository
import battleship.server.repositories.Transaction
import battleship.server.repositories.TransactionManager
import battleship.server.repositories.UserRepository
import battleship.server.services.exceptions.BadGatewayException
import battleship.server.services.exceptions.BattleshipException
import battleship.server.services.exceptions.InternalErrorException
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.sql.SQLException

@Component
class JdbiTransactionManager(
    private val jdbi: Jdbi
) : TransactionManager {

    override fun <R> run(callback: (Transaction) -> R): R {
        return try {
            jdbi.inTransaction<R, SQLException> { handle ->
                val transaction = JdbiTransaction(handle)
                try {

                    return@inTransaction callback(transaction)

                } catch (ex: Exception) { // if sql throws exception here is most likely a malformed sql query
                    println("An SQL Exception has occurred!");
                    println(ex.message)
                    transaction.rollback()

                    // BattleshipExceptions are preseved by being thrown again
                    // Since catch apparently doesnt catch SQLException type exceptions
                    // unless the its the exception class
                    val newException =
                        if(ex is BattleshipException) ex
                        else InternalErrorException("An internal error has ocurred with the database")
                    throw newException
                }
            }
        } catch (ex: Exception) { // if sql throws an exception here it's most likely bad connection
            println("An SQL Connection has died!");
            println(ex.message)
            throw BadGatewayException("An internal error has ocurred with the database")
        }
    }

}


