package sk.uniba.fmph.dcs;

import java.util.*;

public class PatternLine implements PatternLineInterface{
    private final int capacity;
    private final WallLineInterface wallLine;
    private final FloorPutInterface floor;
    private final UsedTilesGiveInterface usedTyles;
    private ArrayList<Tile> tiles;

    public PatternLine(int capacity, WallLineInterface wallLine, FloorPutInterface floor, UsedTilesGiveInterface usedTyles){
        this.capacity = capacity;
        this.wallLine = wallLine;
        this.floor = floor;
        this.usedTyles = usedTyles;
        tiles = new ArrayList();
    }

    @Override
    public void put(Collection<Tile> tiles) {
        if(!wallLine.canPutTile(tiles.iterator().next())){
            floor.put(tiles);
            return;
        }
        Collection<Tile> fallingTile = new ArrayList<>();
        for(Tile tile : tiles) {
            if(this.tiles.size() != capacity) this.tiles.add(tile);
            else fallingTile.add(tile);
        }
        if(fallingTile.size() != 0) usedTyles.give(fallingTile);
    }

    @Override
    public Points finishRound() {
        if(tiles.size() != capacity) return null;
        Tile tile = tiles.get(0);
        tiles.remove(tile);
        usedTyles.give(tiles);
        tiles.removeAll(tiles);
        return wallLine.putTile(tile);
    }

    @Override
    public String state() {
        String toReturn = "";
        for(final Tile tile : tiles) {
            toReturn += tile.toString();
        }
        return toReturn;
    }
}
