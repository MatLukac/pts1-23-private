package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;


import interfaces.TileSource;
import org.junit.Before;
import org.junit.Test;

public class TableAreaIntegrationTest {

    private Bag bag;
    private UsedTiles usedTiles;
    private ArrayList<TileSource> tileSources;
    private TableCenter tableCenter;
    private Factory factory;
    private TableArea tableArea;

    @Before
    public void setUp() {
        usedTiles = new UsedTiles(new ArrayList(List.of(Tile.RED, Tile.RED, Tile.RED, Tile.RED)));
        bag = new Bag(usedTiles, new ArrayList(List.of(Tile.BLACK, Tile.BLACK, Tile.BLUE, Tile.GREEN)));
        tableCenter = new TableCenter();
        tileSources = new ArrayList<>();
        factory = new Factory(bag, tableCenter);
        tileSources.add(tableCenter);
        tileSources.add(factory);
        tableArea = new TableArea(tileSources);
    }

    @Test
    public void newRound() {
        tableArea.startNewRound();
        assertEquals("After new round, there should be 0 tiles left in bag.", 0, bag.state().length());
        assertEquals("After new round, TableCenter should contain only Tile.STARTING_PLAYER.", "S", tableCenter.state());
        String factoriesStates = "";
        assertEquals("After new round, each Factory should contain 4 tiles.", 4, factory.state().length());

        assertEquals("tableAre.isRoundEnd() should yield false.", false, tableArea.isRoundEnd());
    }

    @Test
    public void wrongTake() {
        assertThrows(IllegalArgumentException.class, () -> tableArea.take(0, -1));
        assertThrows(IllegalArgumentException.class, () -> tableArea.take(-1, 0));
    }

    @Test
    public void takePlusEndOfTheRound() {
        tableArea.startNewRound();
        assertEquals("Factory should contain 'LLBG'.", "LLBG", factory.state());
        assertArrayEquals("take(1,0) should yield array of two Tile.BLACK", new ArrayList(List.of(Tile.BLACK, Tile.BLACK)).toArray(), tableArea.take(1, 0).toArray());
        assertEquals("Factory now should be empty.", "", tileSources.get(1).state());
        assertEquals("Rest (BG) should be in TableCenter with Tile.STARTING_PLAYER.", "SBG", tableCenter.state());

        assertEquals("there still should not be end of the round because of TableCenter", false, tableArea.isRoundEnd());
        tableArea.take(0, 0);
        tableArea.take(0, 0);
        tableArea.take(0, 0);
        assertEquals("everything should be empty except UsedTiles.", "", bag.state() + tableArea.state());
        assertEquals("now is end of the round.", true, tableArea.isRoundEnd());
        tableArea.startNewRound();
        assertEquals("Bag when not having enough tiles should take all tiles form UsedTiles when bag.take() is called (now it is called from tableArea.startNewRound).", "RRRR", factory.state());
    }


}
