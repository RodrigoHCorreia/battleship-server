package battleship.server.http.pipeline

import battleship.server.http.pipeline.UserIDArgumentResolver.Companion.KEY
import battleship.server.domain.UserID
import battleship.server.services.exceptions.*
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest


/**
 * Resolves the [UserID] argument in controller methods.
 * The [UserID] is added to the request by the [AuthenticationInterceptor].
 * If the [UserID] is not present, the request is rejected with a 401 status code.
 *
 * @property KEY the key used to store the [UserID] in the request
 */
@Component
class UserIDArgumentResolver : HandlerMethodArgumentResolver {

    /**
     * Checks if the method parameter is of type [UserID].
     *
     * @param parameter the method parameter
     * @return true if the method parameter is of type [UserID], false otherwise
     */
    override fun supportsParameter(parameter: MethodParameter) = parameter.parameterType == UserID::class.java

    /**
     * Resolves the [UserID] argument.
     *
     * @param parameter the method parameter
     * @param mavContainer the model and view container
     * @param webRequest the web request
     * @param binderFactory the data binder factory
     *
     * @return the user or exception if not found
     */
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw InternalErrorException("TODO PARTE 1")
        return getUserIDFrom(request) ?: throw IllegalStateException("TODO PARTE 2")
    }

    companion object {
        private const val KEY = "UserIDArgumentResolver"

        /**
         * Adds the [UserID] to the request.
         */
        fun addUserIDTo(userID: UserID, request: HttpServletRequest) {
            return request.setAttribute(KEY, userID)
        }

        /**
         * Gets the [UserID] from the request.
         * @return the user or null if not found
         */
        fun getUserIDFrom(request: HttpServletRequest): UserID? {
            return request.getAttribute(KEY)?.let {
                it as? UserID
            }
        }
    }
}