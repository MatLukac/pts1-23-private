package sk.uniba.fmph.dcs;

import interfaces.FloorPutInterface;
import interfaces.PatternLineInterface;
import interfaces.UsedTilesGiveInterface;
import interfaces.WallLineInterface;

import java.util.ArrayList;
import java.util.Collection;

public final class PatternLine implements PatternLineInterface {
    private final int capacity;
    private final WallLineInterface wallLine;
    private final FloorPutInterface floor;
    private final UsedTilesGiveInterface usedTiles;
    private ArrayList<Tile> tiles;

    public PatternLine(final int capacity, final WallLineInterface wallLine, final FloorPutInterface floor, final UsedTilesGiveInterface usedTiles) {
        this.capacity = capacity;
        this.wallLine = wallLine;
        this.floor = floor;
        this.usedTiles = usedTiles;
        tiles = new ArrayList(capacity);
    }

    @Override
    public void put(final Collection<Tile> tiles) {
        if (tiles.size() == 0) {
            return;
        }
        if (tiles.iterator().next() == Tile.STARTING_PLAYER || !wallLine.canPutTile(tiles.iterator().next()) || (this.tiles.size() != 0 && this.tiles.get(0) != tiles.iterator().next())) {
            floor.put(tiles);
            return;
        }
        Collection<Tile> fallingTile = new ArrayList<>();
        for (Tile tile : tiles) {
            if (this.tiles.size() != capacity) {
                this.tiles.add(tile);
            } else {
                fallingTile.add(tile);
            }
        }
        if (fallingTile.size() != 0) {
            floor.put(fallingTile);
        }
    }

    @Override
    public Points finishRound() {
        if (tiles.size() != capacity) {
            return new Points(0);
        }
        Tile tile = tiles.get(0);
        tiles.remove(tile);
        usedTiles.give(tiles);
        tiles.removeAll(tiles);
        Points p = wallLine.putTile(tile);
        return p;
    }

    @Override
    public String state() {
        String toReturn = "";
        for (final Tile tile : tiles) {
            toReturn += tile.toString();
        }
        while (toReturn.length() != capacity) {
            toReturn += ".";
        }
        return toReturn;
    }
}
