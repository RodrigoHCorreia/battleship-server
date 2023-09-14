package battleship.server.http.controllers

import battleship.server.http.controllers.models.ProblemModel
import battleship.server.http.getResponseEntity
import battleship.server.services.exceptions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [BattleshipException::class])
    fun battleshipExceptionHandler(ex : BattleshipException, request : HttpServletRequest)
        = getResponseEntity(ex, request.requestURI)

    @ExceptionHandler(value = [NotImplementedError::class])
    fun notImplementedExceptionHandler(ex : NotImplementedError, request : HttpServletRequest)
    {
        ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .contentType(ProblemModel.MEDIA_TYPE)
            .body(
                ProblemModel(
                    type = "https://example.org/problems/internal-server-error",
                    title = "Operation not implemented",
                    details = ex.message ?: "",
                    instance = request.requestURI
                )
            )
    }

    /*
    @ExceptionHandler(value = [NotImplementedError::class])
    fun notImplementedExceptionHandler(ex : NotImplementedError, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(ProblemModel.MEDIA_TYPE)
        .body(
            ProblemModel(
                type = "https://example.org/problems/internal-server-error",
                title = "Internal server error",
                details = ex.message ?: "",
                instance = request.requestURI
            )
        )
    @ExceptionHandler(value = [NotFoundException::class])
    fun notFoundExceptionHandler(ex: NotFoundException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .contentType(ProblemModel.MEDIA_TYPE)
        .body(
            ProblemModel(
                type = "https://example.org/problems/not-found",
                title = "UriNotFound",
                details = ex.message ?: "",
                instance = request.requestURI
            )
        )

    @ExceptionHandler(value = [BadRequestException::class])
    fun badRequestExceptionHandler(ex: BadRequestException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .contentType(ProblemModel.MEDIA_TYPE)
        .body(
            ProblemModel(
                type = "https://example.org/problems/bad-request",
                title = "BadRequest",
                details = ex.message ?: "",
                instance = request.requestURI
            )
        )

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun notAuthorizedExceptionHandler(ex: UnauthorizedException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .contentType(ProblemModel.MEDIA_TYPE)
        .body(
            ProblemModel(
                type = "https://example.org/problems/unauthorized",
                title = "Not authorized",
                details = ex.message ?: "",
                instance = request.requestURI
            )
        )

    @ExceptionHandler(value = [ForbiddenException::class])
    fun forbiddenExceptionHandler(ex: ForbiddenException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .contentType(ProblemModel.MEDIA_TYPE)
        .body(
            ProblemModel(
                type = "https://example.org/problems/forbidden",
                title = "Forbidden",
                details = ex.message ?: "",
                instance = request.requestURI
            )
        )

    @ExceptionHandler(value = [ConflictException::class])
    fun conflictExceptionHandler(ex: ConflictException, request: HttpServletRequest) = ResponseEntity
        .status(HttpStatus.CONFLICT)
        .contentType(ProblemModel.MEDIA_TYPE)
        .body(
            ProblemModel(
                type = "https://example.org/problems/conflict",
                title = "Conflict",
                details = ex.message ?: "",
                instance = request.requestURI
            )
        )

    @ExceptionHandler(value = [InternalErrorException::class])
    fun internalServerErrorExceptionHandler(ex: InternalErrorException, request: HttpServletRequest) : ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(ProblemModel.MEDIA_TYPE)
            .body(
                ProblemModel(
                    type = "https://example.org/problems/internal-server-error",
                    title = "Internal server error",
                    details = ex.message ?: "",
                    instance = request.requestURI
                )
            )
    }
    */
}
