package battleship.server.http.controllers

import battleship.server.domain.UserID
import battleship.server.domain.game.board.Position
import battleship.server.http.URIs
import battleship.server.http.controllers.models.*
import battleship.server.infra.SirenModel
import battleship.server.services.*
import battleship.server.services.exceptions.*
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping(URIs.Games.ROOT)
class GameController(val services: GameService) {

    /**
     * returns a list of active games the user is participating in
     */
    @GetMapping(URIs.Games.HUB)
    fun getGameHub(userID : UserID, request : HttpServletRequest) : SirenModel<GameHubEntity> {
        val res = services.getActiveGames(userID)
        val output = GameHubEntity(5)
        return SirenModels.gameHub(output, res, URI(request.requestURL.toString()))
    }

    @GetMapping(URIs.Games.BY_ID)
    fun getGame(@PathVariable id : Int, userID : UserID, request: HttpServletRequest) : SirenModel<GameEntity> {
        val res = services.getGame(userID, id)
        val output = GameEntity(res.id, res.ranked, res.opponent?.id, res.state, res.canShoot)
        return SirenModels.game(output, URI(request.requestURL.toString()))

    }

    @PostMapping(URIs.Games.MATCH)
    fun match(userID : UserID, request: HttpServletRequest): SirenModel<GameEntity> {
        val res = services.doMatch(userID)
        val output = GameEntity(res.id, res.ranked, res.opponent?.id, res.state, res.canShoot);
        return SirenModels.game(output, URI(request.requestURL.toString()))
    }

    @PostMapping(URIs.Games.PLACE)
    fun place(
        @PathVariable id : Int,
        @RequestBody body : PlaceInputModel,
        userID: UserID,
        request: HttpServletRequest
    ) : SirenModel<PlaceResultEntity> {

        val proposal = BoardProposal(body.ships.map {
            val direction = when(it.direction) {
                DirectionModel.HORIZONTAL -> Direction.RIGHT
                DirectionModel.VERTICAL -> Direction.DOWN
            }
            ShipProposal(it.type, Position(it.position.x, it.position.y), direction)
        })
        val res = services.place(
            gameID = id,
            userID = userID,
            proposal = proposal
        )

        val output = PlaceResultEntity(res)
        return SirenModels.placeResult(output, URI(request.requestURL.toString()))
    }

    @PostMapping(URIs.Games.SHOOT)
    fun shoot(
        @PathVariable id : Int,
        @RequestBody body : PositionModel,
        userID: UserID,
        request: HttpServletRequest
    ) : SirenModel<ShotResultEntity>
    {
        val res = services.shoot(
            gameID = id,
            userID = userID,
            Position(body.x, body.y)
        )
        val output = ShotResultEntity(res.first, res.second)
        return SirenModels.shotResult(output, URI(request.requestURL.toString()));
    }

    @GetMapping(URIs.Games.BOARD)
    fun getBoard(
        @PathVariable id : Int,
        userID: UserID,
        request : HttpServletRequest
    ) : SirenModel<BoardEntity>
    {
        val res = services.getBoard(id, userID, false)
        val output = BoardEntity(res.grid)
        return SirenModels.board(output, URI(request.requestURL.toString()))
    }

    @GetMapping(URIs.Games.ENEMY_BOARD)
    fun getEnemyBoard(
        @PathVariable id : Int,
        userID: UserID,
        request : HttpServletRequest
    ) : SirenModel<BoardEntity>
    {
        val res = services.getBoard(id, userID, true)
        val output = BoardEntity(res.grid)
        return SirenModels.board(output, URI(request.requestURL.toString()))
    }

}
