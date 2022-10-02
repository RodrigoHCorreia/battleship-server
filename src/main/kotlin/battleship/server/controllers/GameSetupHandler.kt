package battleship.server.controllers

import battleship.server.model.Game
import battleship.server.utils.NewGame
import battleship.server.utils.gson
import org.springframework.util.MultiValueMap
import javax.validation.Valid
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("setup")
class GameSetupHandler {
    @PostMapping("newgame")
    fun newGame(@Valid @RequestBody game: NewGame) : ResponseEntity<Any> {
        games.forEach {
            if(it.creatorName==game.creatorName)
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("You can only host 1 lobby at a time")
            if(it.lobbyName==game.lobbyName)
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("A lobby already has that name")
        }
        games.add(Game(game.lobbyName, game.creatorName, null, null, mutableListOf()))
        return ResponseEntity<Any>("body is valid", HttpStatus.OK)
    }

    @GetMapping("opengames")
    fun getOpenGames(): ResponseEntity<Any> {
        val x = games.filter { it.invitedUserName==null }.map { gson.toJson(it) }
        return ResponseEntity<Any>("{\"games\": $x}", HttpStatus.OK)
    }


    @PostMapping("joingame")
    fun joinGame(@RequestBody body: MultiValueMap<String, String>){

    }

    @PostMapping("gridSetup")
    fun gridSetup(@RequestBody body: MultiValueMap<String, String>){

    }
}