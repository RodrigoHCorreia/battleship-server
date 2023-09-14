package battleship.server.domain

import java.time.Instant

/**
 * Represents the token that is used to authenticate a user.
 *
 * @param tokenValidationInfo the [TokenValidationInfo] that is used to authenticate a user
 * @param userId the id of the user that is authenticated
 * @param createdAt the time when the token was created
 * @param lastUsedAt the time when the token was last used
 */
class Token(
    val tokenValidationInfo: TokenValidationInfo,
    val userID: UserID,
    //val createdAt: Instant,
    //val lastUsedAt: Instant

){
    companion object {
        const val tokenType = "Bearer "
    }
}