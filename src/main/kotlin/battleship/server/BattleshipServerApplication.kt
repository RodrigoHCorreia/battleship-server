package battleship.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class], /*desliga pagina de login estranha*/
	                   scanBasePackages = ["battleship.server.controllers"]
)
class BattleshipServerApplication

fun main(args: Array<String>) {
	runApplication<BattleshipServerApplication>(*args)
}
