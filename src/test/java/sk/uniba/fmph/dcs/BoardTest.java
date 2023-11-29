package sk.uniba.fmph.dcs;

import interfaces.FinalPointsCalculationInterface;
import interfaces.GameFinishedInterface;
import interfaces.PatternLineInterface;
import interfaces.WallLineInterface;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

class FakePatternLine implements PatternLineInterface {
    public int capacity;
    private List<Tile> tiles = new ArrayList<>();
    private int pointsToReturn = 0; // Default points to return in finishRound

    public FakePatternLine(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void put(Collection<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    @Override
    public Points finishRound() {
        Points points = new Points(pointsToReturn);
        tiles.clear(); // Assuming the tiles are cleared at the end of the round
        return points;
    }

    @Override
    public String state() {
        int k = 0;
        StringBuilder result = new StringBuilder();
        for (Tile tile : tiles) {
            result.append(tile.toString());
            k++;
        }
        result.append(".".repeat(Math.max(0, capacity - k)));
        return result.toString();
    }

    // Method to set the points to be returned in finishRound
    public void setPointsToReturn(int points) {
        this.pointsToReturn = points;
    }
}

class FakeWallLine implements WallLineInterface {
    private List<Optional<Tile>> tiles = new ArrayList<>();

    @Override
    public boolean canPutTile(Tile tile) {
        return true;
    }

    @Override
    public List<Optional<Tile>> getTiles() {
        return tiles;
    }

    @Override
    public Points putTile(Tile tile) {
        tiles.add(Optional.ofNullable(tile));
        return new Points(0); // Return 0 points as default
    }

    @Override
    public void setLineUp(WallLine lineUp) {

    }

    @Override
    public void setLineDown(WallLine lineUp) {

    }

    @Override
    public String state() {
        return ".....";
    }
}

class FakeFinalPointsCal implements FinalPointsCalculationInterface {
    @Override
    public Points getPoints(List<List<Optional<Tile>>> wall) {
        return null;
    }
}

class FakeGameFinished implements GameFinishedInterface {
    @Override
    public FinishRoundResult gameFinished(List<List<Optional<Tile>>> wall) {
        return FinishRoundResult.NORMAL;
    }
}


public class BoardTest {
    private Board board;
    private Floor fakeFloor;
    private ArrayList<Points> fakePoints;
    private ArrayList<PatternLineInterface> fakePatternLines;
    private ArrayList<WallLineInterface> fakeWallLines;

    private FakeFinalPointsCal finalPointsCalculation;
    private FakeGameFinished gameFinished;

    @Before
    public void setUp() {
        // Initialize the fake objects
        FakeUsedTiles usedTiles = new FakeUsedTiles();
        ArrayList<Points> pointPattern = new ArrayList<>();
        pointPattern.add(new Points(-1));
        pointPattern.add(new Points(-2));

        fakeFloor = new Floor(usedTiles, pointPattern);
        fakePoints = new ArrayList(List.of(new Points(5)));
        fakePatternLines = new ArrayList(List.of(new FakePatternLine(1), new FakePatternLine(2)));
        fakeWallLines = new ArrayList<>(List.of(new FakeWallLine(), new FakeWallLine()));

        finalPointsCalculation = new FakeFinalPointsCal();
        gameFinished = new FakeGameFinished();

        board = new Board(fakeFloor, fakePoints, fakePatternLines, fakeWallLines, finalPointsCalculation, gameFinished);
    }

    @Test
    public void testPut() {
        ArrayList<Tile> tiles1 = new ArrayList(List.of(Tile.BLUE, Tile.BLUE));
        board.put(-1, tiles1);
        assertEquals("tiles should go to floor", "BB", fakeFloor.state());
        ArrayList<Tile> tiles2 = new ArrayList(List.of(Tile.STARTING_PLAYER));
        board.put(0, tiles2);
        assertEquals("should go to floor", "BBS", fakeFloor.state());
        ArrayList<Tile> tiles3 = new ArrayList(List.of(Tile.BLACK, Tile.BLACK));
        board.put(0, tiles3);
        assertEquals("LL", fakePatternLines.get(0).state());
    }

    @Test
    public void testFinishRound() {
        ArrayList<Tile> tiles1 = new ArrayList(List.of(Tile.BLACK));
        board.put(0, tiles1);
        ArrayList<Tile> tiles2 = new ArrayList(List.of(Tile.BLACK, Tile.BLACK));
        board.put(1, tiles2);
        board.finishRound();
        assertEquals("Points should be 5", new Points(5), board.getPoints());
        ArrayList<Tile> tiles3 = new ArrayList(List.of(Tile.GREEN));
        board.put(-1, tiles3);
        board.finishRound();
        assertEquals("After adding one tile to floor, points should go down minus 1, therefore to 4", new Points(4), board.getPoints());
    }

    @Test
    public void testEndGame() {
    }

    @Test
    public void testState() {
        ArrayList<Tile> tiles1 = new ArrayList(List.of(Tile.RED));
        ArrayList<Tile> tiles2 = new ArrayList(List.of(Tile.YELLOW));
        board.put(0, tiles1);
        board.put(1, tiles2);
        String expectedState = """
                Pattern Lines:
                R
                I.
                Wall Lines:
                .....
                .....
                Floor:
                                
                Points[value=5]
                """;
        assertEquals(expectedState, board.state());

    }

}
