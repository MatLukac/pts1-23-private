package sk.uniba.fmph.dcs;

import java.util.List;

public interface PatternLineInterface {
     void put(List<Tile> tiles);
     Points finishRound();
     String state();
}
