package battleship.server

import battleship.server.domain.Author
import battleship.server.domain.ServerInfo
import battleship.server.repositories.jdbi.customConfigure
import battleship.server.utils.Clock
import battleship.server.utils.encoding.Sha256TokenEncoder
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.time.Instant
import javax.sql.DataSource


private val JDBI_CONNECTION_URL = System.getenv("JDBI_URL") ?: "lol" //?: throw RuntimeException("Requires JDBI_URL!")
private val JDBI_USERNAME = System.getenv("JDBI_USERNAME") ?: "lol" //?: throw RuntimeException("Requires JDBI_USERNAME!")
private val JDBI_PASSWORD = System.getenv("JDBI_PASSWORD") ?: "lol" //?: throw RuntimeException("Requires JDBI_PASSWORD!")

@Component
class ServerPortCustomizer : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    override fun customize(factory: ConfigurableWebServerFactory?) {
        factory?.setPort(System.getenv("PORT")?.toIntOrNull() ?: 8080);
    }
}


/**
 * The main entry point of the battleship server application.
 */
@SpringBootApplication
class BattleshipServerApplication {

    /**
     * Creates a [Jdbi] instance with the connection to the database.
     * @return the [Jdbi] instance
     */
    @Bean
    fun jdbi(): Jdbi = Jdbi.create(JDBI_CONNECTION_URL, JDBI_USERNAME, JDBI_PASSWORD)
            .customConfigure()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun tokenEncoder() = Sha256TokenEncoder()

    @Bean
    fun clock() : Clock = object : Clock {
        override fun now() = Instant.now()
    }

    @Bean
    fun serverInfo() = ServerInfo(
        version = "0.0.1",
        authors = listOf(
            Author("Adolfo Morgado", "a48281@alunos.isel.pt", 48281),
            Author("Rodrigo Correia", "a48335@alunos.isel.pt", 48335)
        )
    )
}

fun main(args: Array<String>) {
    runApplication<BattleshipServerApplication>(*args)

}
