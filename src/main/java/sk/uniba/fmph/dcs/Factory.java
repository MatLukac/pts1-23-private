package sk.uniba.fmph.dcs;

import sun.tools.jconsole.Tab;

import java.util.ArrayList;
import java.util.Collection;

public class Factory implements TileSource{
    private static final int MAX_NUMBER_OF_TILES = 4;
    private final BagInterface bag;
    private final TableCenterAddInterface tableCenter;
    private ArrayList<Tile> tiles;

    public Factory(BagInterface bag, TableCenterAddInterface tableCenter){
        this.bag = bag;
        this.tableCenter = tableCenter;
        tiles = new ArrayList<>();
        startNewRound();
    }
    @Override
    public Collection<Tile> take(int idx) {
        ArrayList<Tile> toReturn = new ArrayList();
        ArrayList<Tile> toTableCenter = new ArrayList<>();
        if (idx < 0 || idx >= tiles.size()) return null;
        Tile chosenTile = tiles.get(idx);
        for(Tile tile : tiles) {
            if(tile.equals(chosenTile)) toReturn.add(tile);
            else toTableCenter.add(tile);
        }

        tableCenter.add(toTableCenter);
        return toReturn;
    }

    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    @Override
    public void startNewRound() {
        tiles.addAll(bag.take(MAX_NUMBER_OF_TILES));
    }

    @Override
    public String state() {
        return null;
    }
}
