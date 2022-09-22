package battleship.server.controllers

import battleship.server.model.User
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import serverInfo
import users

@RestController
class BasicController {

    @GetMapping("systemInfo")
    fun getVersion() = serverInfo

}

@RestController
@RequestMapping("users")
class UserController {

    @PostMapping("addUser")
    fun addUser(@RequestBody body : MultiValueMap<String, String>) {
        val newID = users.size;
        val name = body.getFirst("username");
        //TODO: encrypt, and sanitize this data
        val password = body.getFirst("password");
        if(name == null || password == null) {
            TODO()
            return;
        }

        val newUser = User(
            uuid = newID,
            username = name,
            password = password
        )
    }

    //By default gives wins ranking
    @GetMapping("ranking/{type}")
    fun getRanking(@PathVariable type: String, @RequestParam limit : Int?) : List<User> {

//        val orderingParameter = when(type.toUpperCase().firstOrNull()){
//            'P' -> "played"
//            'W' -> "won"
//            null -> "won"
//            else -> "won"
//        }

        val actualLimit = if(limit == null || limit > 100) 100 else limit
        return users.toList()
            .map {it.second }
            .sortedWith()
            .reversed()
            .take(actualLimit);
    }





}