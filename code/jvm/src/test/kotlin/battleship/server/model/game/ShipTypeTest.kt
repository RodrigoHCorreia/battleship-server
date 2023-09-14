package battleship.server.model.game

import battleship.server.domain.game.Rules
import battleship.server.domain.game.ship.ShipType
import battleship.server.domain.game.ship.toShipType
import battleship.server.domain.game.ship.toShipTypeOrNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class ShipTypeTest {

    private val rulesMock = Rules.defaultRules
    private val shipTypesMock = rulesMock.shiptypes

    @Test
    fun `ShipType Valid Use`() {
        val name = "Carrier"
        val size = 1
        val quantity = 1
        val sut = ShipType(name, size, quantity)
        assertEquals(sut.name, name)
        assertEquals(sut.squares, size)
        assertEquals(sut.quantity, quantity)
    }

    @Test
    fun `Invalid Construction`() {
        // empty ship name
        assertThrows<IllegalArgumentException> { ShipType("", 1, 1) }
        // blank name
        assertThrows<IllegalArgumentException> { ShipType("     ", 1, 1) }
        // 0 size
        assertThrows<IllegalArgumentException> { ShipType("Carrier", 0, 1) }
        // 0 quantity
        assertThrows<IllegalArgumentException> { ShipType("Carrier", 1, 0) }
    }

    @Test
    fun `get ShipType by name prefix`() {
        val sut = "Carr".toShipTypeOrNull(shipTypesMock)
        assertNotNull(sut) // Yes
        assertEquals(5, sut.squares)
        assertEquals(1, sut.quantity)
        assertSame("Carrier".toShipType(shipTypesMock), sut)
    }

    @Test
    fun `get ShipType fails`() {
        assertEquals(null, "0".toShipTypeOrNull(shipTypesMock))
        assertEquals(null, "Ab".toShipTypeOrNull(shipTypesMock))
        assertFailsWith<NoSuchElementException> { "X".toShipType(shipTypesMock) }
        assertFailsWith<NoSuchElementException> { "C".toShipType(shipTypesMock) }
    }
}
