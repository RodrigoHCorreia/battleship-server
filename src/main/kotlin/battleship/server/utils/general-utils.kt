package battleship.server.utils

import com.google.gson.GsonBuilder

fun isValidData(s: String?, checkForSpaces: Boolean = false) : Boolean {
    if(s.isNullOrEmpty() || s.isBlank())return false
    if(checkForSpaces && s.matches(".*\\s.*".toRegex())) return false
    return true
}

val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() //https://www.baeldung.com/gson-exclude-fields-serialization#expose-annotation