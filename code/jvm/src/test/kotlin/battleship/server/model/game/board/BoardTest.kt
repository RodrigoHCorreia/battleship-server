package battleship.server.model.game.board

import Direction
import battleship.server.domain.game.board.*
import battleship.server.domain.game.ship.ShipType
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class BoardTest {

    @Test
    fun `Board place ship`() {
        val size = 5
        val type = ShipType("Carrier", size, 1)
        val sut = Board()
        val new = sut.placeShip(type, Position(0, 0), Direction.DOWN)
        assertSame(new.second, PlaceResult.PLACED)

        val ship = new.first.fleet.first()
        ship.positions.forEach {
            val cell = new.first.grid[it]
            assertTrue { cell is ShipCell && cell.ship == ship }
        }
        assertSame(new.first.grid.size, size)
    }

}
