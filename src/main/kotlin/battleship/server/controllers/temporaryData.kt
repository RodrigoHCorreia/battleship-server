import battleship.server.model.Author
import battleship.server.model.ServerInfo
import battleship.server.model.User

val serverInfo = ServerInfo (
    version = "0.0.1",
    authors = listOf(
        Author("Adolfo Morgado", "a48281@alunos.isel.pt", 48281),
        Author("Rodrigo Correia", "a48335@alunos.isel.pt", 48335)
    )
)

val users = hashMapOf<Int, User>(
    Pair(0, User(0, "Cyb3r", 2, 1,  "1234")),
    Pair(1, User(1,"ligma", 1, 0, "4321")),
)

