package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;
import java.util.stream.Collectors;

import interfaces.*;
import org.junit.Before;
import org.junit.Test;
import records.*;

class FakeUsedTilesFactory implements UsedTilesInterface {
    public List<Tile> tiles;

    public FakeUsedTilesFactory() {
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

class FakeBagFactory implements BagInterface {
    private List<Tile> tiles;

    public FakeBagFactory() {
        tiles = new ArrayList<>();
        tiles.addAll(List.of(Tile.RED, Tile.GREEN, Tile.BLUE, Tile.RED, Tile.GREEN, Tile.BLUE, Tile.GREEN, Tile.BLUE));
    }

    public FakeBagFactory(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }


    @Override
    public TakeBagResult take(int count, UsedTilesInterface usedTiles) {
        List<Tile> out = Collections.unmodifiableList(tiles.subList(0, count));
        tiles = Collections.unmodifiableList(tiles.subList(count, tiles.size()));
        return new TakeBagResult(out, this, usedTiles);
    }

    @Override
    public String state() {
        return tiles.stream().map(Tile::toString).collect(Collectors.joining(""));
    }
}


class FakeTableCenter implements TableCenterInterface {
    public ArrayList<Tile> tiles = new ArrayList<>();

    @Override
    public AddTableCenterResult add(List<Tile> tiles) {
        this.tiles.addAll(tiles);
        return new AddTableCenterResult(this);
    }

    @Override
    public TakeTableCenterResult take(int idx) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public StartNewRoundTableCenterResult startNewRound() {
        return null;
    }

    @Override
    public String state() {
        return tiles.stream().map(Tile::toString).collect(Collectors.joining(""));
    }
}

public class FactoryTest {

    private FakeUsedTilesFactory usedTiles;
    private FakeTableCenter tableCenter;
    private FakeBagFactory bag;
    private FactoryInterface factory;


    @Before
    public void setUp() {
        tableCenter = new FakeTableCenter();
        bag = new FakeBagFactory();
        factory = new Factory();
        usedTiles = new FakeUsedTilesFactory();
    }

    @Test
    public void test_factory() {
        factory = factory.startNewRound(bag, usedTiles).factory();

        assertEquals("New Factory after startNewRound() should contain MAX_NUMBER_OF_TILES (4).", factory.state().length(), 4);
        assertEquals("Testing factory.state().", "RGBR", factory.state());

        String state = factory.state();
        assertThrows(IllegalArgumentException.class, () -> factory.take(-1, tableCenter));
        //assertEquals("Factory after wrong index in take() should not change.", factory.state(), state);
        assertEquals("When state() != '', then isEmpty() -> false.", false, factory.isEmpty());


        TakeFactoryResult takeFactory = factory.take(0, tableCenter);
        factory = takeFactory.factory();
        assertEquals("After take, new factory should be empty.", true, factory.isEmpty());

        boolean allEqual = true;
        for (Tile tile : takeFactory.tiles()) {
            if (tile != takeFactory.tiles().get(0)) {
                allEqual = false;
                break;
            }
        }

        assertEquals("factory.take() should return Tile[] of the same Tile.", true, allEqual);
        String s = tableCenter.tiles.stream().map(Tile::toString).collect(Collectors.joining(""));
        assertEquals("Rest should go to TableCenter.", "GB", s);
        StartNewRoundFactoryResult startNewRoundFactory = factory.startNewRound(bag, usedTiles);
        assertEquals("After startNewRound(), factory should draw new tiles form bag", false, startNewRoundFactory.factory().isEmpty());
    }


}
