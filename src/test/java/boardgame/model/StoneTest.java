package boardgame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoneTest {

    @Test
    void testToString() {
        Stone stone = new Stone(StoneType.DESELECTED_STONE, new Position(0, 0));
        assertEquals("DESELECTED_STONE (0,0)", stone.toString());
    }
}