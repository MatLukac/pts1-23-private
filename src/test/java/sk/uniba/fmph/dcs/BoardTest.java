package sk.uniba.fmph.dcs;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardTest {

    private Board board;
    private Floor mockFloor;
    private Points mockPoints;
    private List<PatternLineInterface> mockPatternLines;
    private List<WallLineInterface> mockWallLines;

    @Before
    public void setUp() {
        mockFloor = mock(Floor.class);
        mockPoints = mock(Points.class);
        mockPatternLines = Arrays.asList(mock(PatternLineInterface.class), mock(PatternLineInterface.class));
        mockWallLines = Arrays.asList(mock(WallLineInterface.class), mock(WallLineInterface.class));

        board = new Board(mockFloor, mockPoints, mockPatternLines, mockWallLines);
    }

    @Test
    public void testPutOnFloor() {
        List<Tile> tiles = Arrays.asList(Tile.BLUE, Tile.YELLOW);
        board.put(-1, tiles);
        verify(mockFloor, times(1)).put(tiles);
    }

    @Test
    public void testPutOnPatternLine() {
        List<Tile> tiles = Arrays.asList(Tile.RED);
        board.put(0, tiles);
        verify(mockPatternLines.get(0), times(1)).put(tiles);
    }

    @Test
    public void testFinishRound() {
        // Assume specific behaviors and interactions, then verify them
        when(mockPatternLines.get(0).finishRound()).thenReturn(10);
        when(mockFloor.finishRound()).thenReturn(5);

        FinishRoundResult result = board.finishRound();

        verify(mockPoints, times(1)).add(10);
        verify(mockPoints, times(1)).add(5);
        // You may need to verify other interactions or check the result
    }

    @Test
    public void testEndGame() {
        // Similar to testFinishRound, mock behavior and verify interactions
    }

    @Test
    public void testState() {
        when(mockPatternLines.get(0).state()).thenReturn("Pattern Line 1 State");
        when(mockWallLines.get(0).state()).thenReturn("Wall Line 1 State");
        when(mockFloor.state()).thenReturn("Floor State");
        when(mockPoints.toString()).thenReturn("Points State");

        String expectedState = "Pattern Lines:\nPattern Line 1 State\n" +
                "Wall Lines:\nWall Line 1 State\n" +
                "Floor:\nFloor State\n" +
                "Points State\n";

        assertEquals(expectedState, board.state());
    }

    // Add more tests as needed
}
