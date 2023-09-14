package battleship.server.http.pipeline.config

import battleship.server.http.URIs
import battleship.server.http.pipeline.AuthenticationInterceptor
import battleship.server.http.pipeline.UserIDArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Configures the pipeline.
 * Adds the [AuthenticationInterceptor] and the [UserIDArgumentResolver].
 * @param authenticationInterceptor the authentication interceptor
 * @param userArgumentResolver the user argument resolver
 *
 * @property addInterceptors Adds the [AuthenticationInterceptor] to the interceptors list.
 * @property addArgumentResolvers Adds the [UserIDArgumentResolver] to the resolvers list.
 */
@Configuration
class PipelineConfigurer(
    val authenticationInterceptor: AuthenticationInterceptor,
    val userArgumentResolver: UserIDArgumentResolver,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("${URIs.Games.ROOT}/**")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
    }
}