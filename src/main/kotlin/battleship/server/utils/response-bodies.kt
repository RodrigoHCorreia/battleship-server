package battleship.server.utils

data class AppError(
    val code: Int,
    val name: String,
    val message: String
)