package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import interfaces.*;
import org.junit.Before;
import org.junit.Test;
import records.GiveUsedTilesResult;
import records.TakeBagResult;
import records.TakeUsedTilesResult;

class FakeUsedTilesBag implements UsedTilesInterface {
    public List<Tile> tiles;

    public FakeUsedTilesBag() {
        tiles = List.of(Tile.RED, Tile.BLACK, Tile.GREEN, Tile.RED);
    }

    @Override
    public TakeUsedTilesResult take() {
        return new TakeUsedTilesResult(tiles, new FakeUsedTilesBag());
    }

    @Override
    public GiveUsedTilesResult give() {
        return null; //dont need to be implemented
    }

    @Override
    public String state() {
        return tiles.stream().map(Tile::toString).collect(Collectors.joining(""));
    }
}

class FakeRandomGenerator implements RandomGeneratorInterface {
    Iterator<Integer> iterator = List.of(0, 67, 58, 45, 12, 23, 89).iterator(); //for determinist approach

    public FakeRandomGenerator() {
    }

    @Override
    public int nextInt(int bound) {
        return 0;
    }
}

public class BagTest {
    private FakeRandomGenerator fakeRandomGenerator;
    private FakeUsedTilesBag usedTiles;
    private BagInterface bag;

    @Before
    public void setUp() {
        fakeRandomGenerator = new FakeRandomGenerator();
        usedTiles = new FakeUsedTilesBag();
        bag = new Bag(fakeRandomGenerator);


    }

    @Test
    public void test_bag() {


        assertEquals("Bag when created should contain 100 tiles.", 100, bag.state().length());

        HashMap<String, Integer> m = new HashMap<>();
        for (String c : bag.state().split("")) {
            if (m.get(c) != null) m.put(c, m.get(c) + 1);
            else m.put(c, 1);
        }
        for (int i : m.values()) {
            assertEquals("There should be 20 tile of each color at the beginning.", 20, i);
        }

        ArrayList<Tile> takenFromBag = new ArrayList<>();

        TakeBagResult takeBagResult = bag.take(4, usedTiles);
        takenFromBag.addAll(takeBagResult.tiles());
        takeBagResult = takeBagResult.bag().take(5, usedTiles);
        takenFromBag.addAll(takeBagResult.tiles());
        takeBagResult = takeBagResult.bag().take(5, usedTiles);
        takenFromBag.addAll(takeBagResult.tiles());
        takeBagResult = takeBagResult.bag().take(6, usedTiles);
        takenFromBag.addAll(takeBagResult.tiles());
        bag = takeBagResult.bag();

        usedTiles.tiles = takenFromBag;


        assertEquals("There should be left 80 tiles in new bag after taken 20 tiles in groups.", bag.state().length(), 80);


        HashMap<Tile, Integer> mTake = new HashMap<>();
        for (Tile tile : takenFromBag) {
            if (mTake.get(tile) != null) mTake.put(tile, mTake.get(tile) + 1);
            else mTake.put(tile, 1);
        }
        m = new HashMap<>();
        for (String c : bag.state().split("")) {
            if (m.get(c) != null) m.put(c, m.get(c) + 1);
            else m.put(c, 1);
        }
        boolean passed = true;
        for (Tile tile : List.of(Tile.RED, Tile.GREEN, Tile.BLUE, Tile.YELLOW, Tile.YELLOW)) {
            int mTileNum, mTakeTileNum;
            if (mTake.get(tile) == null) mTakeTileNum = 0;
            else mTakeTileNum = mTake.get(tile);

            if (m.get(tile.toString()) == null) mTileNum = 0;
            else mTileNum = m.get(tile.toString());

            if (mTileNum + mTakeTileNum != 20) {
                passed = false;
                break;
            }
        }
        assertEquals("Take should not mess with number of all tiles.", true, passed);


        bag = bag.take(82, usedTiles).bag();
        assertEquals("If there are in bag less tiles then we need, take() will return new bag and will take what is strored in UsedTiles and refil", bag.state().length(), 18);
    }


}
