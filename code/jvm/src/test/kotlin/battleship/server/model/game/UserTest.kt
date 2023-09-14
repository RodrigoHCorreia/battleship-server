package battleship.server.model.game


import battleship.server.domain.game.EPlayer
import org.junit.jupiter.api.Test
import kotlin.test.assertSame

class UserTest {
    @Test
    fun `Test Player other`() {
        val playerHost = EPlayer.PLAYER1
        val playerGuest = EPlayer.PLAYER2

        assertSame(playerHost.other(), playerGuest)
        assertSame(playerGuest.other(), playerHost)
        assertSame(playerHost.other().other(), playerHost)
        assertSame(playerGuest.other().other(), playerGuest)
    }
}
