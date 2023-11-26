package sk.uniba.fmph.dcs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TableCenterTest {
    @Test
    public void test_tableCenter() {
        TableCenter tableCenter = new TableCenter();
        tableCenter.startNewRound();
        assertEquals("TablaCenter Should have 1 Starting Player Tile.", "S", tableCenter.State());
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        tiles.add(Tile.RED);
        tiles.add(Tile.GREEN);
        tiles.add(Tile.RED);
        tableCenter.add(tiles);
        assertEquals("Center should contain tiles we put on it.", "SRGR", tableCenter.State());
        assertEquals("Center should take two red tiles.", new ArrayList<Tile>(List.of(Tile.STARTING_PLAYER, Tile.RED, Tile.RED)), tableCenter.take(1));
    }
}
