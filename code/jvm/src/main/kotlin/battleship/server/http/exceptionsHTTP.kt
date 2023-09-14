package battleship.server.http

import battleship.server.http.controllers.models.ProblemModel
import battleship.server.services.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun getCode(ex : BattleshipException) : HttpStatus
    = when(ex) {
        is BadRequestException -> HttpStatus.BAD_REQUEST
        is ConflictException -> HttpStatus.CONFLICT
        is ForbiddenException -> HttpStatus.FORBIDDEN
        is InternalErrorException -> HttpStatus.INTERNAL_SERVER_ERROR
        is NotFoundException -> HttpStatus.NOT_FOUND
        is UnauthorizedException -> HttpStatus.UNAUTHORIZED
        is NotImplementedException -> HttpStatus.NOT_IMPLEMENTED
        is BadGatewayException -> HttpStatus.BAD_GATEWAY
    }

fun getTitle(ex : BattleshipException) : String
    = when(ex) {
        is BadRequestException -> "Bad Request"
        is ConflictException -> "Conflict"
        is ForbiddenException -> "Forbidden"
        is InternalErrorException -> "Internal Server Error"
        is NotFoundException -> "Not Found"
        is UnauthorizedException -> "Unauthorized"
        is NotImplementedException -> "Not Implemented"
        is BadGatewayException -> "Bad Gateway"
}

fun getResponseEntity(ex : BattleshipException, uri : String)
    = ResponseEntity
        .status(getCode(ex))
        .contentType(ProblemModel.MEDIA_TYPE)
        .body(
            ProblemModel(
                type = "TODO", //TODO
                title = getTitle(ex),
                details = ex.message ?: "",
                instance = uri
            )
        )
