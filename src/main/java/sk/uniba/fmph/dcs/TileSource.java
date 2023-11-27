package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public interface TileSource {
    ArrayList<Tile> take (int idx);
    boolean isEmpty();
    void startNewRound();
    String State();
}
