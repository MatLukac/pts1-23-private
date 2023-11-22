package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public interface BagInterface {
    ArrayList<Tile> take(int count);
    String state();
}
