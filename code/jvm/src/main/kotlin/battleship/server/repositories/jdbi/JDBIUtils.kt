package battleship.server.repositories.jdbi

import battleship.server.domain.game.EPlayer
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin

/**
 * @return returns the [this] instance for syntax-sugar
 */
fun Jdbi.customConfigure(): Jdbi {
    // Plugins
    installPlugin(KotlinPlugin())
    installPlugin(PostgresPlugin())
    installPlugin(SqlObjectPlugin())
    installPlugin(KotlinSqlObjectPlugin())

    // Mappers
    registerColumnMapper(BoardMapper())
    registerColumnMapper(RulesMapper())
    registerColumnMapper(UserIDMapper())
    registerColumnMapper(TokenValidationInfoMapper())
    registerColumnMapper(PasswordValidationInfoMapper())


    registerArrayType(GameStateModel::class.java, "game_state")
    registerArrayType(EPlayer::class.java, "eplayer")

    return this
}
