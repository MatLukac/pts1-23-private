package sk.uniba.fmph.dcs;

import java.awt.List;
import java.util.*;
import java.util.random.*;

public final class Bag implements BagInterface{
    private ArrayList<Tile> tiles;
    private final UsedTileTakeInterface usedTiles;

    public Bag(UsedTileTakeInterface usedTiles) {
        tiles = new ArrayList<>();
        this.usedTiles = usedTiles;
    }

    @Override
    public ArrayList<Tile> take(int count) {
        ArrayList<Tile> toReturn = new ArrayList<>();
        if(tiles.size() < count) {
            toReturn.addAll(tiles);
            tiles = new ArrayList<>(usedTiles.takeAll());
            Collections.shuffle(tiles);
        }
        Random rand = new Random();
        for(int i = toReturn.size(); i <= count; i++) {
            Tile tile = tiles.get(rand.nextInt(tiles.size()));
            tiles.remove(tile);
            toReturn.add(tile);
        }

        return toReturn;
    }

    @Override
    public String state() {
        String s = "";
        for(Tile tile : tiles)
            s += tile.toString();
        return s;
    }
}
