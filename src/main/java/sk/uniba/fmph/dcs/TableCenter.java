package sk.uniba.fmph.dcs;

import interfaces.TableCenterAddInterface;
import interfaces.TileSource;

import java.util.ArrayList;
import java.util.Collection;

public final class TableCenter implements TileSource, TableCenterAddInterface {
    private ArrayList<Tile> tiles;
    private boolean isFirst;

    public TableCenter() {
        tiles = new ArrayList<Tile>();
        isFirst = true;
    }

    @Override
    public void add(final Collection<Tile> tiles) {
        this.tiles.addAll(tiles);
    }

    @Override
    public ArrayList<Tile> take(final int idx) {
        if (idx < 0 || idx > tiles.size()) {
            throw new IllegalArgumentException("index not in tiles[]");
        }
        Tile pickedTile = tiles.get(idx);
        ArrayList<Tile> pickedTiles = new ArrayList<Tile>();
        if (isFirst) {
            pickedTiles.add(tiles.remove(0));
        }
        isFirst = false;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) == pickedTile) {
                pickedTiles.add(tiles.remove(i));
                i--;
            }
        }
        return pickedTiles;
    }

    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    @Override
    public void startNewRound() {
        tiles = new ArrayList<Tile>();
        isFirst = true;
        tiles.add(Tile.STARTING_PLAYER);
    }

    @Override
    public String state() {
        String toReturn = "";
        for (final Tile tile : tiles) {
            toReturn += tile.toString();
        }
        return toReturn;
    }
}
