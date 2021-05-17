package boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    GameModel gameModel;

    @BeforeEach
    void init() {
        gameModel = new GameModel();
    }

    @Test
    void isOnBoard() {
        assertTrue(GameModel.isOnBoard(new Position(0, 0)));
        assertTrue(GameModel.isOnBoard(new Position(1, 2)));
        assertTrue(GameModel.isOnBoard(new Position(2, 3)));
        assertTrue(GameModel.isOnBoard(new Position(3, 3)));
        assertFalse(GameModel.isOnBoard(new Position(2, 6)));
        assertFalse(GameModel.isOnBoard(new Position(8, 3)));
        assertFalse(GameModel.isOnBoard(new Position(-5, -5)));
        assertFalse(GameModel.isOnBoard(new Position(9, 7)));
        assertFalse(GameModel.isOnBoard(new Position(Integer.MIN_VALUE, 2)));
        assertFalse(GameModel.isOnBoard(new Position(1, Integer.MAX_VALUE)));
        assertFalse(GameModel.isOnBoard(new Position(Integer.MIN_VALUE, Integer.MAX_VALUE)));
    }

    @Test
    void getStoneCount() {
        assertEquals(16, gameModel.getStoneCount());
        assertNotEquals(9, gameModel.getStoneCount());
        assertNotEquals(Integer.MIN_VALUE, gameModel.getStoneCount());
    }

    @Test
    void getStonePosition() {
        assertEquals(new Position(0, 0), gameModel.getStonePosition(0));
        assertEquals(new Position(2, 1), gameModel.getStonePosition(9));
        assertEquals(new Position(3, 2), gameModel.getStonePosition(14));
        assertNotEquals(new Position(0, 0), gameModel.getStonePosition(6));
        assertNotEquals(new Position(1, 3), gameModel.getStonePosition(11));
    }

    @Test
    void getCurrentPlayer() {
        assertEquals(GameModel.Player.FIRST_PLAYER, gameModel.getCurrentPlayer());
        assertNotEquals(GameModel.Player.SECOND_PLAYER, gameModel.getCurrentPlayer());

        // After switching to SECOND_PLAYER
        assertEquals(GameModel.Player.SECOND_PLAYER, gameModel.getCurrentPlayer().alterPlayer());
        assertNotEquals(GameModel.Player.FIRST_PLAYER, gameModel.getCurrentPlayer().alterPlayer());

        // After switching back to FIRST_PLAYER
        assertEquals(GameModel.Player.FIRST_PLAYER, gameModel.getCurrentPlayer().alterPlayer().alterPlayer());
        assertNotEquals(GameModel.Player.SECOND_PLAYER, gameModel.getCurrentPlayer().alterPlayer().alterPlayer());
    }

    @Test
    void getStoneNumber() {
        assertEquals(OptionalInt.of(0), gameModel.getStoneNumber(new Position(0,0)));
        assertEquals(OptionalInt.of(8), gameModel.getStoneNumber(new Position(2,0)));
        assertEquals(OptionalInt.of(13), gameModel.getStoneNumber(new Position(3,1)));
        assertEquals(OptionalInt.empty(), gameModel.getStoneNumber(new Position(8,6)));
        assertEquals(OptionalInt.empty(), gameModel.getStoneNumber(new Position(Integer.MIN_VALUE,115)));
        assertEquals(OptionalInt.empty(), gameModel.getStoneNumber(new Position(-6,Integer.MAX_VALUE)));
    }

    @Test
    void selectStone() {
        gameModel.selectStone(new Position(0,0));
        gameModel.selectStone(new Position(2,1));
        assertEquals(StoneType.SELECTED_STONE, gameModel.getStoneType(new Position(0,0)));
        assertEquals(StoneType.SELECTED_STONE, gameModel.getStoneType(new Position(2,1)));
        assertNotEquals(StoneType.SELECTED_STONE, gameModel.getStoneType(new Position(1,1)));
        assertNotEquals(StoneType.SELECTED_STONE, gameModel.getStoneType(new Position(0,3)));
    }

    @Test
    void deselectStone() {
        gameModel.selectStone(new Position(1, 1));
        gameModel.selectStone(new Position(2, 2));
        gameModel.deselectStone(new Position(1, 1));
        gameModel.deselectStone(new Position(2, 2));
        assertEquals(StoneType.DESELECTED_STONE, gameModel.getStoneType(new Position(1,1)));
        assertEquals(StoneType.DESELECTED_STONE, gameModel.getStoneType(new Position(2,2)));
    }

    @Test
    void removeStones() {
        gameModel.selectStone(new Position(3, 2));
        gameModel.selectStone(new Position(3, 3));
        gameModel.removeStones(List.of(new Position(3, 2), new Position(3, 3)));
        assertEquals(StoneType.REMOVED_STONE, gameModel.getStoneType(new Position(3, 2)));
        assertEquals(StoneType.REMOVED_STONE, gameModel.getStoneType(new Position(3, 3)));
    }

    @Test
    void isRemovableSelection() {
        var selection1 = Arrays.asList(new Position(0, 0), new Position(1, 0));
        assertTrue(gameModel.isRemovableSelection(selection1));

        var selection2 = Arrays.asList(new Position(1, 1), new Position(1, 2));
        assertTrue(gameModel.isRemovableSelection(selection2));

        var selection3 = Arrays.asList(new Position(0, 0), new Position(1, 0), new Position(3, 0));
        assertTrue(gameModel.isRemovableSelection(selection3));

        var selection4 = Arrays.asList(new Position(0, 0), new Position(0, 1), new Position(0, 3));
        assertTrue(gameModel.isRemovableSelection(selection4));

        var selection5 = Collections.singletonList(new Position(3, 3));
        assertTrue(gameModel.isRemovableSelection(selection5));

        List<Position> selection6 = Collections.emptyList();
        assertFalse(gameModel.isRemovableSelection(selection6));

        var selection7 = Arrays.asList(new Position(0, 0), new Position(1, 1));
        assertFalse(gameModel.isRemovableSelection(selection7));


        gameModel.selectStone(new Position(2, 2));
        gameModel.removeStones(List.of(new Position(2,2)));
        var selection8 = Arrays.asList(new Position(2, 1), new Position(2, 3));
        assertFalse(gameModel.isRemovableSelection(selection8));

        var selection9 = Arrays.asList(new Position(1, 2), new Position(3, 2));
        assertFalse(gameModel.isRemovableSelection(selection9));
    }

    @Test
    void isEnd() {
        assertFalse(gameModel.isEnd());
    }

    @Test
    void testToString() {
        assertEquals("""
                DESELECTED_STONE (0,0)
                DESELECTED_STONE (0,1)
                DESELECTED_STONE (0,2)
                DESELECTED_STONE (0,3)
                DESELECTED_STONE (1,0)
                DESELECTED_STONE (1,1)
                DESELECTED_STONE (1,2)
                DESELECTED_STONE (1,3)
                DESELECTED_STONE (2,0)
                DESELECTED_STONE (2,1)
                DESELECTED_STONE (2,2)
                DESELECTED_STONE (2,3)
                DESELECTED_STONE (3,0)
                DESELECTED_STONE (3,1)
                DESELECTED_STONE (3,2)
                DESELECTED_STONE (3,3)""", gameModel.toString());
    }
}