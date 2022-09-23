package battleship.server.services

import battleship.server.model.Author
import battleship.server.model.ServerInfo

val serverInfo = ServerInfo (
    version = "0.0.1",
    authors = listOf(
        Author("Adolfo Morgado", "a48281@alunos.isel.pt", 48281),
        Author("Rodrigo Correia", "a48335@alunos.isel.pt", 48335),
        Author("Paulo Rosa", "a44873@alunos.isel.pt", 44873)
    )
)

class ServerInfoService {

    companion object {
        fun retrieveServerInfo() = serverInfo;
    }

}
