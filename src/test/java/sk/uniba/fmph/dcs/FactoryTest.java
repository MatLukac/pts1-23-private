package sk.uniba.fmph.dcs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

class FakeBag implements BagInterface {
    private ArrayList<Tile> tiles;

    public FakeBag() {
        tiles = new ArrayList<>();
        tiles.addAll(List.of(Tile.RED, Tile.GREEN, Tile.BLUE,Tile.RED, Tile.GREEN, Tile.BLUE, Tile.GREEN, Tile.BLUE));
    }
    @Override
    public ArrayList<Tile> take(int count) {
        ArrayList<Tile> toReturn = new ArrayList<>();
        for(int i = 0; i < count; i++) toReturn.add(tiles.get(i));
        return toReturn;
    }
    @Override
    public String state() {
        return null;
    }
}

class FakeTableCenter implements TableCenterAddInterface{
    public ArrayList<Tile> tiles = new ArrayList<>();

    @Override
    public void add(Collection<Tile> tiles) {
        this.tiles.addAll(tiles);
    }
}

public class FactoryTest {

    private FakeTableCenter tableCenter;
    private FakeBag bag;
    private Factory factory;

    @Before
    public void setUp(){
        tableCenter = new FakeTableCenter();
        bag = new FakeBag();
        factory = new Factory(bag,tableCenter);
    }

    @Test
    public void test_factory(){
        assertEquals("New factory should contain MAX_NUMBER_OF_TILES (4).", factory.state().length(), 4);
        assertEquals("Testing factory.state().", "RGBR", factory.state());
        assertEquals("Wrong index should yield nothing (null)." , true, factory.take(-1) == null && factory.take(5) == null);
        String state = factory.state();
        factory.take(-1);
        assertEquals("Factory after wrong index in take() should not change." ,factory.state(), state);
        assertEquals("When state() != '', then isEmpty() -> false.", false, factory.isEmpty());

        ArrayList<Tile> tiles = factory.take(0);
        assertEquals("After take factory should be empty.", true, factory.isEmpty());

        boolean allEqual = true;
        for(Tile tile : tiles)
            if(tile != tiles.get(0)) {
                allEqual = false;
                break;
            }

        assertEquals("factory.take() should return Tile[] of the same Tile.", true, allEqual);
        String s = "";
        for(Tile tile : tableCenter.tiles) s += tile.toString();
        assertEquals("Rest should go to TableCenter.", "GB", s);
        factory.startNewRound();
        assertEquals("After startNewRound(), factory should draw new tiles form bag", false, factory.isEmpty());
    }

}
