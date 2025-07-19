package chess.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testConstructor_ValidPosition() {
        Position p = new Position(0, 0);
        assertEquals(0, p.getRow());
        assertEquals(0, p.getColumn());

        Position p2 = new Position(7, 7);
        assertEquals(7, p2.getRow());
        assertEquals(7, p2.getColumn());
    }

    @Test
    public void testConstructor_InvalidRow() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
    }

    @Test
    public void testConstructor_InvalidColumn() {
        assertThrows(IllegalArgumentException.class, () -> new Position(0, 8));
    }

    @Test
    public void testCopyConstructor() {
        Position p1 = new Position(3, 4);
        Position p2 = new Position(p1);
        assertEquals(p1, p2);
        assertNotSame(p1, p2);
    }

    @Test
    public void testSetPositionWithPosition() {
        Position p = new Position(1, 1);
        Position newP = new Position(2, 3);
        p.setPosition(newP);
        assertEquals(2, p.getRow());
        assertEquals(3, p.getColumn());
    }

    @Test
    public void testSetPositionWithInts() {
        Position p = new Position(1, 1);
        p.setPosition(5, 6);
        assertEquals(5, p.getRow());
        assertEquals(6, p.getColumn());
    }

    @Test
    public void testSetPositionWithInts_Invalid() {
        Position p = new Position(1, 1);
        assertThrows(IllegalArgumentException.class, () -> p.setPosition(8, 0));
    }

    @Test
    public void testSetRow() {
        Position p = new Position(2, 2);
        p.setRow(5);
        assertEquals(5, p.getRow());
        assertEquals(2, p.getColumn());
    }

    @Test
    public void testSetRow_Invalid() {
        Position p = new Position(2, 2);
        assertThrows(IllegalArgumentException.class, () -> p.setRow(-1));
    }

    @Test
    public void testSetColumn() {
        Position p = new Position(2, 2);
        p.setColumn(7);
        assertEquals(2, p.getRow());
        assertEquals(7, p.getColumn());
    }

    @Test
    public void testSetColumn_Invalid() {
        Position p = new Position(2, 2);
        assertThrows(IllegalArgumentException.class, () -> p.setColumn(8));
    }

    @Test
    public void testIsValidValue() {
        assertTrue(Position.isValidRowOrColumn(0));
        assertTrue(Position.isValidRowOrColumn(7));
        assertFalse(Position.isValidRowOrColumn(-1));
        assertFalse(Position.isValidRowOrColumn(8));
    }

    @Test
    public void testIsValidPosition_StaticWithInts() {
        assertTrue(Position.isValidPosition(0, 0));
        assertTrue(Position.isValidPosition(7, 7));
        assertFalse(Position.isValidPosition(-1, 0));
        assertFalse(Position.isValidPosition(8, 0));
        assertFalse(Position.isValidPosition(0, 8));
        assertFalse(Position.isValidPosition(0, -1));
    }

    @Test
    public void testRankToRow() {
        assertEquals(7, Position.rankToRow('1'));
        assertEquals(0, Position.rankToRow('8'));
        assertEquals(3, Position.rankToRow('5'));
    }

    @Test
    public void testFileToColumn() {
        assertEquals(0, Position.fileToColumn('a'));
        assertEquals(7, Position.fileToColumn('h'));
        assertEquals(3, Position.fileToColumn('d'));
    }

    @Test
    public void testStringToPosition_Valid() {
        Position p = Position.stringToPosition("a8");
        assertEquals(0, p.getRow());
        assertEquals(0, p.getColumn());

        Position p2 = Position.stringToPosition("h1");
        assertEquals(7, p2.getRow());
        assertEquals(7, p2.getColumn());
    }

    @Test
    public void testStringToPosition_InvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> Position.stringToPosition("a"));
    }

    @Test
    public void testStringToPosition_InvalidFile() {
        assertThrows(IllegalArgumentException.class, () -> Position.stringToPosition("z9"));
    }

    @Test
    public void testStringToPosition_InvalidRank() {
        assertThrows(IllegalArgumentException.class, () -> Position.stringToPosition("a0"));
    }

    @Test
    public void testStringToPosition_InvalidRankOrFile() {
        assertThrows(IllegalArgumentException.class, () -> Position.stringToPosition("z9"));
    }

    @Test
    public void testEquals() {
        Position p1 = new Position(3, 4);
        Position p2 = new Position(3, 4);
        Position p3 = new Position(4, 3);
        Object notAPosition = new Object();
        assertFalse(p1.equals(notAPosition));
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertFalse(p1.equals(new Position(3, 2)));
    }

    @Test
    public void testHashCode() {
        Position p1 = new Position(3, 4);
        Position p2 = new Position(3, 4);
        Position p3 = new Position(4, 3);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
        assertNotEquals(p1.hashCode(), new Position(3, 2).hashCode());
    }

    @Test
    public void testToString() {
        Position p = new Position(2, 5);
        assertEquals("(2, 5)", p.toString());
    }
}