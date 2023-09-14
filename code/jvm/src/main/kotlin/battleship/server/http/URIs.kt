package battleship.server.http

import org.springframework.web.util.UriTemplate
import java.net.URI

object URIs {

    const val HOME = "/"
    const val LOGIN = "/login"
    const val REGISTER = "/register"
    const val RANKING = "/ranking"
    const val RANKING_BY_PAGE = "/ranking/{page}"

    fun home() = URI(HOME)
    fun login() = URI(LOGIN)
    fun register() = URI(REGISTER)
    fun ranking(page : Int) = UriTemplate(RANKING_BY_PAGE).expand(page)

    object Users {
        const val ROOT = "/user"
        const val BY_ID = "/{id}"
        const val PICTURE = "$BY_ID/picture"

        fun by_id(id : Int) = UriTemplate("$ROOT$BY_ID").expand(id)
        fun profile_picture(id : Int) = UriTemplate(PICTURE).expand(id)
    }

    object Games {
        //
        const val ROOT = "/games"
        const val HUB = "/"

        const val MATCH = "/match"
        const val BY_ID = "/{id}"

        const val PLACE = "$BY_ID/place"
        const val SHOOT = "$BY_ID/shoot"

        const val BOARD = "$BY_ID/board"
        const val ENEMY_BOARD = "$BY_ID/enemy_board"


        fun hub() = URI("$ROOT$HUB");
        fun match() = URI("$ROOT$MATCH")
        fun by_id(id : Int) = UriTemplate("$ROOT$BY_ID").expand(id)
        fun place(id : Int) = UriTemplate("$ROOT$PLACE").expand(id)
        fun shoot(id : Int) = UriTemplate("$ROOT$SHOOT").expand(id)
        fun board(id : Int) = UriTemplate("$ROOT$BOARD").expand(id)
        fun enemyBoard(id : Int) = UriTemplate("$ROOT$ENEMY_BOARD").expand(id)
    }
};

