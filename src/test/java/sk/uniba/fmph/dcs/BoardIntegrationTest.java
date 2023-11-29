package sk.uniba.fmph.dcs;

import java.lang.reflect.Array;
import java.util.*;

import interfaces.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


class FakeUsedTilesGive implements UsedTilesGiveInterface {
    public ArrayList<Tile> tiles;

    public FakeUsedTilesGive() {
        this.tiles = new ArrayList();
    }

    @Override
    public void give(Collection<Tile> tiles) {
        this.tiles.addAll(tiles);
    }
}

public class BoardIntegrationTest {
    private Board board;
    private FinalPointsCalculation finalPointsCalculation;
    private GameFinished gameFinished;
    private ArrayList<WallLineInterface> wallLines;
    private ArrayList<PatternLineInterface> patternLines;
    private Floor floor;
    private FakeUsedTilesGive usedTiles;

    @Before
    public void setUp() {
        finalPointsCalculation = new FinalPointsCalculation();
        gameFinished = new GameFinished();
        usedTiles = new FakeUsedTilesGive();

        ArrayList<Points> pointPattern = new ArrayList<>(List.of(new Points(-1), new Points(-1), new Points(-2), new Points(-2), new Points(-2), new Points(-3), new Points(-3)));
        floor = new Floor(usedTiles, pointPattern);

        wallLines = new ArrayList(5);
        wallLines.add(new WallLine(List.of(Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.BLACK, Tile.GREEN), null, null));
        wallLines.add(new WallLine(List.of(Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.BLACK), null, null));
        wallLines.add(new WallLine(List.of(Tile.BLACK, Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.RED), null, null));
        wallLines.add(new WallLine(List.of(Tile.RED, Tile.BLACK, Tile.GREEN, Tile.BLUE, Tile.YELLOW), null, null));
        wallLines.add(new WallLine(List.of(Tile.YELLOW, Tile.RED, Tile.BLACK, Tile.GREEN, Tile.BLUE), null, null));

        wallLines.get(0).setLineDown((WallLine) wallLines.get(1));
        wallLines.get(1).setLineUp((WallLine) wallLines.get(0));
        wallLines.get(1).setLineDown((WallLine) wallLines.get(2));
        wallLines.get(2).setLineUp((WallLine) wallLines.get(1));
        wallLines.get(2).setLineDown((WallLine) wallLines.get(3));
        wallLines.get(3).setLineUp((WallLine) wallLines.get(2));
        wallLines.get(3).setLineDown((WallLine) wallLines.get(4));
        wallLines.get(4).setLineUp((WallLine) wallLines.get(3));

        patternLines = new ArrayList(5);
        patternLines.add(new PatternLine(1, wallLines.get(0), floor, usedTiles));
        patternLines.add(new PatternLine(2, wallLines.get(1), floor, usedTiles));
        patternLines.add(new PatternLine(3, wallLines.get(2), floor, usedTiles));
        patternLines.add(new PatternLine(4, wallLines.get(3), floor, usedTiles));
        patternLines.add(new PatternLine(5, wallLines.get(4), floor, usedTiles));

        board = new Board(floor, new ArrayList(), patternLines, wallLines, finalPointsCalculation, gameFinished);
    }

    @Test
    public void boardIntegrationTest() {
        for (int i = 0; i < 5; i++) {
            assertEquals("All WallLines should be empty when created.", "_".repeat(5), wallLines.get(i).state());
        }
        for (int i = 0; i < 5; i++) {
            assertEquals("All PatternLines should be empty when created.", ".".repeat(i + 1), patternLines.get(i).state());
        }

        assertEquals("Floor should be empty when created.", "", floor.state());

        board.put(0, new ArrayList(List.of(Tile.RED)));
        board.put(1, new ArrayList(List.of(Tile.RED, Tile.RED)));
        board.put(2, new ArrayList(List.of(Tile.RED, Tile.RED, Tile.RED)));
        board.put(3, new ArrayList(List.of(Tile.RED, Tile.RED, Tile.RED, Tile.RED, Tile.RED)));
        board.put(4, new ArrayList(List.of(Tile.RED, Tile.RED, Tile.RED)));

        assertEquals("First PatternLine should contain 'R'.", "R", patternLines.get(0).state());
        assertEquals("Second PatternLine should contain 'RR'.", "RR", patternLines.get(1).state());
        assertEquals("Third PatternLine should contain 'RRR'.", "RRR", patternLines.get(2).state());
        assertEquals("Fourth PatternLine should contain 'RRRR'.", "RRRR", patternLines.get(3).state());
        assertEquals("Fifth PatternLine should contain 'RRR..'.", "RRR..", patternLines.get(4).state());
        assertEquals("Floor now should contain 'RR'.", "R", floor.state());

        assertEquals("There is no finish row, so finishRound should yield FinishRoundResult.NORMAL.", FinishRoundResult.NORMAL, board.finishRound());
        //System.out.println(wallLines.get(4).state());
        assertEquals("After board.finishRound(), first wall should be  '__R__'.", "__R__", wallLines.get(0).state());
        assertEquals("After board.finishRound(), second wall should be '___R_'.", "___R_", wallLines.get(1).state());
        assertEquals("After board.finishRound(), third wall should be  '____R'.", "____R", wallLines.get(2).state());
        assertEquals("After board.finishRound(), fourth wall should be 'R____'.", "R____", wallLines.get(3).state());
        assertEquals("After board.finishRound(), fifth wall should be  '_____'.", "_____", wallLines.get(4).state());
        assertSame("After board.finishRound(), floor should be empty.", "", floor.state());

        assertEquals("Player now should have 3 points.", 3, board.getPoints().getValue());

        board.put(0, new ArrayList(List.of(Tile.GREEN)));
        board.put(1, new ArrayList(List.of(Tile.GREEN, Tile.GREEN)));
        board.put(2, new ArrayList(List.of(Tile.GREEN, Tile.GREEN, Tile.GREEN)));
        board.put(3, new ArrayList(List.of(Tile.BLACK, Tile.BLACK, Tile.BLACK, Tile.BLACK)));
        board.put(4, new ArrayList(List.of(Tile.GREEN)));

        assertEquals("First PatternLine should contain 'G'.", "G", patternLines.get(0).state());
        assertEquals("Second PatternLine should contain 'GG'.", "GG", patternLines.get(1).state());
        assertEquals("Third PatternLine should contain 'GGG'.", "GGG", patternLines.get(2).state());
        assertEquals("Fourth PatternLine should contain 'LLLL'.", "LLLL", patternLines.get(3).state());
        assertEquals("Fifth PatternLine should contain 'RRR..'.", "RRR..", patternLines.get(4).state());
        assertEquals("Floor now should contain 'G'.", "G", floor.state());

        assertEquals("There is no finish row, so finishRound should yield FinishRoundResult.NORMAL.", FinishRoundResult.NORMAL, board.finishRound());

        assertEquals("Player now should have points.", 9, board.getPoints().getValue());

        //Let's fill the row, column and color to test endGame Points rewards
        board.put(0, new ArrayList(List.of(Tile.BLUE)));
        board.put(1, new ArrayList(List.of(Tile.BLUE, Tile.BLUE)));
        board.put(4, new ArrayList(List.of(Tile.RED, Tile.RED)));
        assertEquals("Round should end with exist FinishRoundResult.NORMAL .", FinishRoundResult.NORMAL, board.finishRound());
        assertEquals("points = 20, 9 + 2 for two linked tiles in column (blue patternLine0) + 2 for two linked tiles in row (blue patternLine1) " +
                "+ 3 for three linked tiles in column (blue patternLine1) + 4 for four linked tiles in column (red patternLine4).", 20, board.getPoints().getValue());

        board.put(0, new ArrayList(List.of(Tile.YELLOW)));
        assertEquals("Round should end with exist FinishRoundResult.NORMAL .", FinishRoundResult.NORMAL, board.finishRound());
        assertEquals("points = 28, 20 + 3 for three linked tiles in row (yellow patternLine0) + 5 linked tiles for tiles in column (yellow patternLine0).", 28, board.getPoints().getValue());

        board.put(0, new ArrayList(List.of(Tile.BLACK)));
        assertEquals("Round should end with exist FinishRoundResult.GAME_FINISHED .", FinishRoundResult.GAME_FINISHED, board.finishRound());
        assertEquals("points = 54, 28 + 5 for five linked tiles in a row (black patternLine0) + 2 for two linked tiles in column (black patternLine0) + 2 for full row + 7 for full column + 10 for full collor", 54, board.getPoints().getValue());
    }
}
