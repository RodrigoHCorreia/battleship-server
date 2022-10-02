package battleship.server.controllers

import battleship.server.model.User
import battleship.server.model.Player
import battleship.server.utils.hashPassword
import battleship.server.utils.isValidData
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping


@RestController
@RequestMapping("users")
class UserHandler {

    @RequestMapping
    fun get(): String {
        return "Users page. You can login and register here"
    }

    @PostMapping("newuser")
    fun addUser(@RequestBody body: MultiValueMap<String, String>) : ResponseEntity<String> {
        val newID = nextUserID
        val name = body.getFirst("username")
        val password = body.getFirst("password")
        //val uuid: UUID = UUID.randomUUID()
        //todo ver se o nome ja exist
        if (!isValidData(name, true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name can't be null, empty or have white spaces")
        }
        if(!isValidData(password)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password can't be null, empty or have white spaces")
        }
        users.add(User(newID, Player(name!!), hashPassword(password!!)))
        return ResponseEntity.ok("User registed")
    }

    @GetMapping("{uuid}")
    fun getUser(@PathVariable uuid: Int) : ResponseEntity<Any>{
        val userView: Player? = users.find { it.uuid==uuid }.let { it?.player }
        return if(userView==null) ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist")
        else ResponseEntity<Any>(userView, HttpStatus.OK)
    }

    //By default gives wins ranking
    @GetMapping("ranking", "/ranking/{scheme}")
    fun getRanking(@RequestParam limit: Int?, @PathVariable scheme: String?) : ResponseEntity<Any> {
        // THIS IS SERVICES
        val actualLimit = if (limit == null || limit > 100) 100 else limit
        var sorted: List<Player> = if(scheme=="played") users.map { it.player }.sortedByDescending { it.gamesPlayed }.take(actualLimit)
                                   else users.map { it.player }.sortedByDescending { it.gamesWon }.take(actualLimit)
        return ResponseEntity(sorted, HttpStatus.OK)
    }
}