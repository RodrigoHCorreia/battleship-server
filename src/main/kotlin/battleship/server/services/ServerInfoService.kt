package battleship.server.services

import battleship.server.model.Author
import battleship.server.model.ServerInfo

val serverInfo = ServerInfo (
    version = "0.0.1",
    authors = listOf(
        Author(48281, "Adolfo Morgado", "a48281@alunos.isel.pt"),
        Author(48335, "Rodrigo Correia", "a48335@alunos.isel.pt")
    )
)

class ServerInfoService {

    companion object {
        fun retrieveServerInfo() = serverInfo
    }

}
