package battleship.server.repositories

import battleship.server.domain.*

interface UserRepository {

    /**
     * @return returns the uuid given to the user
     * @throws Exception if the repository cannot create a user it will throw an exception
     */
    fun createUser(username: String, email: String, passwordValidation: PasswordValidationInfo): UserID

    fun createToken(token : Token)

    fun doesUsernameExist(username: String): Boolean

    fun doesEmailExist(email: String): Boolean

    fun getUserById(id: UserID): User?

    fun getUserByEmail(email : String) : User?

    fun getTokenByTokenValidationInfo(tokenValidationInfo : TokenValidationInfo) : Pair<User, Token>?

    fun getUserRanking(page: Int, size : Int): List<User>
}

