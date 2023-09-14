package battleship.server.repositories.jdbi

import battleship.server.domain.*
import battleship.server.repositories.UserRepository
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo

private const val QUERY_NEW_PLAYER =
    "insert into USERS (username, email, password_validation) values (:username, :email, :password_validation);"

private const val QUERY_NEW_TOKEN =
    "insert into TOKENS (token_validation, user_id) values (:token, :uuid);"

private const val QUERY_GET_USER_BY_ID = "select * from USERS where uuid = :id;"
private const val QUERY_GET_USER_BY_EMAIL = "select * from USERS where email = :email;"
private const val QUERY_USER_WITH_EMAIL= "select count(email) from USERS where email = :email;"
private const val QUERY_USER_WITH_NAME = "select count(username) from USERS where username = :username;"

private const val QUERY_RANKING_PAGE = "select * from USERS ORDER BY elo DESC OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY"
private const val QUERY_DELETE_TOKEN = "delete from TOKENS where user_id = :uuid;"

private const val INSERT_TOKEN = "insert into TOKEN ()"

class JDBIUserRepository(private val handle : Handle) : UserRepository {

    override fun createUser(username: String, email: String, passwordValidation: PasswordValidationInfo): UserID {
        val elem = handle.createUpdate(QUERY_NEW_PLAYER)
            .bind("username", username)
            .bind("email", email)
            .bind("password_validation", passwordValidation.validationInfo)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()

        return UserID(elem)
    }

    override fun createToken(token: Token) {
        // first delete any ocurring token
        handle.createUpdate(QUERY_DELETE_TOKEN)
            .bind("uuid", token.userID.id)
            .execute()

        // insert the token
        handle.createUpdate(QUERY_NEW_TOKEN)
            .bind("token", token.tokenValidationInfo.validationInfo)
            .bind("uuid", token.userID.id)
            .execute()
    }

    override fun doesUsernameExist(username: String): Boolean {
        return handle.createQuery(QUERY_USER_WITH_NAME)
            .bind("username", username)
            .mapTo<Int>()
            .single() >= 1
    }
    override fun doesEmailExist(email: String): Boolean {
        val res = handle.createQuery(QUERY_USER_WITH_EMAIL)
            .bind("email", email)
            .mapTo<Int>()
            .single() >= 1
        return res;
    }

    override fun getUserRanking(page: Int, size : Int): List<User> {
        val list = handle.createQuery(QUERY_RANKING_PAGE)
            .bind("offset", size * (page-1))
            .bind("size", size)
            .mapTo<User>()
            .list()

        return list
    }

    override fun getUserById(id: UserID): User? {
        return handle.createQuery(QUERY_GET_USER_BY_ID)
            .bind("id", id.id)
            .mapTo<User>()
            .singleOrNull()
    }

    override fun getUserByEmail(email: String): User? {
        return handle.createQuery(QUERY_GET_USER_BY_EMAIL)
            .bind("email", email)
            .mapTo<User>()
            .singleOrNull()
    }

    override fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>? {
        return handle.createQuery(
            "select uuid, username, email, playCount, elo, password_validation, token_validation " +
                "from USERS as users " +
                "inner join TOKENS as tokens " +
                "on users.uuid = tokens.user_id " +
                "where token_validation = :token_validation"
        )
            .bind("token_validation", tokenValidationInfo.validationInfo)
            .mapTo<UserAndTokenModel>()
            .singleOrNull()
            ?.userAndToken
    }

    private data class UserAndTokenModel(
        val uuid: Int,
        val username: String,
        val email : String,
        val playCount : Int,
        val elo : Int,
        val passwordValidation: PasswordValidationInfo,
        val tokenValidation: TokenValidationInfo
    ) {
        val userAndToken: Pair<User, Token>
            get() = Pair(
                User(uuid, username, email, playCount, elo, passwordValidation),
                Token(tokenValidation, UserID(uuid))
            )
    }

}
