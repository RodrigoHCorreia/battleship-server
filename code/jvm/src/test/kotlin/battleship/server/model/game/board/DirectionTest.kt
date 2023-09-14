package battleship.server.model.game.board

import Direction
import battleship.server.domain.game.board.Position
import extend
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DirectionTest {

    @Test
    fun `Test Direction extension`() {
        val root = Position(0, 0)
        assertEquals(root, root.extend(0, Direction.DOWN))
        assertEquals(root, root.extend(0, Direction.RIGHT))

        assertEquals(Position(0, 1), root.extend(1, Direction.DOWN))
        assertEquals(Position(1, 0), root.extend(1, Direction.RIGHT))
    }

    @Test
    fun `Test Direction Exceptions`() {
        val root = Position(0, 0)
        assertThrows<IllegalStateException> { root.extend(-1, Direction.DOWN) }
        assertThrows<IllegalStateException> { root.extend(-1, Direction.RIGHT) }
    }

}
