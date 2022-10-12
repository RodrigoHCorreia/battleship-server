
class ShipType(
    val name : String,
    val size : Int,
    val quantity : Int
) {
    init {
        require(name.isNotBlank());
        require(size > 0);
        require(quantity > 0);
    }
}