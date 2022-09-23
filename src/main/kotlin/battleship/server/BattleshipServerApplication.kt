package battleship.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class BattleshipServerApplication

fun main(args: Array<String>) {
	runApplication<BattleshipServerApplication>(*args)
}
