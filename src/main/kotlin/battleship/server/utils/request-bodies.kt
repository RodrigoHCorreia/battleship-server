package battleship.server.utils


import javax.validation.constraints.Pattern


/**
 * https://stackoverflow.com/questions/61992596/spring-boot-valid-on-requestbody-in-controller-method-not-working mas isto nao funcionou segundo uns testes q fiz
 * Alternativa à soluçao de cima mas é preciso empacotar o request body dentro de um campo com o nome desse requestBody https://stackoverflow.com/questions/57325466/not-able-to-validate-request-body-in-spring-boot-with-valid
 * O kotlin não faz inherit de anotações... https://discuss.kotlinlang.org/t/inherited-annotations-and-other-reflections-enchancements/6209             https://discuss.kotlinlang.org/t/implement-inherit-extend-annotation-in-kotlin/8916
 */

const val pat = "^\\w+(\\s\\w+)*\$" //Only letter, numbers, only 1 space between words. [a-zA-Z0-9_]. https://stackoverflow.com/a/37402198/9375488
const val msg = "Fields can't contain leading or trailing spaces or symbols"

data class NewUser(
    @field:Pattern(regexp = pat, message = msg)
    val name: String,
    val password: String
)

data class NewGame(
    @field:Pattern(regexp = pat, message = msg)
    val lobbyName: String,
    @field:Pattern(regexp = pat, message = msg)
    val creatorName: String
)
