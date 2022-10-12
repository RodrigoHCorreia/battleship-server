package battleship.server.controllers

import battleship.server.services.ServerInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InfoHandler {

    @GetMapping("server-info")
    fun getVersion() = ServerInfoService.retrieveServerInfo()

    @GetMapping //localhost
    fun get() = "Welcome"
}