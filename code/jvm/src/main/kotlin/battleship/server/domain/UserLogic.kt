package battleship.server.domain

import battleship.server.utils.isInRegex
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.*
import java.util.regex.Pattern

private const val PASSWORD_MIN_SIZE = 4
private const val PASSWORD_MAX_SIZE = 24
private const val USERNAME_MIN_SIZE = 4
private const val USERNAME_MAX_SIZE = 16

/**
 * User logic.
 *
 */
@Component
class UserLogic {

    /**
     * Generate a token.
     *
     * @return the token
     */
    fun generateToken(): String =
        ByteArray(TOKEN_BYTE_SIZE).let { byteArray ->
            SecureRandom.getInstanceStrong().nextBytes(byteArray)
            Base64.getUrlEncoder().encodeToString(byteArray)
        }

    /**
     * Check if [token] is a valid token.
     *
     * @param token the token to check
     * @return true if [token] is a valid token, false otherwise
     */
    fun canBeToken(token: String): Boolean = try {
        Base64.getUrlDecoder()
            .decode(token).size == TOKEN_BYTE_SIZE
    } catch (ex: IllegalArgumentException) {
        false
    }

    /**
     * Check if the password is between min and max size and has at least one uppercase letter, one lowercase letter, one number and one special character
     * Also check that it doesn't contain more than 2 same characters in a row.
     *
     * @return true if the password is safe
     */
    fun isSafePassword(password: String): Boolean {
        //TODO: put the regex back in place, its just useful turned off lol
        /*
        val sameCharsRegex = Regex("(.)\\1{2,}")
        password.matches(sameCharsRegex).let {
            if (it) return false
        }
        val passwordRulesRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{$PASSWORD_MIN_SIZE,$PASSWORD_MAX_SIZE}$"
        return password.isInRegex(passwordRulesRegex)
         */
        return true
    }

    fun isValidEmail(email: String) = email.isInRegex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")

    /**
     * Check if the username is between min and max size and has only letters and numbers.
     */
    fun isValidUsername(username: String): Boolean {
        val usernameRulesRegex = "^[a-zA-Z0-9]{$USERNAME_MIN_SIZE,$USERNAME_MAX_SIZE}$"
        return username.isInRegex(usernameRulesRegex)
    }

    companion object {
        private const val TOKEN_BYTE_SIZE = 256 / 8
    }
}