package battleship.server.controllers

import battleship.server.services.ServerInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Nota: por '/' em "@RequestMapping("ranking/")" vai exigir com que se ponha a barra no fim dopara funcionar
 * https://www.baeldung.com/spring-requestparam-vs-pathvariable
 * https://www.baeldung.com/spring-optional-path-variables
 */

@RestController
class InfoHandler {

    @GetMapping("info")
    fun getVersion() = ServerInfoService.retrieveServerInfo()

    @GetMapping //localhost
    fun get() = "Welcome"
}