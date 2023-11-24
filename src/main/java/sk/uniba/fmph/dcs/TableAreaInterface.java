package sk.uniba.fmph.dcs;

import java.util.ArrayList;

public interface TableAreaInterface {
    ArrayList<Tile> take(int sourceIdx, int idx);
    void startNewRound();
    String state();
}
