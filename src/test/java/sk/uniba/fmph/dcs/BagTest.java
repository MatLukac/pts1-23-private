package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.List;

import interfaces.UsedTileTakeInterface;
import org.junit.Before;
import org.junit.Test;

class FakeUsedTilesBag implements UsedTileTakeInterface {
    public ArrayList<Tile> tiles;
    public FakeUsedTilesBag() {tiles = new ArrayList();}
    @Override
    public Collection<Tile> takeAll() {
        return tiles;
    }
}

public class BagTest {
    private FakeUsedTilesBag usedTiles;
    private Bag bag;

    @Before
    public void setUp(){
        usedTiles = new FakeUsedTilesBag();
        bag = new Bag(usedTiles);
    }

    @Test
    public void test_bag(){
        assertEquals("Bag when created should contain 100 tiles.", bag.state().length(), 100);
        HashMap<String, Integer> m = new HashMap<>();
        for(String c : bag.state().split("")) {
            if(m.get(c) != null) m.put(c,m.get(c)+1);
            else m.put(c,1);
        }
        for(int i : m.values()) {
            assertEquals("There should be 20 tile of each color at the beginning.", i, 20);
        }


        ArrayList<Tile> take = bag.take(4);
        take.addAll(bag.take(5));
        take.addAll(bag.take(5));
        take.addAll(bag.take(6));
        usedTiles.tiles.addAll(take);
        assertEquals("There should be left 80 tiles in bag.", bag.state().length(), 80);

        HashMap<Tile, Integer> mTake = new HashMap<>();
        for(Tile tile : take) {
            if(mTake.get(tile) != null) mTake.put(tile, mTake.get(tile)+1);
            else mTake.put(tile,1);
        }
        m = new HashMap<>();
        for(String c : bag.state().split("")) {
            if(m.get(c) != null) m.put(c,m.get(c)+1);
            else m.put(c,1);
        }
        boolean passed = true;
        for(Tile tile : List.of(Tile.RED, Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.YELLOW)){
            int mTileNum, mTakeTileNum;
            if(mTake.get(tile) == null) mTakeTileNum = 0;
            else mTakeTileNum = mTake.get(tile);

            if(m.get(tile.toString()) == null) mTileNum = 0;
            else mTileNum = m.get(tile.toString());

            if(mTileNum + mTakeTileNum != 20) {
                passed = false;
                break;
            }
        }
        assertEquals("Take should not mess with number of all tiles." , true, passed);

        ArrayList<Tile> refill = bag.take(82);
        assertEquals("If there are in bag less tiles then we need, bag will take what is strored in UsedTiles and refil", bag.state().length(), 18);
    }


}
