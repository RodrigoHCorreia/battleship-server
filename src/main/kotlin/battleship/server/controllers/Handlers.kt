package battleship.server.controllers

import battleship.server.model.User
import battleship.server.services.ServerInfoService
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import users

@RestController
class BasicController {

    @GetMapping("systemInfo")
    fun getVersion() = ServerInfoService.retrieveServerInfo()

}

@RestController
@RequestMapping("users")
class UserController {

    @PostMapping("addUser")
    fun addUser(@RequestBody body: MultiValueMap<String, String>) {
        val newID = users.size
        val name = body.getFirst("username")
        //TODO: encrypt, and sanitize this data
        val password = body.getFirst("password")
        if (name == null || password == null) {
            TODO()
            return
        }

        val newUser = User(
            uuid = newID,
            username = name,
            password = password
        )
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