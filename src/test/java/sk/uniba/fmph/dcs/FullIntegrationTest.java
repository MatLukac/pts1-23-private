package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Array;
import java.util.*;


import interfaces.*;
import org.junit.Before;
import org.junit.Test;

class FakeObserver implements ObserverInterface {
    private String state;

    public FakeObserver() {
        state = "";
    }

    public String getState() {
        return state;
    }

    @Override
    public void notify(String newState) {
        state = newState;
    }
}

public class FullIntegrationTest {
    private UsedTiles usedTiles;
    private Bag bag;
    private ArrayList<TileSource> tileSources;
    private TableCenter tableCenter;
    private TableArea tableArea;
    private ArrayList<Floor> floors;
    private ArrayList<ArrayList<PatternLineInterface>> patternLines;
    private ArrayList<ArrayList<WallLineInterface>> wallLines;
    private GameFinished gameFinished;
    private FinalPointsCalculation finalPointsCalculation;
    private ArrayList<BoardInterface> boards;
    private GameObserver gameObserver;
    private Game game;
    private FakeObserver observer;

    @Before
    public void setUp() {
        usedTiles = new UsedTiles();
        bag = new Bag(usedTiles, 42);
        tableCenter = new TableCenter();
        tileSources = new ArrayList<>(List.of(tableCenter, new Factory(bag, tableCenter), new Factory(bag, tableCenter)));
        tableArea = new TableArea(tileSources);
        floors = new ArrayList();
        wallLines = new ArrayList();
        patternLines = new ArrayList();
        boards = new ArrayList();
        gameFinished = new GameFinished();
        finalPointsCalculation = new FinalPointsCalculation();
        gameObserver = new GameObserver();
        observer = new FakeObserver();
        gameObserver.registerObserver(observer);

        for (int i = 0; i < 2; i++) {
            floors.add(new Floor(usedTiles, new ArrayList<>(List.of(new Points(-1), new Points(-1), new Points(-2), new Points(-2), new Points(-2), new Points(-3), new Points(-3)))));
            ArrayList<WallLineInterface> tmpWallLine = new ArrayList();
            tmpWallLine.add(new WallLine(List.of(Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.BLACK, Tile.GREEN), null, null, new boolean[]{false, true, true, true, false}));
            tmpWallLine.add(new WallLine(List.of(Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.RED, Tile.BLACK), null, null, new boolean[]{true, true, true, true, false}));
            tmpWallLine.add(new WallLine(List.of(Tile.BLACK, Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.RED), null, null, new boolean[]{false, true, true, true, false}));
            tmpWallLine.add(new WallLine(List.of(Tile.RED, Tile.BLACK, Tile.GREEN, Tile.BLUE, Tile.YELLOW), null, null, new boolean[]{false, true, true, false, false}));
            tmpWallLine.add(new WallLine(List.of(Tile.YELLOW, Tile.RED, Tile.BLACK, Tile.GREEN, Tile.BLUE), null, null, new boolean[]{false, true, false, true, false}));

            tmpWallLine.get(0).setLineDown((WallLine) tmpWallLine.get(1));
            tmpWallLine.get(1).setLineUp((WallLine) tmpWallLine.get(0));
            tmpWallLine.get(1).setLineDown((WallLine) tmpWallLine.get(2));
            tmpWallLine.get(2).setLineUp((WallLine) tmpWallLine.get(1));
            tmpWallLine.get(2).setLineDown((WallLine) tmpWallLine.get(3));
            tmpWallLine.get(3).setLineUp((WallLine) tmpWallLine.get(2));
            tmpWallLine.get(3).setLineDown((WallLine) tmpWallLine.get(4));
            tmpWallLine.get(4).setLineUp((WallLine) tmpWallLine.get(3));

            ArrayList<PatternLineInterface> tmpPatternLine = new ArrayList();
            tmpPatternLine.add(new PatternLine(1, tmpWallLine.get(0), floors.get(i), usedTiles));
            tmpPatternLine.add(new PatternLine(2, tmpWallLine.get(1), floors.get(i), usedTiles));
            tmpPatternLine.add(new PatternLine(3, tmpWallLine.get(2), floors.get(i), usedTiles));
            tmpPatternLine.add(new PatternLine(4, tmpWallLine.get(3), floors.get(i), usedTiles));
            tmpPatternLine.add(new PatternLine(5, tmpWallLine.get(4), floors.get(i), usedTiles));

            boards.add(new Board(floors.get(i), new ArrayList(), tmpPatternLine, tmpWallLine, finalPointsCalculation, gameFinished));
        }
        game = new Game(bag, tableArea, gameObserver, boards);
    }

    @Test
    public void fullIntegrationTest() {
        assertEquals("Observer should hold state: Game started.", "Game started.", observer.getState());
        //simulation of direct game. functionality of other classes were already tested. we are trying to show that all classes together cooperate
        tableArea.startNewRound();
        assertEquals("Starting player at the beginning  should be 0.", 0, game.getCurrentPlayerId());
        assertEquals("UsedTiles should be empty when created.", 0, usedTiles.state().length());
        assertEquals("TableCenter should only contain S when created.", "S", tableCenter.state());
        for (int i = 1; i < tileSources.size(); i++) {
            assertEquals("Each facotry should contain 4 tiles.", 4, tileSources.get(i).state().length());
            /*
            System.out.println(tileSources.get(i).state());
            while (!tileSources.get(i).isEmpty()) {
                tileSources.get(i).take(0);
            }

            */
        }

        assertEquals("Wrong indexing or player should yield false and starting player should not change.", false,
                game.take(1, 0, 0, 0) || game.take(0, -1, 0, 0) ||
                        game.take(0, 0, -1, 0));

        //System.out.println(tileSources.get(0).take(0));
        assertEquals("Player 0 took Tile.STARTING_PLAYER form tableCenter.", true, game.take(0, 0, 0, 0));
        assertEquals(0, game.getNextStartingPlayer());
        assertEquals(false, game.take(0, 0, 0, 0));
        assertEquals(true, game.take(1, 1, 0, 0));
        assertEquals(true, game.take(0, 0, 0, 0));
        assertEquals(true, game.take(1, 2, 0, 2));
        assertEquals(true, game.take(0, 0, 0, 0));
        assertEquals(true, game.take(1, 0, 0, 0));
        assertEquals("Observer should hold state: Game started.", "New round started.", observer.getState());
        assertEquals(true, game.take(0, 1, 0, 0));
        assertEquals(true, game.take(1, 0, 1, 1));
        assertEquals("Player 1 will lbe next starting player.", 1, game.getNextStartingPlayer());
        assertEquals(false, game.take(1, 0, 0, 0));
        assertEquals(true, game.take(0, 2, 0, 0));
        assertEquals(true, game.take(1, 0, 0, 0));
        assertEquals(true, game.take(0, 0, 0, 0));
        assertEquals(true, game.take(1, 0, 0, 0));
        assertEquals("Observer should hold state: Game ended.", "Game ended.", observer.getState());
        for (BoardInterface b : boards) System.out.println(b.state());
    }

}
