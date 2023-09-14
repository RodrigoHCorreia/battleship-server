package battleship.server.model.game

import battleship.server.domain.game.Dimension
import battleship.server.domain.game.Rules
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class RulesTest {

    @Test
    fun `Test is valid Dimension`() {
        assertThrows<IllegalArgumentException> { Rules(boardDimension = Dimension(2, 10)) }
        assertThrows<IllegalArgumentException> { Rules(boardDimension = Dimension(10, 2)) }
        assertThrows<IllegalArgumentException> { Rules(boardDimension = Dimension(2, 2)) }
        assertThrows<IllegalArgumentException> { Rules(boardDimension = Dimension(-1, 10)) }
        assertThrows<IllegalArgumentException> { Rules(boardDimension = Dimension(10, -1)) }
        assertThrows<IllegalArgumentException> { Rules(boardDimension = Dimension(-1, -1)) }
        assertDoesNotThrow { Rules(boardDimension = Dimension(10, 10)) }
        assertDoesNotThrow { Rules(boardDimension = Dimension(5, 10)) }
        assertDoesNotThrow { Rules(boardDimension = Dimension(10, 5)) }
        assertDoesNotThrow { Rules(boardDimension = Dimension(5, 5)) }
    }
}