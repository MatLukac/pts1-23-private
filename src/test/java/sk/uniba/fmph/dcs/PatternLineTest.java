package sk.uniba.fmph.dcs;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

class FakeFloor implements FloorPutInterface {
    public ArrayList<Tile> tiles;
    public FakeFloor() {tiles = new ArrayList();}

    @Override
    public void put(Collection<Tile> tiles) {
        this.tiles.addAll(tiles);
    }
}

class FakeWallLine implements WallLineInterface {
    public ArrayList<Optional<Tile>> tiles;
    public FakeWallLine() {tiles = new ArrayList<>();}

    @Override
    public boolean canPutTile(Tile tile) {
        if(tiles.contains(tile)) return false;
        else return true;

    }

    @Override
    public ArrayList<Optional<Tile>> getTiles() {
        return tiles;
    }

    @Override
    public Points putTile(Tile tile) {
        tiles.add(Optional.ofNullable(tile));
        return new Points(10);
    }

    @Override
    public String state() {
        return null;
    }
}

public class PatternLineTest {
    private FakeUsedTiles usedTiles;
    private FakeFloor floor;
    private FakeWallLine wallLine;
    private PatternLine patternLine;
    @Before
    public void setUp(){
        usedTiles = new FakeUsedTiles();
        wallLine = new FakeWallLine();
        floor = new FakeFloor();
        patternLine = new PatternLine(4,wallLine, floor, usedTiles);
    }

    @Test
    public void patternLineTest(){
        assertEquals("PatternLine should be empty when created." , "", patternLine.state());
        patternLine.put(List.of(Tile.RED, Tile.RED));
        String state = patternLine.state();
        assertEquals("PatternLine now should contain RR.", "RR", patternLine.state());
        patternLine.put(List.of(Tile.GREEN, Tile.GREEN));
        assertEquals("Pattern should not add 'GG' when already contains 'RR'.", state, patternLine.state());
        String s = "";
        for(Tile tile : floor.tiles) s += tile.toString();
        assertEquals("After false put(), patternLine should put them in Foor.", s, "GG");
        patternLine.finishRound();
        assertEquals("If PatternLine is not fulled to its capacity, then finishRound() should not change PatternLine.",state, patternLine.state());
        patternLine.put(List.of(Tile.RED, Tile.RED, Tile.RED));
        assertEquals("PatternLine now should be full.", "RRRR", patternLine.state());
        s = "";
        for(Tile tile : floor.tiles) s += tile.toString();
        assertEquals("Extra tiles should be in Floor.", s,"GGR");
        patternLine.finishRound();
        s = "";
        for(Tile tile : usedTiles.tiles) s += tile.toString();
        assertEquals("When PatternLine is full, after finishRound() one is placed to WallLine and rest to UsedTiles.", "RRR", s);
    }
}
