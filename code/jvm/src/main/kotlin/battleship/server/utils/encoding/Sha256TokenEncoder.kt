package battleship.server.utils.encoding

import battleship.server.domain.TokenValidationInfo
import java.security.MessageDigest
import java.util.*

/**
 * Encodes a token using SHA-256
 *
 */
class Sha256TokenEncoder : TokenEncoder {

    override fun createValidationInformation(token: String): TokenValidationInfo =
        TokenValidationInfo(hash(token))

    override fun validate(validationInfo: TokenValidationInfo, token: String): Boolean =
        validationInfo.validationInfo == hash(token)

    /**
     * Hashes a token using SHA-256
     * @param input the token to hash
     * @return the hashed token
     */
    private fun hash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA256")
        return Base64.getUrlEncoder().encodeToString(
            messageDigest.digest(
                Charsets.UTF_8.encode(input).array()
            )
        )
    }
}