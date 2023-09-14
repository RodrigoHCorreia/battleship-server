package battleship.server.http.controllers.models

import battleship.server.domain.game.board.PlaceResult
import battleship.server.domain.game.board.ShotResult
import battleship.server.services.GameState

data class AuthorModel(
    val name: String,
    val email: String,
    val id: Int
)

data class HomeEntity(
    val version: String,
    val authors: List<AuthorModel>
)

data class TokenEntity(val token : String)

data class RankingEntity(val page : Int, val size : Int)

// TODO: This is a placeholder lol
data class GameHubEntity(
    val placeholder : Int
)

data class GameEntity(
    val gameID: Int,
    val ranked : Boolean,
    val opponent : Int?,
    val state : GameState,
    val canShoot : Boolean
)


data class UserEntity(
    val uuid : Int,
    val username : String,
    val playCount : Int,
    val elo : Int
)

data class BoardEntity(
    val grid : List<String>,
)

data class PlaceResultEntity(
    val result : PlaceResult
)

data class ShotResultEntity(
    val result : ShotResult,
    val ship : String?
)
