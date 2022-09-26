package battleship.server.controllers

import battleship.server.model.User
import battleship.server.model.UserView
import battleship.server.utils.gson
import battleship.server.utils.isValidUserData
import battleship.server.utils.pbkdf2PasswordEncoder
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
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
        if (!isValidUserData(name)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name can't be null, empty or have white spaces")
        }
        if(!isValidUserData(password)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password can't be null, empty or have white spaces")
        }
        users.add(User(newID, name!!, pbkdf2PasswordEncoder.encode(password), 0, 0))
        return ResponseEntity.ok("User registed")
    }

    @GetMapping("{uuid}")
    fun getUser(@PathVariable uuid: Int) : ResponseEntity<Any>{
        val userView: UserView? = users.find { it.uuid==uuid }.let { it?.getUserView() }
        return if(userView==null) ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist")
        else ResponseEntity<Any>(userView, HttpStatus.OK)
    }

    //By default gives wins ranking
    @GetMapping("ranking", "/ranking/{scheme}")
    fun getRanking(@RequestParam limit: Int?, @PathVariable scheme: String?) : ResponseEntity<Any>/*: List<User>*/ {

        // THIS IS SERVICES
        val actualLimit = if (limit == null || limit > 100) 100 else limit
        var sorted: List<UserView>? = null
        if(!scheme.isNullOrEmpty()) {
            if(scheme=="played") sorted = users.take(actualLimit).sortedByDescending { it.gamesPlayed }.map { it.getUserView()}
        } else sorted = users.take(actualLimit).sortedByDescending { it.gamesWon }.map { it.getUserView()}
        return if(sorted!=null) ResponseEntity(sorted.get(0), HttpStatus.OK)
               else ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error")
    }
}