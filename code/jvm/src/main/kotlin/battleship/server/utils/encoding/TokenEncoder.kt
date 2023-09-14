package battleship.server.utils.encoding

import battleship.server.domain.TokenValidationInfo

/**
 * Interface responsible for encoding a token
 */
interface TokenEncoder {
    /**
     * Creates a [TokenValidationInfo] from a token
     * @param token the token create the [TokenValidationInfo] from
     * @return the [TokenValidationInfo] created from the token
     */
    fun createValidationInformation(token: String): TokenValidationInfo

    /**
     * Validates a token.
     * @param validationInfo the [TokenValidationInfo].
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    fun validate(validationInfo: TokenValidationInfo, token: String): Boolean
}