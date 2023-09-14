package battleship.server.utils

import java.time.Instant

/**
 * Interface that represents a clock.
 */
interface Clock {
    fun now(): Instant
}

object RealClock : Clock {
    override fun now(): Instant = Instant.ofEpochSecond(Instant.now().epochSecond)
}