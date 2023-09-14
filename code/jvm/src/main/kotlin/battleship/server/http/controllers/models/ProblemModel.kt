package battleship.server.http.controllers.models

import org.springframework.http.MediaType

class ProblemModel(
    val type: String,
    val title: String,
    val details: String,
    val instance: String
){
    companion object {
        val MEDIA_TYPE = MediaType.parseMediaType("application/problem+json")
    }
}

//        /**
//         * Creates a [ResponseEntity] with the given [error].
//         * The [error] is converted to JSON and the [ResponseEntity] has the [MEDIA_TYPE] as its content type.
//         * @return The [ResponseEntity] with the given [error].
//         */
//        fun response(error: AppError) = ResponseEntity
//            .status(error.type)
//            .header("Content-Type", MEDIA_TYPE)
//            .body(error.errorPayload)