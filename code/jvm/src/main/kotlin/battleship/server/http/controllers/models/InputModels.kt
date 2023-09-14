package battleship.server.http.controllers.models

data class RegisterInputModel(
    val username : String,
    val email : String,
    val password : String
)

data class LoginInputModel(
    val email : String,
    val password : String
)


data class PlaceInputModel(
    val ships : List<ShipRepresentationModel>
)

data class ShotInputModel(
    val position : PositionModel
)
