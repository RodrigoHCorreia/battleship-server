package battleship.server.model.game

import ShipType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ShipTypeTest {
    @Test
    fun `ShipType Valid Use`() {
        val name = "Carrier";
        val size = 1;
        val quant = 1;
        val sut = ShipType(name, size, quant);
        assertEquals(sut.name, name);
        assertEquals(sut.size, size);
        assertEquals(sut.quantity, quant);
    }

    @Test
    fun `Invalid Construction`() {
        // empty ship name
        assertThrows<IllegalStateException>{ ShipType("", 1, 1) };
        // blank name
        assertThrows<IllegalStateException>{ ShipType("     ", 1, 1) };
        // 0 size
        assertThrows<IllegalStateException>{ ShipType("Carrier", 0, 1) };
        // 0 quantity
        assertThrows<IllegalStateException>{ ShipType("Carrier", 1, 0) };
    }

}
