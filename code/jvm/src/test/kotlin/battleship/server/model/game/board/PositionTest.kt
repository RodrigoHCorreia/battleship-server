package battleship.server.model.game.board

import battleship.server.domain.game.board.Position
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class PositionTest {

    @Test
    fun `Position Incorrect initialization`() {
        assertThrows<IllegalStateException> { Position(-1, 0) }
        assertThrows<IllegalStateException> { Position(0, -1) }

        assertDoesNotThrow { Position(0, 0) }
        assertDoesNotThrow { Position(1, 0) }
        assertDoesNotThrow { Position(0, 1) }
    }

}
