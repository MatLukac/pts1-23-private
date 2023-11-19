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
        ArrayList<Tile> toReturn = new ArrayList<>();
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
