package battleship.server.http.pipeline

import battleship.server.http.pipeline.AuthenticationInterceptor.Companion.NAME_AUTHORIZATION_HEADER
import battleship.server.http.pipeline.AuthenticationInterceptor.Companion.NAME_WWW_AUTHENTICATE_HEADER
import battleship.server.http.pipeline.AuthenticationInterceptor.Companion.logger
import battleship.server.domain.UserID
import battleship.server.services.exceptions.ConflictException
import battleship.server.services.exceptions.UnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Intercepts requests and checks if the user is authenticated.
 *
 * @param authorizationHeaderProcessor The processor that handles the authorization header.
 *
 * @property logger The (logger) of the class.
 * @property NAME_AUTHORIZATION_HEADER The name of the header that contains the authentication token.
 * @property NAME_WWW_AUTHENTICATE_HEADER The name that the authorization header has.
 */
@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: AuthorizationHeaderProcessor,
) : HandlerInterceptor {

    /**
     * Intercepts the request before handling the request and checks if the user is authenticated.
     * If the user is not authenticated, the request is rejected with a 401 status code.
     * @param request The request.
     * @param response The response.
     * @param handler The handler.
     * @return true if the user is authenticated, false otherwise.
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.methodParameters.any { it.parameterType == UserID::class.java }) {
            val authorizationHeader = request.getHeader(NAME_AUTHORIZATION_HEADER);
            val userID = authorizationHeaderProcessor.process(authorizationHeader)
                ?: throw UnauthorizedException("No valid Authorization token was passed!")

            //TODO: Introduce authorization through cookies later on here
            UserIDArgumentResolver.addUserIDTo(userID, request)
            return true
        }

        return true
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthenticationInterceptor::class.java)
        private const val NAME_AUTHORIZATION_HEADER = "Authorization"
        private const val NAME_WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"
    }
}