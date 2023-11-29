package sk.uniba.fmph.dcs;

import interfaces.UsedTilesGiveInterface;
import interfaces.UsedTilesTakeInterface;

import java.util.ArrayList;
import java.util.Collection;

public final class UsedTiles implements UsedTilesGiveInterface, UsedTilesTakeInterface {
    private ArrayList<Tile> tiles;

    public UsedTiles() {
        tiles = new ArrayList<>();
    }

    public UsedTiles(final ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override

    public void give(final Collection<Tile> newTiles) {
        for (Tile tile : newTiles) {
            if (tile != Tile.STARTING_PLAYER) {
                tiles.add(tile);
            }
        }
    }

    @Override
    public Collection<Tile> takeAll() {
        ArrayList<Tile> toReturn = new ArrayList(tiles);
        tiles.removeAll(tiles);
        return toReturn;
    }

    public String state() {
        String toReturn = "";
        for (final Tile tile : tiles) {
            toReturn += tile.toString();
        }
        return toReturn;
    }
}
