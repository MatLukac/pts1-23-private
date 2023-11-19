package sk.uniba.fmph.dcs;

import java.util.ArrayList;
import java.util.*;

public final class Bag implements BagInterface{
    private ArrayList<Tile> tiles;
    private final UsedTileTakeInterface usedTiles;

    public Bag(final UsedTileTakeInterface usedTiles) {
        tiles = new ArrayList<>();
        this.usedTiles = usedTiles;

        for(Tile tile : List.of(Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK))
            for(int i = 0; i < 20; i++)
                tiles.add(tile);

        Collections.shuffle(tiles);
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
