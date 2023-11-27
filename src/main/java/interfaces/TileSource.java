package interfaces;

import sk.uniba.fmph.dcs.Tile;

import java.util.ArrayList;

public interface TileSource {
    ArrayList<Tile> take(int idx);
    boolean isEmpty();
    void startNewRound();
    String state();
}
