package battleship.server.http.controllers.models

import battleship.server.http.Rels
import battleship.server.http.URIs
import battleship.server.infra.ActionBuilderScope
import battleship.server.infra.LinkRelation
import battleship.server.infra.siren
import org.springframework.http.HttpMethod
import java.net.URI
import java.net.URL
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.typeOf

fun <T> ActionBuilderScope.toField(prop : KProperty<T>) {
    when(prop.returnType) {
        typeOf<Int>() -> numberField(prop.name)
        typeOf<String>() -> textField(prop.name)
        else -> textField(prop.name)
    }
}

object SirenModels {

    fun home(output : HomeEntity, uri : URI) = siren(output) {
        clazz("Home")

        action(
            name = "login",
            href = URI(uri.host + URIs.login()),
            method = HttpMethod.POST,
            type = "application/json"
        ) {
            LoginInputModel::class.declaredMemberProperties.forEach {
                toField(it)
            }
        }

        action(
            name = "register",
            href = URI(uri.host + URIs.register()),
            method = HttpMethod.POST,
            type = "application/json"
        ) {
            RegisterInputModel::class.declaredMemberProperties.forEach {
                toField(it)
            }
        }

        link(rel = Rels.self, href = uri);
        link(rel = Rels.ranking, href = URI(uri.host + URIs.ranking(0)))
        link(rel = Rels.gameHub, href = URI(uri.host + URIs.Games.hub()))
    }

    fun token(output : TokenEntity, uuid : Int, uri : URI) = siren(output) {
        clazz("Token")

        link(rel = Rels.self, href = uri);
        link(rel = Rels.user, href = URI(uri.host + URIs.Users.by_id(uuid)))
    }

    fun ranking(output : RankingEntity, uri : URI, users : List<UserEntity>) = siren(output) {
        clazz("Ranking")

        users.forEach {
            entity(it, Rels.user) {
                clazz("User")
                link(rel = Rels.self, href = URI(uri.host + URIs.Users.by_id(it.uuid)))
            }
        }

        val page = output.page
        link(rel = Rels.self, href = URI(uri.host + URIs.ranking(page)));
        link(rel =  Rels.next, href = URI(uri.host + URIs.ranking(page + 1)));
        if(page > 1)
            link(rel = Rels.previous, href = URI(uri.host + URIs.ranking(page - 1)));
    }

    fun user(output : UserEntity, uri : URI) = siren(output) {
        clazz("User")

        link(rel = Rels.self, href = uri);
    }

    fun gameHub(output : GameHubEntity, activeIDs: List<Int>, uri : URI) = siren(output) {
        clazz("GameHub")

        activeIDs.forEach { id ->
            linkEntity("Game", Rels.game, URI(uri.host + URIs.Games.by_id(id)))
        }

        action(
            name = "match",
            href = URI(uri.host + URIs.Games.match()),
            method = HttpMethod.POST,
            type = "none"
        ) {
            /* Nothing */
        }

        link(rel = Rels.self, href = uri)
    }

    fun game(output : GameEntity, uri : URI) = siren(output) {
        clazz("Game")

        output.opponent?.let {
            linkEntity("User", Rels.opponent, URI(uri.host + URIs.Users.by_id(it)))
        }

        linkEntity("Board", Rels.playerBoard, URI(uri.host + URIs.Games.board(output.gameID)))
        linkEntity("Board", Rels.enemyBoard, URI(uri.host + URIs.Games.enemyBoard(output.gameID)))

        action(
            name = "place",
            href = URI(uri.host + URIs.Games.place(output.gameID)),
            method = HttpMethod.POST,
            type = "application/json"
        ) {
            // NOTHING
        }

        action(
            name = "shoot",
            href = URI(uri.host + URIs.Games.shoot(output.gameID)),
            method = HttpMethod.POST,
            type = "application/json"
        ) {
            // NOTHING
        }

        link(rel = Rels.self, href = uri)
    }

    fun board(output : BoardEntity, uri : URI) = siren(output) {
        clazz("Board")

        link(rel = Rels.self, href = uri)
    }

    fun placeResult(output : PlaceResultEntity, uri : URI) = siren(output) {
        clazz("Place Result")

        link(rel = Rels.self, href = uri)
    }

    fun shotResult(output : ShotResultEntity, uri : URI) = siren(output) {
        clazz("Shot Result")

        link(rel = Rels.self, href = uri)
    }
}
