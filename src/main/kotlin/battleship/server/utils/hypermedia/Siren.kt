@file:Suppress("unused")

package battleship.server.utils.hypermedia

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import java.net.URI
import org.springframework.http.*
import com.fasterxml.jackson.databind.type.TypeFactory

/**
 * For details regarding the Siren media type, see <a href="https://github.com/kevinswiber/siren">Siren</a>
 */

private const val APPLICATION_TYPE = "application"
private const val SIREN_SUBTYPE = "vnd.siren+json"


const val SirenMediaType = "$APPLICATION_TYPE/$SIREN_SUBTYPE"

/**
 * Gets a Siren self link for the given URI
 *
 * @param uri   the string with the self URI
 * @return the resulting siren link
 */
fun selfLink(uri: String) = SirenLink(rel = listOf("self"), href = URI(uri))

/**
 * Class whose instances represent links as they are represented in Siren.
 */
data class SirenLink(
    val rel: List<String>,
    val href: URI,
    val title: String? = null,
    val type: MediaType? = null)

/**
 * Class whose instances represent actions that are included in a siren entity.
 */
data class SirenAction(
    val name: String,
    val href: URI,
    val title: String? = null,
    @JsonProperty("class")
    val clazz: List<String>? = null,
    val method: HttpMethod? = null,
    val type: MediaType? = null,
    val fields: List<Field>? = null
) {
    /**
     * Represents action's fields
     */
    data class Field(
        val name: String,
        val type: String? = null,
        val value: String? = null,
        val title: String? = null
    )
}

data class SirenEntity<T>(
    @JsonProperty("class")
    val clazz: List<String>? = null,
    val properties: T? =null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: List<SirenAction>? = null,
    val title: String? = null
) {
    companion object {
        inline fun <reified T> getType(): TypeReference<SirenEntity<T>> =
            object : TypeReference<SirenEntity<T>>() { }
    }
}


/**
 * Base class for admissible sub entities, namely, [EmbeddedLink] and [EmbeddedEntity].
 * Notice that this is a closed class hierarchy.
 */
sealed class SubEntity

data class EmbeddedLink(
    @JsonProperty("class")
    val clazz: List<String>? = null,
    val rel: List<String>,
    val href: URI,
    val type: MediaType? = null,
    val title: String? = null
) : SubEntity()

data class EmbeddedEntity<T>(
    val rel: List<String>,
    @JsonProperty("class")
    val clazz: List<String>? = null,
    val properties: T? =null,
    val entities: List<SubEntity>? = null,
    val links: List<SirenLink>? = null,
    val actions: SirenAction? = null,
    val title: String? = null
) : SubEntity() {
    companion object {
        inline fun <reified T> getType(): TypeReference<EmbeddedEntity<T>> =
            object : TypeReference<EmbeddedEntity<T>>() { }
    }
}
