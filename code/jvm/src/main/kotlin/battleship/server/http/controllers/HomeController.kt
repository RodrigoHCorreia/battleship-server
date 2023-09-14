package battleship.server.http.controllers

import battleship.server.http.URIs
import battleship.server.http.controllers.models.*
import battleship.server.infra.SirenModel
import battleship.server.services.*
import battleship.server.services.exceptions.*
import org.springframework.http.RequestEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.net.URL
import javax.servlet.http.HttpServletRequest

@RestController
class HomeController(val service: HomeService) {

    @GetMapping(URIs.HOME)
    fun getHome(request: HttpServletRequest) : SirenModel<HomeEntity>
    {
        val authors = service.serverInfo.authors.map { AuthorModel(it.name, it.email, it.id) }
        val output = HomeEntity(service.serverInfo.version, authors);

        return SirenModels.home(output, URI(request.requestURL.toString()));
    }

    @PostMapping(URIs.LOGIN)
    fun login(@RequestBody body : LoginInputModel, request: HttpServletRequest): SirenModel<TokenEntity> {
        val res = service.login(body.email, body.password)
        val output = TokenEntity(res.first)
        return SirenModels.token(output, res.second, URI(request.requestURL.toString()))
    }

    @PostMapping(URIs.REGISTER)
    fun register(@RequestBody body : RegisterInputModel, request: HttpServletRequest): SirenModel<TokenEntity> {
        val res = service.createUser(body.username, body.email, body.password)
        val output = TokenEntity(res.first)
        return SirenModels.token(output, res.second, URI(request.requestURL.toString()))
    }

    @GetMapping(URIs.RANKING, URIs.RANKING_BY_PAGE)
    fun getRanking(@PathVariable(required = false) page : Int?, request: HttpServletRequest) : SirenModel<RankingEntity> {
        val targetPage = page ?: 1
        val res = service.getRankingList(targetPage);
        val output = RankingEntity(targetPage, res.size);

        return SirenModels.ranking(output, URI(request.requestURL.toString()), res)
    }
}

