package battleship.server.domain

data class User(
    val uuid: Int,
    val username: String,
    val email: String,
    val playCount: Int,
    val elo: Int,
    val password_validation: PasswordValidationInfo,
)
