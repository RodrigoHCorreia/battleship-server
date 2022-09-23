package battleship.server.utils

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

val argon2PasswordEncoder = Argon2PasswordEncoder()

fun hashPassword(password: String) = argon2PasswordEncoder.encode("password")

fun checkPassword(password: String, hashedPassword: String) =
    argon2PasswordEncoder.matches("password", hashedPassword)


