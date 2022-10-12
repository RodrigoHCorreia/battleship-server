package battleship.server.utils


fun isValidData(s: String?, checkForSpaces: Boolean = false) : Boolean {
    if(s.isNullOrEmpty() || s.isBlank())return false
    if(checkForSpaces && s.matches(".*\\s.*".toRegex())) return false
    return true
}

