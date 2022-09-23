package battleship.server.utils

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder

var pbkdf2PasswordEncoder = Pbkdf2PasswordEncoder("salt", 1000, 128) //aumentar estes valores vai metendo mais lento. Se o salt for alterado as PW passadas ja nao sao válidas

fun hashPassword(password: String) = pbkdf2PasswordEncoder.encode(password)

fun checkPassword(password: String, hashedPassword: String) =
    pbkdf2PasswordEncoder.matches(password, hashedPassword)
