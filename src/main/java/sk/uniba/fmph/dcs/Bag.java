package sk.uniba.fmph.dcs;

import interfaces.BagInterface;
import interfaces.UsedTilesTakeInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Bag implements BagInterface {
    private static final int NUM_OF_TILES_OF_EACH_COLOR = 20;
    private ArrayList<Tile> tiles;
    private final UsedTilesTakeInterface usedTiles;

    public Bag(final UsedTilesTakeInterface usedTiles) {
        tiles = new ArrayList<>();
        this.usedTiles = usedTiles;

        for (Tile tile : List.of(Tile.RED, Tile.GREEN, Tile.YELLOW, Tile.BLUE, Tile.BLACK)) {
            for (int i = 0; i < NUM_OF_TILES_OF_EACH_COLOR; i++) {
                tiles.add(tile);
            }
        }

        Collections.shuffle(tiles);
    }

    @Override
    public ArrayList<Tile> take(final int count) {
        ArrayList<Tile> toReturn = new ArrayList<>();
        if (tiles.size() < count) {
            toReturn.addAll(tiles);
            tiles = new ArrayList<>(usedTiles.takeAll());
            Collections.shuffle(tiles);
        }
        Random rand = new Random();
        for (int i = toReturn.size(); i < count; i++) {
            Tile tile = tiles.get(rand.nextInt(tiles.size()));
            tiles.remove(tile);
            toReturn.add(tile);
        }

        return toReturn;
    }

    @Override
    public String state() {
        String s = "";
        for (Tile tile : tiles) {
            s += tile.toString();
        }
        return s;
    }
}
