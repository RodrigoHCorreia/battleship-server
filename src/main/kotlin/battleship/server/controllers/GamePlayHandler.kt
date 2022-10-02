package battleship.server.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("play")
class GamePlayHandler {
    @PostMapping("setshots")
    fun setShots(){

    }

    @GetMapping("myfleet")
    fun myFleet(){

    }

    @GetMapping("enemyfleet")
    fun enemyFleet(){

    }

    @GetMapping("game")
    fun game(){

    }
}