package battleship.server.controllers

import battleship.server.model.User
import battleship.server.services.ServerInfoService
import battleship.server.utils.pbkdf2PasswordEncoder
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import users
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity

@RestController
class BasicController {

    @GetMapping("systemInfo")
    fun getVersion() = ServerInfoService.retrieveServerInfo()

}

@RestController
class HomeController {
    @GetMapping //localhost
    fun get() = "hey"
}

@RestController
@RequestMapping("users")
class UserController {

    @PostMapping("addUser")
    fun addUser(@RequestBody body: MultiValueMap<String, String>, response: HttpServletResponse) : ResponseEntity<UserController> {
        val newID = users.size
        val name = body.getFirst("username")
        val password = body.getFirst("password")
        if (name.isNullOrEmpty() || name.isBlank() || password.isNullOrEmpty() || name.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
        users.add(User(newID, name, pbkdf2PasswordEncoder.encode(password), 0, 0))
        return ResponseEntity.ok(null)
    }

    //By default gives wins ranking
    @GetMapping("ranking/{type}")
    fun getRanking(@PathVariable type: String, @RequestParam limit: Int?): List<User> {

        // THIS IS SERVICES
        val actualLimit = if (limit == null || limit > 100) 100 else limit
        val sorted = if(type=="won") users.sortedByDescending { it.gamesWon }
                    else users.sortedByDescending { it.gamesPlayed }
        return sorted.take(actualLimit)
    }
}