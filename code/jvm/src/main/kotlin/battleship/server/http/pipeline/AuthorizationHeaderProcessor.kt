package battleship.server.http.pipeline

import battleship.server.http.pipeline.AuthorizationHeaderProcessor.Companion.SCHEME
import battleship.server.http.pipeline.AuthorizationHeaderProcessor.Companion.logger
import battleship.server.domain.UserID
import battleship.server.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Class that processes the Authorization header of a request.
 * @param usersService The service that handles the users.
 * @property SCHEME The scheme that the authorization header must follow.
 * @property logger The (logger) of the class.
 */
@Component
class AuthorizationHeaderProcessor(
    val usersService: UserService
) {

    /**
     * Processes the authorization header of a request, making sure it's in the right syntax and returning the [User] if it is.
     * @param authorizationValue The value of the authorization header, null if none could be retreived
     */
    fun process(authorizationValue: String?): UserID? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2 || parts[0].lowercase() != SCHEME)
            return null

        return usersService.getIDByToken(parts[1])
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthorizationHeaderProcessor::class.java)
        const val SCHEME = "bearer"
    }
}