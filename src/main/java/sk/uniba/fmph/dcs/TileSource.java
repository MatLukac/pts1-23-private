package sk.uniba.fmph.dcs;

import java.util.Collection;

public interface TileSource {
    Collection<Tile> take(int idx);
    boolean isEmpty();
    void startNewRound();
    String state();
}
