package battleship.server.http.controllers.models

/**
 * General Models shared around the input and output models
 * Simple building blocks
 */

// General
data class PositionModel (
    val x : Int,
    val y : Int
)

enum class DirectionModel { HORIZONTAL, VERTICAL }

data class ShipRepresentationModel(
    val type: String,
    val position: PositionModel,
    val direction: DirectionModel
);

