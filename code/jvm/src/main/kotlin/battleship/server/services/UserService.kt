package battleship.server.services

import battleship.server.domain.UserID
import battleship.server.domain.UserLogic
import battleship.server.repositories.TransactionManager
import battleship.server.services.exceptions.InternalErrorException
import battleship.server.services.exceptions.NotFoundException
import battleship.server.utils.Clock
import battleship.server.utils.encoding.TokenEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

//const val ILLEGAL_CHARACTER = " \"'$"

data class UserByIDResult(
    val id : Int,
    val username : String,
    val playCount : Int,
    val elo : Int
)

@Component
class UserService(
    private val transactionManager: TransactionManager,
    private val tokenEncoder: TokenEncoder,
    ) {

    fun getUserByID(id : UserID) : UserByIDResult {
        val user = transactionManager.run {
            it.usersRepository.getUserById(id)
        } ?: throw NotFoundException("An user with id $id does not exist!")

        return UserByIDResult(
            id = user.uuid,
            username = user.username,
            playCount = user.playCount,
            elo = user.elo);
    }

    fun getIDByToken(token : String) : UserID? {
        val userInfo = transactionManager.run {
            val tokenValidationInfo = tokenEncoder.createValidationInformation(token)
            it.usersRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
        }

        return userInfo?.let { UserID(it.first.uuid) }
    }
}


