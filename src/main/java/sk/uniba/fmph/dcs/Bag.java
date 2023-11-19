package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public class Bag implements BagInterface{
    private ArrayList<Tile> tiles;
    private final UsedTileTakeInterface usedTiles;
    public Bag(UsedTileTakeInterface usedTiles){
        tiles = new ArrayList<>();
        this.usedTiles = usedTiles;
    }
    @Override
    public ArrayList<Tile> take(int count) {
        return null;
    }

    @Override
    public String state() {
        return null;
    }
}
