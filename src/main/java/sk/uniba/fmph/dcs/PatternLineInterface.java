package sk.uniba.fmph.dcs;
import java.util.Collection;
public interface PatternLineInterface {
    void put(Collection<Tile> tiles);
    Points finishRound();
    String state();
}
