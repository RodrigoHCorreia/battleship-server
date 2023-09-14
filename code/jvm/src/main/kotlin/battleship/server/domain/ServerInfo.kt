package battleship.server.domain

data class Author(
    val name: String,
    val email: String,
    val id: Int
)

data class ServerInfo(
    val version: String,
    val authors: List<Author>
)