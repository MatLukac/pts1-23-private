package sk.uniba.fmph.dcs;

import java.util.Collection;

public interface BagInterface {
    Collection<Tile> take(int count);
    String state();
}
