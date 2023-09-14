package battleship.server.utils

import javax.servlet.http.HttpServletRequest

fun printRequestInfo(request: HttpServletRequest) {
    val sb = StringBuilder("Remote address -> ${request.remoteAddr}\n")
    sb.append("Local address -> ${request.localAddr}\n")
    sb.append("Remote user -> ${request.remoteUser}\n")
    sb.append("Locale user -> ${request.locale}\n")
    sb.append("Device memory -> ${request.getHeader("Device-Memory")}\n")

    request.headerNames.toList().forEach {
        sb.append("$it -> ${request.getHeader(it)}\n")
    }
    sb.deleteCharAt(sb.length - 1)
    println(sb)
}

fun String.isInRegex(regex: String): Boolean {
    return matches(regex.toRegex())
}
