package battleship.server.services

import battleship.server.domain.*
import battleship.server.http.controllers.models.UserEntity
import battleship.server.repositories.TransactionManager
import battleship.server.services.exceptions.*
import battleship.server.utils.Clock
import battleship.server.utils.encoding.TokenEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

const val RANKING_PAGE_SIZE = 20;

@Component
class HomeService(
    private val transactionManager: TransactionManager,
    private val tokenEncoder : TokenEncoder,
    private val userLogic : UserLogic,
    private val passwordEncoder: PasswordEncoder,
    val serverInfo: ServerInfo,
    private val clock: Clock,
    ) {



    /**
     * Creates a user with the given credentials.
     * @param username the username of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     * @return [UserCreationResultSuccessful] if successfully or a [UserCreationResultFailed] if it failed.
     */
    fun createUser(username : String, email : String, password : String) : Pair<String, Int> {
        if(!userLogic.isValidUsername(username)) throw BadRequestException("Username $username is not valid!")
        if(!userLogic.isValidEmail(email)) throw BadRequestException("Email $email is not valid!")
        if(!userLogic.isSafePassword(password)) throw BadRequestException("password $password is not valid!")
        val passwordValidation = PasswordValidationInfo(passwordEncoder.encode(password))

        return transactionManager.run {
            val repo = it.usersRepository
            if(repo.doesEmailExist(email)) throw ConflictException("User with email $email already exists!")
            if(repo.doesUsernameExist(username)) throw ConflictException("User with username $username already exists!")

            val userID = repo.createUser(username, email, passwordValidation)
            val token = userLogic.generateToken()
            val newToken = Token(tokenEncoder.createValidationInformation(token), userID);
            repo.createToken(newToken)
            return@run Token.tokenType + token to userID.id;
        }
    }


    /**
     * Creates a token for the user with the given id.
     * @param id the id of the user
     * @return the token
     */
    fun login(email : String, password : String): Pair<String, Int> {
        return transactionManager.run {
            val repo = it.usersRepository
            val user = repo.getUserByEmail(email) ?:
                throw NotFoundException("No user with email $email")
            if(!passwordEncoder.matches(password, user.password_validation.validationInfo))
                throw UnauthorizedException("Invalid credentials!")

            val token = userLogic.generateToken()
            val newToken = Token(tokenEncoder.createValidationInformation(token), UserID(user.uuid));
            
            repo.createToken(newToken)

            return@run Token.tokenType + token to user.uuid
        }
    }

    fun getRankingList(page: Int): List<UserEntity> {
        val res = transactionManager.run {
            // TODO: maybe movie ranking page size elsewhere
            it.usersRepository.getUserRanking(page, RANKING_PAGE_SIZE)
        }
        return res.map { user -> UserEntity(user.uuid, user.username, user.playCount, user.elo) }
    }

}
