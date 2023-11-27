package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class UsedTilesTest {
    private ArrayList<Tile> tiles;
    private UsedTiles usedTiles;
    @Before
    public void setUp() {
        usedTiles = new UsedTiles();
    }

    @Test
    public void test_tiles() {
        tiles = new ArrayList();
        usedTiles.give(new ArrayList<>());
        tiles.addAll(List.of(Tile.STARTING_PLAYER, Tile.RED, Tile.BLACK, Tile.RED));
        assertEquals("UsedTiles should be empty when created.", "", usedTiles.state());
        usedTiles.give(tiles);
        assertEquals("UsedTiles should contain tiles we put in, withou STARTING_PLAYER tile.", "RLR", usedTiles.state());
        usedTiles.takeAll();
        assertEquals("UsedTiles should be empty after takeAll().", "", usedTiles.state());

        usedTiles.give(new ArrayList<>());
    }
}
