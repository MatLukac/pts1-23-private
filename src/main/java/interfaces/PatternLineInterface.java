package interfaces;
import sk.uniba.fmph.dcs.Points;
import sk.uniba.fmph.dcs.Tile;

import java.util.Collection;
public interface PatternLineInterface {
    void put(Collection<Tile> tiles);
    Points finishRound();
    String state();
}
