package battleship.server.http.controllers

import battleship.server.domain.Token
import battleship.server.domain.UserID
import battleship.server.http.URIs
import battleship.server.http.controllers.models.*
import battleship.server.infra.SirenModel
import battleship.server.services.*
import battleship.server.services.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URL
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(URIs.Users.ROOT)
class UserController(private val service: UserService) {
    @GetMapping(URIs.Users.BY_ID)
    fun getUser(@PathVariable id : Int, request: HttpServletRequest) : SirenModel<UserEntity> {
        val res = service.getUserByID(UserID(id))
        val output = UserEntity(res.id, username = res.username, playCount = res.playCount, elo = res.elo);
        return SirenModels.user(output, URI(request.requestURL.toString()));
    }
}