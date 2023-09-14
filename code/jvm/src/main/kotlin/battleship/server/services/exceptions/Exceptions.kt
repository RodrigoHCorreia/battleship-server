package battleship.server.services.exceptions

sealed class BattleshipException(override val message: String?) : Exception(message)

